package org.jetlinks.edge.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 输入描述.
 *
 * @author zhangji 2023/1/28
 */
@Getter
@Setter
public class FrpClientConfig {

    @Schema(description = "传输协议类型，如tcp、udp")
    private FrpDistributeInfo.FrpcType type;

    @Schema(description = "客户端的端口")
    private Integer localPort;

    @Schema(description = "服务端透传代理的端口")
    private int remotePort;

}
