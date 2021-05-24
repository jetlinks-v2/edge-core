package org.jetlinks.edge.core.web;

import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.authorization.annotation.ResourceAction;
import org.jetlinks.edge.core.EdgeOperations;
import org.jetlinks.edge.core.monitor.EdgeRunningState;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/edge/operations")
@Resource(id = "edge-operations", name = "边缘网关-操作")
public class EdgeOperationsController {

    private final EdgeOperations edgeOperations;

    public EdgeOperationsController(EdgeOperations edgeOperations) {
        this.edgeOperations = edgeOperations;
    }

    @PostMapping("/{deviceId}/{functionId}/invoke")
    @ResourceAction(id = "invoke", name = "调用功能")
    public Flux<Object> invokeFunction(@PathVariable String functionId,
                                       @PathVariable String deviceId,
                                       @RequestBody(required = false) Mono<Map<String, Object>> params) {
        return params
            .defaultIfEmpty(Collections.emptyMap())
            .flatMapMany(param -> edgeOperations.invokeFunction(deviceId, functionId, param));
    }

    @GetMapping(value = "/{deviceId}/state", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResourceAction(id = "listen-state", name = "监听运行状态")
    public Flux<EdgeRunningState.FormatEdgeRunningState> listenState(@PathVariable String deviceId) {

        return edgeOperations
            .listenSate(deviceId)
            .map(EdgeRunningState::toFormatEdgeRunningState);
    }

    @GetMapping(value = "/{deviceId}/detail")
    @ResourceAction(id = "device-info", name = "网关详情")
    public Mono<Object> edgeDeviceInfo(@PathVariable String deviceId) {
        return edgeOperations.edgeDeviceInfo(deviceId);
    }


    @GetMapping(value = "/{deviceId}/property/{property}")
    @ResourceAction(id = "device-property-state", name = "设备属性状态")
    public Mono<Object> devicePropertySate(@PathVariable String deviceId, @PathVariable String property) {
        return edgeOperations.getDevicePropertySate(deviceId, property);
    }
}
