package org.jetlinks.edge.core.monitor;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import org.jetlinks.pro.dashboard.measurements.SystemMonitor;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import java.util.List;

/**
 * 网关硬件探针
 *
 * @author FCG
 */
@Data
public class EdgeMonitorProbe {
    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * <li>CPU核心数</li>
     * <li>CPU总的使用率</li>
     * <li>CPU核心数</li>
     * <li>CPU系统使用率</li>
     * <li>CPU用户使用率</li>
     * <li>CPU当前等待率</li>
     * <li>CPU当前空闲率</li>
     */
    private int cpuNum;
    private double totalUsedCpu;
    private double sysCpu;
    private double usedCpu;
    private double waitCpu;
    private double freeCpu;
    private double cpuTemperature;
    /**
     * <li>内存总量</li>
     * <li>已用内存</li>
     * <li>剩余内存</li>
     */
    private double totalMem;
    private double usedMem;
    private double freeMem;

    private double jvmMem;

    /**
     * <li>盘符路径</li>
     * <li>盘符类型</li>
     * <li>文件类型</li>
     * <li>总大小</li>
     * <li>剩余大小</li>
     * <li>已经使用量</li>
     * <li>资源的使用率</li>
     */
    //保存的是字节
    private String sysDirName;
    private String sysTypeName;
    private String sysFileTypeName;
    private long sysTotal;
    private long sysFree;
    private long sysUsed;
    private double sysUsage;

    private SystemInfo systemInfo;

    public EdgeMonitorProbe(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        this.setCpuInfo(hardware.getProcessor());
        this.setMemInfo(hardware.getMemory());
        this.setIpAddr(NetUtil.getLocalhostStr());
        this.setSysFiles(systemInfo.getOperatingSystem());
        this.setCpuTemperature(
            systemInfo.getHardware()
                      .getSensors()
                      .getCpuTemperature()
        );
    }

    /**
     * 设置CPU信息。只计算CPU负载信息。
     *
     * @param processor 抽象CPU硬件
     * @see CentralProcessor#getSystemCpuLoadTicks()
     * @see CentralProcessor#getSystemCpuLoadBetweenTicks(long[])
     */
    private void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        /*long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        this.setCpuNum(processor.getLogicalProcessorCount());
        this.setTotalCpu(totalCpu);
        this.setSysCpu(cSys);
        this.setUsedCpu(user);
        this.setWaitCpu(iowait);
        this.setFreeCpu(idle);*/
        this.setTotalUsedCpu(SystemMonitor.systemCpuUsage.getValue());

    }

    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory) {
        this.setTotalMem(memory.getTotal());
        this.setUsedMem(memory.getTotal() - memory.getAvailable());
        this.setFreeMem(memory.getAvailable());
    }

    /**
     * 设置磁盘信息
     */
    private void setSysFiles(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            String mount = fs.getMount();
            //只获取根目录或者C盘
            if ("C:\\".equals(mount) || "/".equals(mount)) {
                long free = fs.getUsableSpace();
                long total = fs.getTotalSpace();
                long used = total - free;

                this.setSysDirName(fs.getMount());
                this.setSysTypeName(fs.getType());
                this.setSysFileTypeName(fs.getName());
                this.setSysTotal(total);
                this.setSysFree(free);
                this.setSysUsed(used);
                this.setSysUsage(NumberUtil.round(NumberUtil.mul(used, total, 4), 100).doubleValue());
            }
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    private static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

}

