package com.itzy.commonservice.kits;//package com.itzy.commonservice.kits;
//
//import com.alibaba.fastjson.JSONObject;
//import com.google.common.net.MediaType;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import lombok.val;
//import okhttp3.*;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.Optional;
//
///**
// * Created with IntelliJ IDEA
// *
// * @author MiaoWoo
// * Created on 2018/12/29 15:38.
// */
//@Slf4j
//@Configuration("defaultRequestKit")
//@ConditionalOnBean(OkHttpClient.class)
//public class RequestKit {
//    public static MediaType JSON = MediaType.parse("application/json; charset=UTF-8");
//    private final kHttpClient client;
//
//    public RequestKit(OkHttpClient client) {
//        this.client = client;
//    }
//
//    @SneakyThrows
//    public Response execute(Request request) {
//        return client.newCall(request).execute();
//    }
//
//    public void redirect(String url) {
//        WebKit.redirect(execute(new Request.Builder().url(url).get().build()).header("Location"));
//    }
//
//    @SneakyThrows
//    public String get(String url) {
//        Response response = execute(new Request.Builder().url(url).build());
//        return Optional.ofNullable(response.body()).orElse(ResponseBody.create(JSON, "")).string();
//    }
//
//    public String postJson(String path, Object object) {
//        return postJson(path, JSONObject.toJSONString(object));
//    }
//
//    @SneakyThrows
//    public String postJson(String url, String json) {
//        val response = execute(new Request.Builder().url(url).post(RequestBody.create(JSON, json)).build());
//        val result = Optional.ofNullable(response.body()).orElse(ResponseBody.create(JSON, "{}")).string();
//        log.debug("\n====================[RequestKit]====================" +
//                  "\nURL: {}\nBody: {}\nResult: {}" +
//                  "\n====================[RequestKit]====================",
//                  url, json, result);
//        return result;
//    }
//}
