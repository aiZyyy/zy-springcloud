package com.itzy.commonservice.utils;



import com.itzy.commonservice.exception.ForbiddenException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA
 * 断言判断工具类
 */
public class AssertUtil<T> {
    private T t;

    public AssertUtil(T t) {
        this.t = t;
    }

    /**
     * 当对象不为Null时运行
     *
     * @param consumer
     */
    public void runNonNull(Consumer<? super T> consumer) {
        Optional.ofNullable(t).ifPresent(consumer);
    }

    /**
     * 当一个集合不为空的时候执行
     *
     * @param consumer
     *         执行器
     */
    public void runNonEmpty(Consumer<? super T> consumer) {
        if (Objects.nonNull(t)) {
            AssertUtil.require(t instanceof Collection, "对象必须为集合才能使用 runNonEmpty");
            runIsTrue(!((Collection) t).isEmpty(), () -> consumer.accept(t));
        }
    }

    /**
     * 当对象为Null的时候执行
     *
     * @param consumer
     *         执行器
     */
    public void runIsNull(Consumer<? super T> consumer) {
        runIsTrue(Objects.isNull(t), () -> consumer.accept(t));
    }

    /**
     * 当对象是clazz的子类时执行
     *
     * @param clazz
     *         需要判断的类
     * @param consumer
     *         执行
     */
    @SuppressWarnings("unchecked")
    public void runIsAssignable(Class clazz, Consumer<? super T> consumer) {
        runIsTrue(Objects.nonNull(t) && clazz.isAssignableFrom(t.getClass()), () -> consumer.accept(t));
    }

    /**
     * 包装对象
     *
     * @param value
     *         对象
     * @param <T>
     *         泛型
     * @return 包装后的Assert
     */
    public static <T> AssertUtil<T> of(T value) {
        return new AssertUtil<>(value);
    }

    /**
     * @param bool
     *         结果
     * @param runnable
     *         正确时运行
     */
    public static void runIsTrue(Boolean bool, Runnable runnable) {
        if (bool) {
            runnable.run();
        }
    }

    /**
     * 根据结果进行运行
     *
     * @param bool
     *         结果
     * @param t
     *         True时运行
     * @param f
     *         False时运行
     */
    public static void runOrElse(Boolean bool, Runnable t, Runnable f) {
        if (bool) {
            t.run();
        } else {
            f.run();
        }
    }

    /**
     * 当对象为 Null 时抛出异常
     *
     * @param object
     *         对象
     * @param message
     *         异常消息
     */
    public static <T> T requireNonNull(T object, String message) {
        if (Objects.isNull(object)) {
            throw new ForbiddenException(message);
        }
        return object;
    }

    /**
     * 当集合为空时抛出异常
     *
     * @param collection
     *         集合
     * @param message
     *         异常消息
     */
    public static void requireNotEmpty(Collection collection, String message) {
        if (Objects.isNull(collection) || collection.isEmpty()) {
            throw new ForbiddenException(message);
        }
    }

    /**
     * 当字符串为空时抛出异常
     *
     * @param string
     *         字符串
     * @param message
     *         异常消息
     */
    public static String requireNotBlank(String string, String message) {
        if (Objects.isNull(string) || string.isEmpty()) {
            throw new ForbiddenException(message);
        }
        return string;
    }

    /**
     * 当结果为False时抛出异常
     *
     * @param result
     *         获取返回值
     * @param message
     *         异常消息
     */
    public static void require(boolean result, String message) {
        if (!result) {
            throw new ForbiddenException(message);
        }
    }

    /**
     * 当结果为True时抛出异常
     *
     * @param result
     *         获取返回值
     * @param message
     *         异常消息
     */
    public static void forbidden(boolean result, String message) {
        if (result) {
            throw new ForbiddenException(message);
        }
    }

    /**
     * 抛出异常
     *
     * @param result
     *         获取返回值
     * @param message
     *         异常消息
     * @param errCode
     *         错误代码
     */
    public static void forbidden(boolean result, String message, Integer errCode) {
        if (result) {
            throw new ForbiddenException(message, errCode);
        }
    }

    /**
     * 当结果为False时抛出异常
     *
     * @param result
     *         获取返回值
     * @param message
     *         异常消息
     * @deprecated 简化语义 <br/>
     * 请使用 {@link AssertUtil#forbidden(boolean, String)}
     */
    @Deprecated
    public static void requireTrue(boolean result, String message) {
        require(result, message);
    }

    /**
     * 抛出异常
     *
     * @param result
     *         获取返回值
     * @param message
     *         异常消息
     * @deprecated 简化语义 <br/>
     * 请使用 {@link AssertUtil#forbidden(boolean, String)}
     */
    @Deprecated
    public static void isForbidden(boolean result, String message) {
        forbidden(result, message);
    }

    /**
     * 抛出异常
     *
     * @param result
     *         获取返回值
     * @param message
     *         异常消息
     * @deprecated 简化语义 <br/>
     * 请使用 {@link AssertUtil#forbidden(boolean, String, Integer)}
     */
    @Deprecated
    public static void isForbidden(boolean result, String message, Integer errCode) {
        forbidden(result, message, errCode);
    }
}
