package org.jetlinks.edge.core.driver;

import reactor.core.Disposable;

/**
 * 边缘驱动接口，具体请查看不同类型的驱动接口
 *
 * @author zhouhao
 * @see org.jetlinks.edge.core.driver.function.FunctionDriver
 * @since 1.0
 */
public interface Driver extends Disposable {

    /**
     * 驱动ID,如某个功能的ID定义
     *
     * @return 驱动ID
     */
    String getId();

    /**
     * 驱动名称,对此驱动对描述
     *
     * @return 驱动名称
     */
    String getName();

    /**
     * 将当前驱动转为指定的类型
     *
     * @param target 驱动类
     * @param <T>    驱动类型
     * @return 指定类型的驱动
     * @see org.jetlinks.edge.core.driver.function.FunctionDriver
     */
    default <T extends Driver> T unwrap(Class<T> target) {
        return target.cast(this);
    }

    /**
     * 判读当前驱动是否为指定类型
     *
     * @param target 指定驱动类型
     * @param <T>    驱动类型
     * @return 是否为指定的驱动类型
     * @see org.jetlinks.edge.core.driver.function.FunctionDriver
     */
    default <T extends Driver> boolean is(Class<T> target) {
        return target.isInstance(this);
    }
}
