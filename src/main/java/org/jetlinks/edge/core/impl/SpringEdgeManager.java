package org.jetlinks.edge.core.impl;

import org.jetlinks.edge.core.EdgeManager;
import org.jetlinks.edge.core.driver.Driver;
import org.jetlinks.edge.core.driver.function.Function;
import org.jetlinks.edge.core.driver.function.MethodInvokeFunctionDriver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SpringEdgeManager implements EdgeManager, BeanPostProcessor {

    private final Map<String, Driver> driverMap = new ConcurrentHashMap<>();

    @Override
    public Mono<Driver> getDriver(String id) {
        return Mono.justOrEmpty(driverMap.get(id));
    }

    @Override
    public Flux<Driver> getDrivers() {
        return Flux.fromIterable(driverMap.values());
    }

    public void register(Driver driver) {
        driverMap.put(driver.getId(), driver);
    }

    @Override
    public Object postProcessAfterInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        if (bean instanceof Driver) {
            register(((Driver) bean));
        }
        Class<?> clazz = ClassUtils.getUserClass(bean);
        ReflectionUtils.doWithMethods(clazz, method -> {
            Function function = AnnotationUtils.findAnnotation(method, Function.class);
            if (function == null) {
                return;
            }
            String id = function.id();
            String name = function.name();

            register(new MethodInvokeFunctionDriver(id, name, bean, method));

        });
        return bean;
    }

}
