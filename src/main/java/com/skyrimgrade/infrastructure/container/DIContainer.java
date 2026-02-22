package com.skyrimgrade.infrastructure.container;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

interface DIContainerInterface {

    DIContainerInterface register(Class<?> iface, Class<?> impl, Scope scope) throws DIContainerException;

    DIContainerInterface register(Class<?> iface, Class<?> impl) throws DIContainerException;

    DIContainerInterface register(Class<?> impl, Scope scope) throws DIContainerException;

    DIContainerInterface register(Class<?> impl) throws DIContainerException;

    <T> T get(Class<T> iface) throws DIContainerException;
}

public class DIContainer implements DIContainerInterface {

    private record Binding(Class<?> impl, Scope scope) {

    }

    private final Map<Class<?>, Binding> bindings = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> singletons = new ConcurrentHashMap<>();

    public DIContainerInterface register(Class<?> iface, Class<?> impl) throws DIContainerException {
        return register(iface, impl, Scope.SINGLETONE);
    }

    @Override
    public DIContainerInterface register(Class<?> impl, Scope scope) throws DIContainerException {
        return register(impl, impl, scope);
    }

    @Override
    public DIContainerInterface register(Class<?> impl) throws DIContainerException {
        return register(impl, impl, Scope.SINGLETONE);
    }

    @Override
    public DIContainerInterface register(Class<?> iface, Class<?> impl, Scope scope) throws DIContainerException {

        if (bindings.containsKey(impl)) {
            throw new DIContainerException("Class is already registered");
        }

        return this;
    }

    @Override
    public <T> T get(Class<T> type) throws DIContainerException {
        return resolve(type);
    }

    public <T> T resolve(Class<T> type) throws DIContainerException {
        Binding binding = bindings.get(type);
        if (binding == null) {
            throw new DIContainerException("Class not registered");
        }

        if (binding.scope() == Scope.SINGLETONE) {
            Object singletone = singletons.get(type);
            if (singletone != null) {
                return (T) singletone;
            }
        }

        try {
            var impl = binding.impl();

            Constructor<?>[] constructors = impl.getDeclaredConstructors();
            Arrays.stream(constructors)
                    .filter((constructor) -> constructor.isAnnotationPresent(Inject.class));

        } catch (Exception e) {
        }

    }

}
