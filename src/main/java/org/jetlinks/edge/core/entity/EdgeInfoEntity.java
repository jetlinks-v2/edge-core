package org.jetlinks.edge.core.entity;

/*
 * @author FCG
 */

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.jetlinks.core.device.DeviceInfo;
import org.jetlinks.core.device.DeviceStateInfo;
import reactor.core.publisher.Mono;

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
@Slf4j
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

    public static EdgeInfoEntity convertDeviceInfoToEdgeInfo(Object jsonObject) {

        JSONObject json = (JSONObject) jsonObject;
        EdgeInfoEntity edgeInfoEntity = new EdgeInfoEntity();

        // 可以写入更多数据
        edgeInfoEntity.setDeviceId(json.getString("id"));
        edgeInfoEntity.setProductId(json.getString("productId"));
        edgeInfoEntity.setConnected("online".equals(json.getJSONObject("state").getString("value")));

        return edgeInfoEntity;
    }

    /**
     * @author 仅允许修改的字段
     * @date 2021/7/8 14:22
     * @version 1.0
     */
    public Mono<EdgeInfoEntity> merge(EdgeInfoEntity entity) {
        this.setProductId(ObjectUtil.isNotEmpty(entity.getProductId()) ? entity.getProductId() : this.getProductId());
        this.setDeviceId(ObjectUtil.isNotEmpty(entity.getDeviceId()) ? entity.getDeviceId() : this.getDeviceId());
        this.setSecureId(ObjectUtil.isNotEmpty(entity.getSecureId()) ? entity.getSecureId() : this.getSecureId());
        this.setSecureKey(ObjectUtil.isNotEmpty(entity.getSecureKey()) ? entity.getSecureKey() : this.getSecureKey());
        this.setHost(ObjectUtil.isNotEmpty(entity.getHost()) ? entity.getHost() : this.getProductId());
        this.setPort(ObjectUtil.isNotEmpty(entity.getPort()) ? entity.getPort() : this.getPort());
        return Mono.just(this);
    }
}
