package org.jetlinks.edge.core.driver;

import org.jetlinks.core.metadata.ConfigMetadata;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * 驱动提供商接口,实现此接口,并注入到spring.
 * 实现对驱动的拓展.平台侧可以根据支持的提供商来创建和使用对应的驱动.
 *
 * @author zhouhao
 * @since 1.0
 */
public interface DriverProvider {

    /**
     * @return 服务商ID
     */
    String getId();

    /**
     * @return 服务商名称
     */
    String getName();

    /**
     * @return 此驱动服务商需要的配置定义信息
     * @see org.jetlinks.core.metadata.DefaultConfigMetadata
     */
    @Nullable
    ConfigMetadata getConfigMetadata();

    /**
     * 使用配置创建驱动,创建时应当校验配置是否合法.如果不合法,应当抛出异常.
     *
     * @param config 配置信息
     * @return 驱动
     */
    @Nonnull
    Mono<Driver> createDriver(String id, Map<String, Object> config);

    /**
     * 重新加载驱动配置,用于不停机更新配置等操作.
     *
     * @param driver 驱动实例
     * @param config 配置信息
     * @return 加载后的驱动
     */
    @Nonnull
    Mono<Driver> reload(Driver driver, Map<String, Object> config);
}
