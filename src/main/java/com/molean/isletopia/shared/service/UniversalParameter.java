package com.molean.isletopia.shared.service;

import com.molean.isletopia.shared.database.PlayerParameterDao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UniversalParameter {
    @Nullable
    public static String getParameter(UUID uuid, String key) {
        return PlayerParameterDao.get(uuid, key);
    }


    public static List<String> getParameterAsList(UUID uuid, String key) {
        ArrayList<String> list = new ArrayList<>();
        String parameter = getParameter(uuid, key);
        if (parameter != null && !parameter.trim().equals("")) {
            list.addAll(Arrays.asList(parameter.split(",")));
        }
        return list;
    }


    public static void setParameter(UUID uuid, String key, String value) {
        PlayerParameterDao.set(uuid, key, value);
    }


    public static void addParameter(@NotNull UUID uuid, String key, String value) {
        String before = PlayerParameterDao.get(uuid, key);
        if (before == null || before.trim().equals("")) {
            PlayerParameterDao.set(uuid, key, value);
        } else {
            List<String> strings = Arrays.asList(before.split(","));
            List<String> newStrings = new ArrayList<>(strings);
            newStrings.add(value);
            newStrings = newStrings.stream().distinct().collect(Collectors.toList());
            String join = String.join(",", newStrings);
            PlayerParameterDao.set(uuid, key, join);
        }
    }


    public static void removeParameter(UUID uuid, String key, String value) {
        String before = PlayerParameterDao.get(uuid, key);
        if (before == null || before.trim().equals("")) {
            PlayerParameterDao.set(uuid, key, value);
        } else {
            List<String> strings = Arrays.asList(before.split(","));
            List<String> newStrings = new ArrayList<>(strings);
            newStrings.remove(value);
            String join = String.join(",", newStrings);
            PlayerParameterDao.set(uuid, key, join);
        }
    }


    public static void unsetParameter(UUID uuid, String key) {
        PlayerParameterDao.unset(uuid, key);
    }


}
