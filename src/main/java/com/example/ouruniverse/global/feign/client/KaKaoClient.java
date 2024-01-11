package com.example.ouruniverse.global.feign.client;

import com.example.ouruniverse.domain.auth.controller.dto.KaKaoInfo;
import com.example.ouruniverse.domain.auth.controller.dto.KaKaoToken;
import com.example.ouruniverse.global.feign.config.KaKaoFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@FeignClient(name = "KaKaoClient", configuration = KaKaoFeignConfig.class)
public interface KaKaoClient {

    @PostMapping
    KaKaoInfo getInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

    @PostMapping
    KaKaoToken getToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
                        @RequestParam("redirect_uri") String redirectUrl,
                        @RequestParam("code") String code,
                        @RequestParam("grant_type") String grantType);

}
