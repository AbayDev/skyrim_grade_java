package com.skyrimgrade.infrastructure.http.router;

import java.lang.reflect.Method;

import com.skyrimgrade.infrastructure.exception.RouterConfigurationException;
import com.skyrimgrade.infrastructure.http.HttpContext;
import com.skyrimgrade.infrastructure.http.annotations.Controller;
import com.skyrimgrade.infrastructure.http.annotations.Delete;
import com.skyrimgrade.infrastructure.http.annotations.Get;
import com.skyrimgrade.infrastructure.http.annotations.Patch;
import com.skyrimgrade.infrastructure.http.annotations.Post;
import com.skyrimgrade.infrastructure.http.annotations.Put;

public class RouterScanner implements RouterScannerInterface {

    private final RouterInterface router;

    public RouterScanner(RouterInterface router) {
        this.router = router;
    }

    @Override
    public void scan(Object... controllers) {
        for (Object controller : controllers) {
            scanOne(controller);
        }
    }

    private void validateMethod(Method method) throws IllegalStateException {
        if (method.getParameterCount() != 1 || !method.getParameterTypes()[0].equals(HttpContext.class)) {
            throw new RouterConfigurationException("The method " + method.getName() + " must only accept an HttpContext argument");
        }
    }

    private void scanOne(Object controller) {

        String prefix = "";

        if (controller.getClass().isAnnotationPresent(Controller.class)) {
            prefix = controller.getClass().getAnnotation(Controller.class).value();
        }

        for (Method method : controller.getClass().getDeclaredMethods()) {
            boolean isHandler = method.isAnnotationPresent(Get.class) || method.isAnnotationPresent(Post.class) || method.isAnnotationPresent(Put.class) || method.isAnnotationPresent(Delete.class) || method.isAnnotationPresent(Patch.class);

            if (!isHandler) {
                continue;
            }

            validateMethod(method);

            RouteHandler handler = ctx -> method.invoke(controller, ctx);

            if (method.isAnnotationPresent(Get.class)) {
                String path = method.getAnnotation(Get.class).value();
                router.get(prefix + path, handler);
            }

            if (method.isAnnotationPresent(Post.class)) {
                String path = method.getAnnotation(Post.class).value();
                router.post(prefix + path, handler);
            }

            if (method.isAnnotationPresent(Put.class)) {
                String path = method.getAnnotation(Put.class).value();
                router.put(prefix + path, handler);
            }

            if (method.isAnnotationPresent(Patch.class)) {
                String path = method.getAnnotation(Patch.class).value();
                router.patch(prefix + path, handler);
            }

            if (method.isAnnotationPresent(Delete.class)) {
                String path = method.getAnnotation(Delete.class).value();
                router.delete(prefix + path, handler);
            }

        }
    }

}
