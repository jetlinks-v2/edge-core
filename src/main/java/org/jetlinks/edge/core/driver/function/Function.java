package org.jetlinks.edge.core.driver.function;

import org.jetlinks.core.message.function.FunctionInvokeMessage;
import org.jetlinks.core.message.function.FunctionParameter;
import org.jetlinks.core.metadata.FunctionMetadata;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.*;

/**
 * 在方法上添加此注解,用于声明一个功能.对应的方法将被创建为{@link FunctionDriver}.
 * 当接收到{@link FunctionInvokeMessage}时,{@link Function#id()}与{@link FunctionInvokeMessage#getFunctionId()}匹配时,将执行对应的方法.
 * <p>
 * 在参数上注解{@link RequestBody}时,声明将整个参数映射到body中。否则按照方法到参数名进行映射，参数名与{@link FunctionParameter#getName()}对应。
 * <p>
 * 例子:
 * <pre>
 *     &#064;Function(id="customFunction",name="自定义功能")
 *     public Mono&lt;Void&gt; handleInvoke(int nums){ //nums为参数名
 *         return doSomething(nums);
 *     }
 *
 *     &#064;Function(id="customFunction2",name="自定义功能2")
 *     public Mono&lt;Void&gt; handleInvoke(@RequestBody MyEntity entity){ //参数名与实体属性名对应
 *         return doSomething(entity);
 *     }
 * </pre>
 * <p>
 * 注意: 参数不支持响应式类型(Mono,Flux...),使用{@link RequestBody}时,方法只能有一个参数.
 *
 * @see org.jetlinks.core.message.function.FunctionInvokeMessage
 * @see FunctionDriver
 * @see RequestBody
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Function {
    /**
     * 对应物模型中到功能ID。
     *
     * @return 功能ID
     * @see FunctionMetadata#getId()
     */
    String id();

    /**
     * 功能名称
     *
     * @return 名称
     * @see FunctionMetadata#getName()
     */
    String name();

    /**
     * @return 说明
     * @see FunctionMetadata#getDescription()
     */
    String description() default "";

    /**
     * 参数物模型定义类
     *
     * @return 物模型
     */
    Class<?> parameterMetadata() default Void.class;

}
