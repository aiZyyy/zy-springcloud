package com.itzy.commonservice.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: ZY
 * @Date: 2019/8/2 15:44
 * @Version 1.0
 */
@Setter
@Getter
public class FtpProperties {
    private String ip;

    private String user;

    private String pwd;

    private String httpPrefix;
}
