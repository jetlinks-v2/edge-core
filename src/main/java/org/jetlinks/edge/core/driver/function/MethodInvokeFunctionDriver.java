package org.jetlinks.edge.core.driver.function;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.SneakyThrows;
import org.hswebframework.web.aop.MethodInterceptorHolder;
import org.hswebframework.web.bean.FastBeanCopier;
import org.jetlinks.core.metadata.DataType;
import org.jetlinks.core.metadata.FunctionMetadata;
import org.jetlinks.core.metadata.PropertyMetadata;
import org.jetlinks.core.metadata.types.ObjectType;
import org.jetlinks.supports.official.DeviceMetadataParser;
import org.jetlinks.supports.official.JetLinksPropertyMetadata;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 使用反射调用java方法来实现功能调用
 *
 * @author zhouhao
 * @since 1.9
 */
public class MethodInvokeFunctionDriver implements FunctionDriver, FunctionMetadata {

    @Getter
    private final String id;

    @Getter
    private final String name;

    private final Object instance;

    private final Method method;

    private final String[] argNames;

    private final ResolvableType[] argTypes;

    private Function<Map<String, Object>, Object> invoker;

    private List<PropertyMetadata> inputs;

    private DataType output;

    public MethodInvokeFunctionDriver(String id,
                                      String name,
                                      Object instance,
                                      Method method) {
        this.instance = instance;
        this.method = method;
        this.id = id;
        this.name = name;

        this.argTypes = new ResolvableType[method.getParameterCount()];
        this.argNames = new String[method.getParameterCount()];
        this.inputs = new ArrayList<>();
        for (int i = 0; i < method.getParameterCount(); i++) {
            this.argTypes[i] = ResolvableType.forMethodParameter(method, i);
        }
        String[] discoveredArgName = MethodInterceptorHolder.nameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < this.argNames.length; i++) {
            Parameter parameter = parameters[i];
            Schema schemaAnn = parameter.getAnnotation(Schema.class);
            if (schemaAnn != null && StringUtils.hasText(schemaAnn.name())) {
                this.argNames[i] = schemaAnn.name();
            } else {
                this.argNames[i] = (discoveredArgName != null && discoveredArgName.length > i) ? discoveredArgName[i] : "arg" + i;
            }
        }

        ResolvableType returnType = ResolvableType.forMethodReturnType(method);
        if (!returnIsVoid(returnType)) {
            this.output = DeviceMetadataParser.withType(returnType);
        }

        if (argTypes.length == 0) {
            this.invoker = ignore -> doInvoke();
        } else {
            if (argTypes.length == 1) {
                RequestBody requestBody = AnnotationUtils.findAnnotation(method.getParameters()[0], RequestBody.class);
                if (requestBody != null) {
                    DataType metadataType = DeviceMetadataParser.withType(argTypes[0]);
                    if (metadataType instanceof ObjectType) {
                        this.inputs = ((ObjectType) metadataType).getProperties();
                    }
                    invoker = parameter -> {
                        ResolvableType type = argTypes[0];
                        return doInvoke(
                            FastBeanCopier.DEFAULT_CONVERT.convert(parameter, type.toClass(), type.resolveGenerics())
                        );
                    };
                }
            }
            if (this.invoker == null) {
                for (int i = 0; i < argTypes.length; i++) {
                    ResolvableType type = argTypes[i];
                    Parameter parameter = parameters[i];
                    Schema schemaAnn = parameter.getAnnotation(Schema.class);
                    DataType dataType = DeviceMetadataParser.withType(type);
                    JetLinksPropertyMetadata metadata = new JetLinksPropertyMetadata();
                    metadata.setId(argNames[i]);
                    metadata.setName(schemaAnn == null || StringUtils.isEmpty(schemaAnn.description())
                                         ? argNames[i]
                                         : schemaAnn.description());
                    metadata.setDataType(dataType);
                    this.inputs.add(metadata);
                }
                this.invoker = parameter -> {
                    Object[] args = new Object[argTypes.length];
                    for (int i = 0; i < argTypes.length; i++) {
                        ResolvableType type = argTypes[i];
                        Object val = parameter.get(argNames[i]);
                        if (!type.isInstance(val)) {
                            val = FastBeanCopier.DEFAULT_CONVERT.convert(val, type.toClass(), type.resolveGenerics());
                        }
                        args[i] = val;
                    }
                    return doInvoke(args);
                };
            }
        }
    }

    @SneakyThrows
    private Object doInvoke(Object... args) {
        return method.invoke(instance, args);
    }

    private boolean returnIsVoid(ResolvableType type) {
        if (type.getGenerics().length > 0) {
            return returnIsVoid(type.getGeneric(0));
        }
        return type.toClass() == Void.class || type.toClass() == void.class;
    }

    @Override
    @SuppressWarnings("all")
    public Flux<Object> invoke(Map<String, Object> parameter) {
        return Flux
            .defer(() -> {
                Object response = invoker.apply(parameter);
                if (response instanceof Publisher) {
                    return ((Publisher<Object>) response);
                }
                return Mono.justOrEmpty(response);
            });
    }

    @Override
    public FunctionMetadata getMetadata() {
        return this;
    }

    @Override
    public void dispose() {

    }

    @Override
    public @NotNull List<PropertyMetadata> getInputs() {
        return inputs;
    }

    @Override
    public @NotNull DataType getOutput() {
        return output;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Map<String, Object> getExpands() {
        return new HashMap<>();
    }
}
