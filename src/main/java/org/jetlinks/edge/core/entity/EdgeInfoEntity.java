package org.jetlinks.edge.core.entity;

/*
 * @author FCG
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hswebframework.web.api.crud.entity.GenericEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 网关硬件信息
 *
 * @author FCG
 */
@Getter
@Setter
@Table(name = "dev_edge_info")
public class EdgeInfoEntity extends GenericEntity<String> {

    @Schema(description = "网关名称")
    @Column(nullable = false)
    private String edgeName;

    @Schema(description = "固件版本")
    @Column(nullable = false)
    private String firmwareVer;

    @Schema(description = "操作系统")
    @Column(nullable = false)
    private String osInfo;

    @Schema(description = "网关地理位置信息")
    @Column()
    private String geoAdder;

    @Schema(description = "网关在平台的产品id")
    @Column(nullable = false)
    private String productId;

    @Schema(description = "网关设备的id")
    @Column(nullable = false)
    private String deviceId;

    @Schema(description = "网关secureId")
    @Column(nullable = false)
    private String secureId;

    @Schema(description = "网关secureKey")
    @Column(nullable = false)
    private String secureKey;

    @Schema(description = "平台地址")
    @Column(nullable = false)
    private String host;

    @Schema(description = "平台端口")
    @Column(nullable = false)
    private int port;

    @Schema(description = "是否连接平台")
    @Column()
    private boolean connected;
}
