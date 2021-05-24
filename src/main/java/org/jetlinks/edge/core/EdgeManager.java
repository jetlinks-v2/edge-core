package org.jetlinks.edge.core;

import org.jetlinks.edge.core.driver.Driver;
import org.jetlinks.edge.core.monitor.EdgeMonitor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 边缘管理器,用于管理支持的驱动等信息
 *
 * @author zhouhao
 * @since 1.0
 */
public interface EdgeManager {

    /**
     * 根据ID获取驱动,根据不同的驱动类型进行不同的处理
     *
     * @param id ID
     * @return 驱动
     * @see Driver
     */
    Mono<Driver> getDriver(String id);

    /**
     * 获取全部驱动
     *
     * @return 驱动信息
     */
    Flux<Driver> getDrivers();
}
