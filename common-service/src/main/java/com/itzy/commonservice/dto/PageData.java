package com.itzy.commonservice.dto;

import com.itzy.commonservice.utils.AssertUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageData<T> {
    private Integer num;
    private Integer size;
    private Long count;
    private List<T> list;

    /**
     * PageHelper 快捷封装
     *
     * @param list
     *         数据
     * @param <T>
     *         实体泛型
     * @return {@link PageData}
     */
    public static <T> PageData<T> page(List<T> list) {
        AssertUtil.require(list instanceof com.github.pagehelper.Page, "分页结果无法转换为Page对象 请检查是否为Mapper返回的原始对象(服务器内部错误)");
        return page(list, (com.github.pagehelper.Page<T>) list);
    }

    /**
     * SpringData 快捷封装
     *
     * @param page
     *         分页数据
     * @param <T>
     *         实体泛型
     * @return {@link PageData}
     */
    public static <T> PageData<T> page(org.springframework.data.domain.Page<T> page) {
        return page(page.getContent(), page);
    }

    /**
     * PageHelper 分页参数
     *
     * @param list
     *         数据集合
     * @param page
     *         分页参数
     * @param <T>
     *         实体泛型
     * @return {@link PageData}
     */
    public static <T> PageData<T> page(List<T> list, com.github.pagehelper.Page<T> page) {
        return page(list, page.getTotal(), page.getPageSize(), page.getPageSize());
    }

    /**
     * SpringData 分页参数
     *
     * @param list
     *         数据集合
     * @param page
     *         分页参数
     * @param <T>
     *         实体泛型
     * @return {@link PageData}
     */
    public static <T> PageData<T> page(List<T> list, org.springframework.data.domain.Page<T> page) {
        return page(list, page.getTotalElements(), page.getSize(), page.getNumber());
    }

    public static <T> PageData<T> page(List<T> list, long count, int size, int num) {
        AssertUtil.requireNonNull(list, "分页结果不得为Null(服务器内部错误)");
        return PageData.<T>builder().list(list).count(count).size(size).num(num).build();
    }
}
