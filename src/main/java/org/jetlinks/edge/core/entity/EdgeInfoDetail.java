package org.jetlinks.edge.core.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

import javax.persistence.Column;

/**
 * 网关硬件信息
 *
 * @author FCG
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EdgeInfoDetail {

    @Schema(description = "网关名称")
    private String edgeName;

    @Schema(description = "固件版本")
    private String firmwareVer;

//    @Schema(description = "操作系统")
//    private String osInfo;
//
//    @Schema(description = "网关地理位置信息")
//    private String geoAdder;

    @Schema(description = "网关在平台的产品id")
    private String productId;

    @Schema(description = "网关设备的id")
    private String deviceId;

    @Schema(description = "网关secureId")
    private String secureId;

    @Schema(description = "网关secureKey")
    private String secureKey;

    @Schema(description = "平台地址")
    private String host;

    @Schema(description = "平台端口")
    private int port;

    @Schema(description = "穿透端口")
    private int penetrationPort;

    @Schema(description = "穿透token")
    private String penetrationToken;

    @Schema(description = "离线缓存")
    private boolean offlineCache;

    @Schema(description = "是否连接平台")
    private boolean connected;
}
