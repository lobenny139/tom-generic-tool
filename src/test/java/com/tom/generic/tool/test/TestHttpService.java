package com.tom.generic.tool.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.tom.generic.tool.service.IHttpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.tom.generic.tool.TestApplication.class)
@TestPropertySource(locations = "/test-application.properties")
public class TestHttpService {

    @Autowired
    @Qualifier(value = "httpService")
    private IHttpService service;

    @Test
    public void testGet(){
        String url = "https://httpbin.org/get";
        Map<String, String> headers = new HashMap<>();
        headers.put("custom-key", "mkyong");
        headers.put(HttpHeaders.USER_AGENT, "Googlebot");
        System.out.println(service.sendSSHGET(url, null));
    }

    @Test
    public void  testGet2(){
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=台北市南港區園區街3-2號&key=AIzaSyDmyD5m22zwY2UBcbnZrloiCUG4sm3n3Hs&language=zh-TW";
        System.out.println(service.sendSSHGET(url, null));
    }

    @Test
    public void testPost2(){
        String url = "https://httpbin.org/post";
        Map<String, String> headers = new HashMap<>();
        headers.put("username", "abc");
        headers.put("password", "123");
        headers.put("custom", "secret");
        System.out.println(service.sendSSHPOST(url, null, headers));
    }

    @Test
    public void testPost(){
        String url = "https://www.post.gov.tw/post/streetNameData?city=臺北市&cityarea=內湖區";
        System.out.println(service.sendSSHPOST(url, null, null));
    }

    @Test
    public void testPost3() throws JsonProcessingException, UnsupportedEncodingException {
        String url = "https://httpbin.org/post";
        Map<String, String> body = new HashMap<>();
        body.put("name","羅智全");
        body.put("notes", "my notes");

        Map<String, String> headers = new HashMap<>();
        headers.put("username", "abc");
        headers.put("password", "123");
        headers.put("custom", "secret");
        headers.put("Content-Type","application/json;charset=utf-8");

        System.out.println(service.sendSSHPOST(url, new ObjectMapper().writeValueAsString(body), headers));

    }


}
