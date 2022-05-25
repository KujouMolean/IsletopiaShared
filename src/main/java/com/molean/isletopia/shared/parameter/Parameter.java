package com.molean.isletopia.shared.parameter;

public class Parameter<O> {
    private final String namespace;

    public Parameter(String namespace) {
        this.namespace = namespace;
    }

    public static <T>Parameter<T> of(String namespace) {
        return new Parameter<>(namespace);
    }

    public ParameterObject<O> get(O o) {
        return new ParameterObject<>(namespace, o);
    }

}
