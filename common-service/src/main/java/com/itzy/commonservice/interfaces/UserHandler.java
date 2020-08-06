package com.itzy.commonservice.interfaces;

/**
 * Created with IntelliJ IDEA
 *
 */
public interface UserHandler extends LoginHandler {
    /**
     * 获得用户ID
     *
     * @return 用户ID
     */
    String getId();
}
