package com.molean.isletopia.shared;

import com.molean.isletopia.shared.annotations.AutoInject;
import com.molean.isletopia.shared.annotations.Bean;
import com.molean.isletopia.shared.annotations.BeanHandler;
import com.molean.isletopia.shared.annotations.BeanHandlerPriority;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.jar.JarEntry;

public enum ClassResolver {
    INSTANCE;


    //所有 com.molean.isletopia 下的类
    private final Set<Class<?>> classSet = new HashSet<>();

    //由 ClassResolver 管理的所有 bean
    private final Set<Object> objects = new HashSet<>();

    //用于处理注解的Handlers
    private final Set<BeanHandler> beanHandlers = new HashSet<>();

    /**
     * 获取一个类及其父类所包含的所有注解, except中的除外
     *
     * @param clazz  目标类
     * @param except 排除
     * @return 注解集合
     */
    public Set<Class<?>> getAnnotations(Class<?> clazz, Set<Class<?>> except) {
        List<? extends Class<? extends Annotation>> annotations
                = Arrays.stream(clazz.getAnnotations())
                .map(Annotation::annotationType)
                .filter(aClass -> !except.contains(aClass))
                .toList();

        HashSet<Class<?>> finalAnnotations = new HashSet<>(annotations);
        except.addAll(annotations);
        for (Class<?> annotation : annotations) {
            Set<Class<?>> annotationAnnotations = getAnnotations(annotation, except);
            finalAnnotations.addAll(annotationAnnotations);
        }
        return finalAnnotations;
    }

    /**
     * 是否需要扫描并处理
     *
     * @param clazz 目标类
     * @return 布尔值
     */
    public boolean shouldScan(Class<?> clazz) {

        if (clazz.isAnnotation()) {
            return false;
        }
        Set<Class<?>> annotations = getAnnotations(clazz, new HashSet<>());

        return annotations.contains(Bean.class);
    }


    /**
     * 从容器获取对象
     *
     * @param parameter 根据参数
     * @return 目标对象, 不存在则返回null
     */
    public Object getObject(Parameter parameter) {
        Class<?> type = parameter.getType();
        return getObject(type);
    }

    /**
     * 从容器获取对象
     *
     * @param field 根据字段
     * @return 目标对象, 不存在则返回null
     */
    public Object getObject(Field field) {
        Class<?> type = field.getType();
        return getObject(type);
    }

    /**
     * 从容器获取对象
     *
     * @param type 根据类
     * @return 目标对象, 不存在则返回null
     */
    public <T> T getObject(Class<T> type) {
        for (Object value : objects) {
            if (type.isInstance(value)) {
                return (T) value;
            }
        }
        return null;
    }


    /**
     * 为类中的空字段自动注入容器中的对象
     *
     * @throws Exception 异常
     */
    public void resolveFieldInject() throws Exception {
        for (Object object : objects) {
            Field[] declaredFields = object.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(AutoInject.class)) {
                    declaredField.setAccessible(true);
                    Object origin = declaredField.get(object);
                    if (origin == null) {
                        Object tobeInject = getObject(declaredField);
                        declaredField.set(object, tobeInject);
                    }
                }
            }
        }

    }

    public void addBean(Object object) {
        objects.add(object);
    }


    /**
     * 为@Bean注解的类创建对象并使用BeanHandler处理所有对象
     *
     * @throws Exception 异常
     */
    public void resolveBean() throws Exception {

        Set<Class<?>> targetClass = new HashSet<>();

        for (Class<?> clazz : classSet) {
            if (shouldScan(clazz)) {
                targetClass.add(clazz);
            }
        }

        while (true) {

            int resolved = 0;
            for (Class<?> clazz : new HashSet<>(targetClass)) {
                Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
                constructor:
                for (Constructor<?> declaredConstructor : declaredConstructors) {
                    ArrayList<Object> parameters = new ArrayList<>();
                    for (Parameter parameter : declaredConstructor.getParameters()) {
                        Object object = getObject(parameter);
                        if (object != null) {
                            parameters.add(object);
                        } else {
                            break constructor;
                        }
                    }
                    Object object = declaredConstructor.newInstance(parameters.toArray());

                    resolved++;
                    targetClass.remove(clazz);

                    if (object instanceof BeanHandler) {
                        beanHandlers.add((BeanHandler) object);
                    } else {
                        objects.add(object);

                    }
                    break;
                }
            }
            for (Object value : new HashSet<>(objects)) {
                Method[] declaredMethods = value.getClass().getDeclaredMethods();
                method:
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.isAnnotationPresent(Bean.class)) {
                        ArrayList<Object> parameters = new ArrayList<>();
                        for (Parameter parameter : declaredMethod.getParameters()) {
                            Object object = getObject(parameter);
                            if (object != null) {
                                parameters.add(object);
                            } else {
                                break method;
                            }
                        }
                        Object object = declaredMethod.invoke(value, parameters.toArray());
                        resolved++;
                        if (object instanceof BeanHandler) {
                            beanHandlers.add((BeanHandler) object);
                        } else {
                            objects.add(object);
                        }
                    }
                }
            }


            if (resolved == 0 || targetClass.size() == 0) {
                break;
            }
        }


        int size = targetClass.size();
        if (size > 0) {
            PlatformRelatedUtils.getInstance().getLogger().severe("There are %d classes, which cannot be construct!".formatted(size));

            for (Class<?> aClass : targetClass) {
                PlatformRelatedUtils.getInstance().getLogger().severe(aClass.getName());
            }
        }
        PlatformRelatedUtils.getInstance().getLogger().info("%d classes was constructed successfully!".formatted(objects.size()));
        PlatformRelatedUtils.getInstance().getLogger().info("%d bean handlers was constructed successfully!".formatted(beanHandlers.size()));

        ArrayList<BeanHandler> beanHandlerArrayList = new ArrayList<>(beanHandlers);

        beanHandlerArrayList.sort((o1, o2) -> {
            int p1 = 0;
            int p2 = 0;

            if (o1.getClass().isAnnotationPresent(BeanHandlerPriority.class)) {
                p1 = o1.getClass().getAnnotation(BeanHandlerPriority.class).value();
            }
            if (o2.getClass().isAnnotationPresent(BeanHandlerPriority.class)) {
                p2 = o2.getClass().getAnnotation(BeanHandlerPriority.class).value();
            }
            return p2 - p1;
        });
        for (BeanHandler beanHandler : beanHandlerArrayList) {
            for (Object value : objects) {
                beanHandler.handle(value);
            }
        }
    }


    /**
     * 加载此 com.molean.isletopia 下的所有类
     *
     * @throws Exception 异常
     */
    public void loadClass() throws Exception {
        if (classSet.size() != 0) {
            throw new RuntimeException("Classes was already loaded!");
        }

        Enumeration<JarEntry> entries = PlatformRelatedUtils.getInstance().getJarFile().entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class")) {
                name = name.substring(0, name.length() - 6);
                name = name.replaceAll("/", ".");
                if (name.startsWith("com.molean.isletopia")) {
                    Class<?> aClass = PlatformRelatedUtils.getInstance().loadClass(name);
                    if (aClass != getClass()) {
                        classSet.add(aClass);
                    }
                }
            }

        }
    }

}
