package com.skyrimgrade.infrastructure.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface DIContainerInterface {

    DIContainerInterface register(Class<?> iface, Class<?> impl, Scope scope) throws DIContainerException;

    DIContainerInterface register(Class<?> iface, Class<?> impl) throws DIContainerException;

    DIContainerInterface register(Class<?> impl, Scope scope) throws DIContainerException;

    DIContainerInterface register(Class<?> impl) throws DIContainerException;

    DIContainerInterface singletone(Class<?> iface, Object instance) throws DIContainerException;

    <T> T get(Class<T> iface) throws DIContainerException;
}

public class DIContainer implements DIContainerInterface {

    private record Binding(Class<?> impl, Scope scope) {

    }

    private final Map<Class<?>, Binding> bindings = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> singletons = new ConcurrentHashMap<>();

    @Override
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

        if (bindings.containsKey(iface)) {
            throw new DIContainerException("Class " + iface.getName() + " is already registered");
        }

        bindings.put(iface, new Binding(impl, scope));

        return this;
    }

    @Override
    public DIContainerInterface singletone(Class<?> iface, Object instance) throws DIContainerException {
        if (singletons.containsKey(iface)) {
            throw new DIContainerException("Class " + iface.getName() + " is already registered");
        }

        singletons.put(iface, instance);

        return this;
    }

    @Override
    public <T> T get(Class<T> type) throws DIContainerException {
        return (T) resolve(type);
    }

    public Object resolve(Class<?> type) throws DIContainerException {
        Binding binding = bindings.get(type);
        if (binding == null) {
            throw new DIContainerException("Type " + type.getName() + " not registered in DI container");
        }

        if (binding.scope() == Scope.SINGLETONE) {
            Object singletone = singletons.get(type);
            if (singletone != null) {
                return singletone;
            }
        }

        try {
            Object instance = this.createInstance(binding.impl());

            if (binding.scope() == Scope.SINGLETONE) {
                singletons.put(type, instance);
            }

            return instance;
        } catch (DIContainerException e) {
            throw e;
        } catch (Exception e) {
            throw new DIContainerException("Failed to create " + type.getName());
        }
    }

    private Object createInstance(Class<?> type) throws DIContainerException {
        Constructor<?> ctor = selectConstructor(type);
        ctor.setAccessible((true));

        Class<?>[] paramTypes = ctor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = resolve(paramTypes[i]);
        }

        try {
            return ctor.newInstance(args);
        } catch (InvocationTargetException e) {
            throw new DIContainerException(
                    "Constructor of " + ctor.getName() + " threw an exception " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage())
            );
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DIContainerException(
                    "Failed to instantiate " + type.getName() + ": " + e.getMessage()
            );
        }
    }

    private Constructor<?> selectConstructor(Class<?> type) throws DIContainerException {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        Constructor<?> annotatedCtor = Arrays.stream(constructors)
                .filter((ctor) -> ctor.isAnnotationPresent(Inject.class))
                .findFirst()
                .orElse(null);

        if (annotatedCtor != null) {
            return annotatedCtor;
        }

        if (constructors.length == 1) {
            return constructors[0];
        }

        throw new DIContainerException("The " + type.getName() + " class uses multiple constructors without @Inject. Add the Inject annotation to the constructor that requires dependency injection container.");
    }

}
