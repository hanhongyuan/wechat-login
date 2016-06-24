package com.tmy.config;

import org.scribe.builder.ServiceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tmy.oauth.api.WeixinApi;
import com.tmy.oauth.service.CustomOAuthService;

@Configuration
public class OAuthConfig {
    
    private static final String CALLBACK_URL = "http://tianmaying.com/oauth/%s/callback";
    
    @Value("${oAuth.weixin.appId}") String weixinAppId;
    @Value("${oAuth.weixin.appSecret}") String weixinAppSecret;
    
    @Bean
    public CustomOAuthService getWeixinOAuthService(){
        return (CustomOAuthService) new ServiceBuilder()
            .provider(WeixinApi.class)
            .apiKey(weixinAppId)
            .apiSecret(weixinAppSecret)
            .scope("snsapi_login")
            .callback(String.format(CALLBACK_URL, OAuthTypes.WEIXIN))
            .build();
    }
    

}
