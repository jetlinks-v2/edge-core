package org.jetlinks.edge.core.monitor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hswebframework.web.bean.FastBeanCopier;
import org.hswebframework.web.exception.NotFoundException;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@ToString
public class EdgeRunningState {

    //百分比数值
    private double cpuUsage;

    private double jvmMemUsage;

    private double sysMemUsage;

    private double diskUsage;

    // TODO: 2021/2/25 更多运行状态信息


    @Getter
    @Setter
    public static class FormatEdgeRunningState {

        private PropertyInfo cpuUsage;

        private PropertyInfo jvmMemUsage;

        private PropertyInfo sysMemUsage;

        private PropertyInfo diskUsage;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PropertyInfo {

        private String formatValue;

        private Object numberValue;

        private String propertyName;
    }

    private static DecimalFormat df = new DecimalFormat("0.00%");

    public FormatEdgeRunningState toFormatEdgeRunningState() {
        FormatEdgeRunningState state = new FormatEdgeRunningState();
        state.setCpuUsage(new PropertyInfo(getPercentFormat(this.getCpuUsage()), this.getCpuUsage(), "cpu使用率"));
        state.setDiskUsage(new PropertyInfo(getPercentFormat(this.getDiskUsage()), this.getDiskUsage(), "磁盘使用率"));
        state.setSysMemUsage(new PropertyInfo(getPercentFormat(this.getSysMemUsage()), this.getSysMemUsage(), "系统内存使用率"));
        state.setJvmMemUsage(new PropertyInfo(getPercentFormat(this.getJvmMemUsage()), this.getJvmMemUsage(), "JVM内存使用率"));
        return state;
    }

    private static String getPercentFormat(double value) {
        return df.format(value / 100);
    }


    public Object getPropertyValue(String propertyName) {
        FormatEdgeRunningState state = this.toFormatEdgeRunningState();
        Map<String, Object> mapData = FastBeanCopier.copy(state, new HashMap<>());
        return Optional.ofNullable(mapData.get(propertyName))
                       .orElseThrow(() -> new NotFoundException("网关属性" + propertyName + "不存在"));
    }
}
