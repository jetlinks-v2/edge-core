package org.jetlinks.edge.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * frp下发信息.
 *
 * @author zhangji 2023/1/18
 */
@Getter
@Setter
public class FrpDistributeInfo {

    @Schema(description = "frp服务器地址")
    private String publicHost;

    @Schema(description = "frp服务器端口")
    private int bindPort;

    @Schema(description = "frp密钥")
    private String token;

    @Schema(description = "frp客户端配置")
    private List<FrpClientConfig> clientConfigList;

    @Getter
    @AllArgsConstructor
    public enum FrpcType {
        HTTP("http"),
        TCP("tcp"),
        UDP("udp");

        private final String type;

        public static FrpcType of(String type) {
            return Arrays.stream(values()).filter(i -> i.getType().equalsIgnoreCase(type)).findAny().orElse(TCP);
        }

    }

}
