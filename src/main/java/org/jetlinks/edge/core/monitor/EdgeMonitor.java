package org.jetlinks.edge.core.monitor;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * 边缘设备监控信息
 *
 * @author zhouhao
 * @since 1.0
 */
public interface EdgeMonitor {

    /**
     * 获取设备运行状态
     *
     * @return 运行状态
     */
    Mono<EdgeRunningState> getRunningState();

    Mono<Map<String, Object>> getSceneTriggerNum(List<Integer> triggerDay);

}
