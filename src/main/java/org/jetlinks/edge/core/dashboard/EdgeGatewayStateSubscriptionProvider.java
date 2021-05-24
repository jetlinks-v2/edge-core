package org.jetlinks.edge.core.dashboard;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.exception.BusinessException;
import org.jetlinks.edge.core.EdgeOperations;
import org.jetlinks.pro.gateway.external.SubscribeRequest;
import org.jetlinks.pro.gateway.external.SubscriptionProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author bestfeng
 * @since 1.9
 */
@Component
@AllArgsConstructor
@Slf4j
public class EdgeGatewayStateSubscriptionProvider implements SubscriptionProvider {

    private final EdgeOperations edgeOperations;


    @Override
    public String id() {
        return "edge-gateway-state";
    }

    @Override
    public String name() {
        return "边缘网关运行状态";
    }

    @Override
    public String[] getTopicPattern() {
        return new String[]{"/edge-gateway-state/**"};
    }

    @Override
    public Flux<?> subscribe(SubscribeRequest request) {
        return edgeOperations
            .listenSate(request
                            .getString("deviceId")
                            .orElseThrow(() -> new BusinessException("设备Id不存在")));
    }

}
