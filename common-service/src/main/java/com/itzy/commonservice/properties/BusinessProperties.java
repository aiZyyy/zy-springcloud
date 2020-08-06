package com.itzy.commonservice.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: ZY
 * @Date: 2019/8/2 15:43
 * @Version 1.0
 */
//@ConfigurationProperties(prefix = "itzy.business")
@Getter
@Setter
public class BusinessProperties {

    private FtpProperties ftp;
}
