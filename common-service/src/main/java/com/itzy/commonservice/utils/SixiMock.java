package com.itzy.commonservice.utils;

import com.alibaba.fastjson.JSON;
import com.itzy.commonservice.kits.BeanKit;
import com.itzy.commonservice.kits.DozerBeanKit;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created with IntelliJ IDEA
 * <strong>四喜微服务测试工具类.</strong>
 *
 * <h3>Example</h3>
 *
 * <pre class="code">
 * import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
 * import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 * import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
 *
 * // ...
 *
 * ApiRequest body = ApiRequest
 *                  .builder()
 *                  .path("param2/1/com.alibaba.trade/alibaba.trade.get.sellerView")
 *                  .memberId("鑫鑫花业")
 *                  .param(new KeyValue().add("webSite", "1688").add("orderId", "126363381696345022").toMap())
 *                  .build();
 *
 * SixiMock.mock()
 *         // 创建请求
 *         .post()
 *         .url("/ali")
 *         .body(body)
 *         // 匹配结果
 *         .matcher(status().isOk())
 *         .matcher(content().contentType(MediaType.APPLICATION_JSON_UTF8))
 *         .matcher(content().json("{ \"msg\": \"登录成功!\", \"status\": 200 }"))
 *         // 运行Test
 *         .runAndPrint();
 * </pre>
 *
 * @author 喵♂呜
 * Created on 2017/11/13 19:53.
 */
public class SixiMock {
    private static org.springframework.test.web.servlet.MockMvc mvc;

    static {
        mvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) BeanKit.getApplicationContext()).build();
    }

    public static SixiMockBuilder mock() {
        return new SixiMockBuilder();
    }

    public static class SixiMockBuilder {
        /**
         * 请求地址
         */
        private String url;
        /**
         * 请求方法
         */
        private HttpMethod method = HttpMethod.GET;
        /**
         * 请求Session
         */
        private MockHttpSession session = new MockHttpSession();
        /**
         * 请求Body
         */
        private Object body;

        /**
         * 结果匹配列表
         */
        private List<ResultMatcher> matchers;
        /**
         * 结果处理列表
         */
        private List<ResultHandler> handlers;

        public SixiMockBuilder() {
            matchers = new ArrayList<>();
            handlers = new ArrayList<>();
        }

        /**
         * 设置路径
         *
         * @param url
         *         路径
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 使用POST
         *
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder post() {
            return method(HttpMethod.POST);
        }

        /**
         * 使用GET
         *
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder get() {
            return method(HttpMethod.GET);
        }

        /**
         * 设置请求方式
         *
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        /**
         * 设置Session值
         * <p>
         * Session 对象
         *
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder session(Object object) {
            this.session(DozerBeanKit.toMap(object));
            return this;
        }

        /**
         * 设置Session值
         * <p>
         * Session 对象
         *
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder session(Map<String, Object> map) {
            map.forEach(this::session);
            return this;
        }

        /**
         * 设置Session值
         *
         * @param key
         *         键
         * @param value
         *         值
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder session(String key, Object value) {
            this.session.setAttribute(key, value);
            return this;
        }

        /**
         * 设置请求参数
         *
         * @param body
         *         请求参数
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder body(Object body) {
            this.body = body;
            return this;
        }

        /**
         * 添加结果匹配器
         *
         * @param matcher
         *         结果匹配器
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder matcher(ResultMatcher matcher) {
            matchers.add(matcher);
            return this;
        }


        /**
         * 添加结果处理器
         *
         * @param handler
         *         结果处理器
         * @return {@link SixiMockBuilder}
         */
        public SixiMockBuilder handler(ResultHandler handler) {
            handlers.add(handler);
            return this;
        }

        /**
         * 执行并打印
         *
         * @return {@link ResultActions}
         */
        @SneakyThrows
        public ResultActions runAndPrint() {
            return handler(print()).run();
        }

        /**
         * 执行
         *
         * @return {@link ResultActions}
         */
        public ResultActions run() {
            return run(null);
        }

        /**
         * 执行附加参数
         *
         * @return {@link ResultActions}
         */
        @SneakyThrows
        public ResultActions run(Consumer<MockHttpServletRequestBuilder> consumer) {
            // 初始化Builder
            MockHttpServletRequestBuilder builder = request(method, url, "json").session(session);
            // 设置请求Body
            Optional.ofNullable(body).ifPresent(p -> builder.content(JSON.toJSONString(body)));
            // 设置扩展参数
            Optional.ofNullable(consumer).ifPresent(c -> c.accept(builder));
            // 执行请求
            ResultActions result = mvc.perform(builder);
            // 处理匹配
            for (ResultMatcher matcher : matchers) {
                result.andExpect(matcher);
            }
            // 处理结果
            for (ResultHandler handler : handlers) {
                result.andDo(handler);
            }
            return result;
        }
    }
}
