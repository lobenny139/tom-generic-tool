package com.tom.generic.tool.service.beanConfig;

import com.tom.generic.tool.service.IHttpService;
import com.tom.generic.tool.service.provider.HttpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    // ---------------------- 在這註冊你的資料表存取服務 -------------------

    @Bean(name="httpService")
    public IHttpService httpService(){
        return new HttpService();
    }

}
