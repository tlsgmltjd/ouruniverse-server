package com.example.ouruniverse.domain.auth.controller;

import com.example.ouruniverse.domain.auth.controller.dto.KaKaoAccount;
import com.example.ouruniverse.domain.auth.service.KaKaoAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KaKaoAuthController {

    private final KaKaoAuthService kaKaoAuthService;

    @GetMapping("/callback")
    public KaKaoAccount getKakaoAccount(@RequestParam("code") String code) {
      log.debug("code = {}", code);
      return kaKaoAuthService.getInfo(code).getKaKaoAccount();
    }

}
