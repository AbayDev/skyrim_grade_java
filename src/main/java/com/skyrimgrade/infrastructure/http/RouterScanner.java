package com.skyrimgrade.infrastructure.http;

import java.lang.reflect.Method;

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

    private void scanOne(Object controller) {

        String prefix = "";

        if (controller.getClass().isAnnotationPresent(Controller.class)) {
            prefix = controller.getClass().getAnnotation(Controller.class).value();
        }

        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Get.class)) {
                String path = method.getAnnotation(Get.class).value();
                router.get(prefix + path, (ctx) -> method.invoke(controller, ctx));
            }

            if (method.isAnnotationPresent(Post.class)) {
                String path = method.getAnnotation(Post.class).value();
                router.post(prefix + path, (ctx) -> method.invoke(controller, ctx));
            }

            if (method.isAnnotationPresent(Put.class)) {
                String path = method.getAnnotation(Put.class).value();
                router.put(prefix + path, (ctx) -> method.invoke(controller, ctx));
            }

            if (method.isAnnotationPresent(Patch.class)) {
                String path = method.getAnnotation(Patch.class).value();
                router.patch(prefix + path, (ctx) -> method.invoke(controller, ctx));
            }

            if (method.isAnnotationPresent(Delete.class)) {
                String path = method.getAnnotation(Delete.class).value();
                router.delete(prefix + path, (ctx) -> method.invoke(controller, ctx));
            }

        }
    }

}
