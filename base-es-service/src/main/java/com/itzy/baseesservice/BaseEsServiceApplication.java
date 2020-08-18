package com.itzy.baseesservice;

import com.itzy.commonservice.annotation.ZyMicroServiceApplication;
import org.springframework.boot.SpringApplication;

/**
 * @author ZY
 * @date 2020/8/18 14:46
 * @Description:
 * @Version 1.0
 */
@ZyMicroServiceApplication
public class BaseEsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseEsServiceApplication.class, args);
    }
}
