package com.itzy.commonservice.interfaces;


public interface PermissionHandler {
    /**
     * 判断用户权限
     *
     * @param permission
     *         权限字符串
     * @return 返回用户是否有权限
     */
    boolean hasPermission(String permission);
}
