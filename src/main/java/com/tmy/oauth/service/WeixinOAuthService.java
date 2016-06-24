package com.tmy.oauth.service;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.tmy.config.OAuthTypes;
import com.tmy.model.OAuthUser;
import com.tmy.model.User;

public class WeixinOAuthService extends OAuth20ServiceImpl implements CustomOAuthService {
    
    private final DefaultApi20 api;
    private final OAuthConfig config;
    private final String authorizationUrl;
    
    public WeixinOAuthService(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        this.api = api;
        this.config = config;
        this.authorizationUrl = getAuthorizationUrl(null);
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier){
      OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
      request.addQuerystringParameter("appid", config.getApiKey());
      request.addQuerystringParameter("secret", config.getApiSecret());
      request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
      if(config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
      Response response = request.send();
      String responceBody = response.getBody();
      Object result = JSON.parse(responceBody);
      return new Token(JSONPath.eval(result, "$.access_token").toString(), "", responceBody);
    }

    @Override
    public OAuthUser getOAuthUser(Token accessToken) {
        OAuthUser oAuthUser = new OAuthUser();
        oAuthUser.setoAuthType(getoAuthType());
        Object result = JSON.parse(accessToken.getRawResponse());
        oAuthUser.setoAuthId(JSONPath.eval(result, "$.openid").toString());
        oAuthUser.setUser(new User());
        return oAuthUser;
    }

    @Override
    public String getoAuthType() {
        return OAuthTypes.WEIXIN;
    }

    @Override
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

}
