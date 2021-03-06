package com.itzy.springclouddemo.domain;

import java.io.Serializable;

/**
 * @Author: ZY
 * @Date: 2019/8/2 18:10
 * @Version 1.0
 */

public class User implements Serializable {
    private Integer id;

    private String name;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
