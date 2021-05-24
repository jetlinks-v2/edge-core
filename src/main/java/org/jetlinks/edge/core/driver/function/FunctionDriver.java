package org.jetlinks.edge.core.driver.function;

import org.jetlinks.core.message.function.FunctionInvokeMessage;
import org.jetlinks.core.metadata.DataType;
import org.jetlinks.core.metadata.FunctionMetadata;
import org.jetlinks.core.metadata.PropertyMetadata;
import org.jetlinks.edge.core.driver.Driver;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * 功能调用驱动,可以理解为物模型中的功能,{@link Driver#getId()}则为物模型中定义的功能ID.
 * <p>
 * 场景:
 * <p>
 * 设备接入:
 * <ul>
 *     <li>定义一个功能: StartDeviceGateway (启动设备接入网关)</li>
 *     <li>参数: 网络类型,协议包地址</li>
 * </ul>
 *
 * @author zhouhao
 * @see Function
 * @since 1.0
 */
public interface FunctionDriver extends Driver {

    /**
     * 执行功能,参数与{@link FunctionInvokeMessage#getInputs()}一致
     *
     * @param parameter 参数
     * @return 返回结果流
     */
    Flux<Object> invoke(Map<String, Object> parameter);

    /**
     * 获取此功能的物模型定义信息
     *
     * @return 物模型功能定义
     */
    FunctionMetadata getMetadata();
}
