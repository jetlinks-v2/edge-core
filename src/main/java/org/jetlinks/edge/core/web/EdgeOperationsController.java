package org.jetlinks.edge.core.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.authorization.annotation.ResourceAction;
import org.hswebframework.web.i18n.LocaleUtils;
import org.jetlinks.edge.core.EdgeOperations;
import org.jetlinks.edge.core.entity.EdgeInfoDetail;
import org.jetlinks.edge.core.monitor.EdgeRunningState;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
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

    @GetMapping(value = "/{functionId}/invoke/_batch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResourceAction(id = "invoke", name = "调用功能")
    @Operation(summary = "批量调用功能")
    public Flux<Object> invokeFunction(@PathVariable String functionId,
                                       @RequestParam @Schema(description = "设备ID") List<String> deviceId,
                                       @RequestParam(required = false)
                                       @Schema(description = "功能输入参数") Map<String, Object> params) {
        return Flux
            .fromIterable(deviceId)
            .buffer(200)
            .flatMap(Flux::fromIterable)
            .flatMap(id -> this
                .invokeFunction(functionId, id, Mono.justOrEmpty(params))
                .map(BatchOperationResult::success)
                .switchIfEmpty(LocaleUtils
                    .currentReactive()
                    .map(locale -> BatchOperationResult
                        .fail(LocaleUtils.resolveMessage("error.edge_device_not_exist", locale) + " deviceId: " + deviceId)))
                .onErrorResume(err -> Mono.just(BatchOperationResult.fail(err.getLocalizedMessage()))));
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
    public Mono<EdgeInfoDetail> edgeDeviceInfo(@PathVariable String deviceId) {
        return edgeOperations.edgeDeviceInfo(deviceId);
    }

    @GetMapping(value = "/{deviceId}/property/{property}")
    @ResourceAction(id = "device-property-state", name = "设备属性状态")
    public Mono<Object> devicePropertySate(@PathVariable String deviceId, @PathVariable String property) {
        return edgeOperations.getDevicePropertySate(deviceId, property);
    }

    @Getter
    @Setter
    public static class BatchOperationRequest {

        @Schema(description = "设备ID")
        List<String> deviceId;

        @Schema(description = "功能输入参数")
        Map<String, Object> params;

    }

    @Getter
    @Setter
    @Builder
    public static class BatchOperationResult {

        @Schema(description = "功能的输出值")
        private Object result;

        @Schema(description = "是否成功")
        private boolean successful;

        @Schema(description = "错误消息")
        private String message;

        @Schema(description = "时间")
        private long time;

        public static BatchOperationResult success(Object result) {
            return BatchOperationResult
                .builder()
                .successful(true)
                .time(System.currentTimeMillis())
                .result(result)
                .build();
        }

        public static BatchOperationResult fail(String message) {
            return BatchOperationResult
                .builder()
                .successful(false)
                .time(System.currentTimeMillis())
                .message(message)
                .build();
        }

    }
}
