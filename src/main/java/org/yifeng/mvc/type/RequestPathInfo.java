package org.yifeng.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPathInfo {
    private String httpMethod;

    private String httpPath;

}
