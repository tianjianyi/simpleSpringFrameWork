package org.yifeng.aop.aspect;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.yifeng.aop.PointcutLocator;


@AllArgsConstructor
@Data
public class AspectInfo {
    private  int orderIndex;

    private DefaultAspect defaultAspect;

    private PointcutLocator pointcutLocator;

}
