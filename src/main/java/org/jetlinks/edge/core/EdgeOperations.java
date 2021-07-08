package org.jetlinks.edge.core;

import org.jetlinks.edge.core.entity.EdgeInfoDetail;
import org.jetlinks.edge.core.monitor.EdgeRunningState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 边缘网关操作接口
 *
 * @since 1.9
 */
public interface EdgeOperations {

    /**
     * 调用网关功能
     *
     * @param edgeDeviceId 边缘网关设备ID
     * @param function     功能标识
     * @param parameter    参数
     * @return 调用返回结果
     * @see org.jetlinks.edge.core.driver.function.FunctionDriver
     */
    Flux<Object> invokeFunction(String edgeDeviceId, String function, Map<String, Object> parameter);

    /**
     * 获取设备运行状态
     *
     * @param edgeDeviceId 边缘网关设备ID
     * @return 运行状态
     */
    Mono<EdgeRunningState> getState(String edgeDeviceId);

    /**
     * 监听设备实时运行状态
     *
     * @param edgeDeviceId 边缘网关设备ID
     * @return 运行状态
     */
    Flux<EdgeRunningState> listenSate(String edgeDeviceId);


    /**
     * 查询设备运行属性
     * @param edgeDeviceId 边缘网关设备ID
     * @param property 属性Id
     * @return
     */
    Mono<Object> getDevicePropertySate(String edgeDeviceId, String property);


    /**
     * 获取边缘网关信息
     *
     * @param edgeDeviceId 边缘网关设备ID
     */
    Mono<EdgeInfoDetail> edgeDeviceInfo(String edgeDeviceId);

}
