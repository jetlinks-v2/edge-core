package org.jetlinks.edge.core.monitor;

import java.lang.annotation.*;

/**
 * 支持降低峰值时可关闭的方法
 *
 * @author gyl
 * @since 2.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PeakReduction {
}
