package org.jetlinks.edge.core.driver;

import reactor.core.Disposable;

/**
 * 声明周期定义
 *
 * @author zhouhao
 * @see 1.0
 */
public interface LifeCycle extends Disposable {

    /**
     * 启动
     */
    void start();

    /**
     * 暂停,要恢复则执行{@link this#start()}
     */
    void pause();
}
