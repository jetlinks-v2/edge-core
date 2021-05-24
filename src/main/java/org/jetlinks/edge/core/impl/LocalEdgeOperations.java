package org.jetlinks.edge.core.impl;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.jetlinks.edge.core.EdgeManager;
import org.jetlinks.edge.core.EdgeOperations;
import org.jetlinks.edge.core.driver.function.FunctionDriver;
import org.jetlinks.edge.core.monitor.EdgeMonitor;
import org.jetlinks.edge.core.monitor.EdgeRunningState;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

/**
 * 本地网关操作实现，忽略设备ID参数，用于直接操作本地网关。
 *
 * @author zhouhao
 * @since 1.8
 */
public class LocalEdgeOperations implements EdgeOperations {

    private final EdgeManager edgeManager;

    private final EdgeMonitor edgeMonitor;

    public LocalEdgeOperations(EdgeManager edgeManager,
                               EdgeMonitor edgeMonitor) {
        this.edgeManager = edgeManager;
        this.edgeMonitor = edgeMonitor;
    }

    @Override
    public Flux<Object> invokeFunction(String edgeDeviceId, String function, Map<String, Object> parameter) {

        return edgeManager
            .getDriver(function)
            .cast(FunctionDriver.class)
            .flatMapMany(driver -> driver.invoke(parameter));
    }

    @Override
    public Mono<EdgeRunningState> getState(String edgeDeviceId) {
        return edgeMonitor.getRunningState();
    }

    @Override
    public Flux<EdgeRunningState> listenSate(String edgeDeviceId) {
        return Flux
            .interval(Duration.ofSeconds(1))
            .flatMap(i -> getState(edgeDeviceId));
    }

    @Override
    public Mono<Object> getDevicePropertySate(String edgeDeviceId, String property) {
        return getState(edgeDeviceId)
            .map(state-> state.getPropertyValue(property));
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("all")
    public Mono<Object> edgeDeviceInfo(String edgeDeviceId) {
        try (InputStream inputStream = new ClassPathResource("edge-detail.json").getInputStream()){
            String edgeDetail = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            return Mono.just(JSON.parse(edgeDetail));
        }
    }
}
