package com.itzy.commonservice.kits;

import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA
 *
 * @author 喵♂呜
 * Created on 2017/6/14 19:12.
 */
public class SessionKit {
    private SessionKit() {
    }

    /**
     * 获取Session属性
     *
     * @param key
     *         键
     * @param <T>
     *         泛型
     * @return 属性值
     */
    public static <T> T getSessionAttr(String key) {
        return (T) getSession().getAttribute(key);
    }

    /**
     * 获取Session属性
     *
     * @param key
     *         键
     * @param value
     *         值
     * @return 属性值
     */
    public static void setSessionAttr(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 获取Session属性
     *
     * @param key
     *         键
     * @return 属性值
     */
    public static void clearSessionAttr(String key) {
        setSessionAttr(key, null);
    }

    public static HttpSession getSession() {
        return WebKit.getRequest().getSession();
    }
}
