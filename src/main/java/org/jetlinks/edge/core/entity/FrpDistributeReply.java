package org.jetlinks.edge.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 输入描述.
 *
 * @author zhangji 2023/1/28
 */
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class FrpDistributeReply {

    private String url;

    private String token;
}
