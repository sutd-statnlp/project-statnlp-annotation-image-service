package com.sutd.statnlp.annotationimage.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Object LOGIN_URL = "https://dev-156902.oktapreview.com/oauth2/v1/authorize?idp=0oae3ckfgccYu2dkZ0h7&client_id=0oae3cm9c6dztVMPJ0h7&response_type=code&response_mode=fragment&scope=openid&redirect_uri=http://localhost:8082/&state=WM6D&nonce=YsG76jo";

    @GetMapping("/login")
    public String getLogin(Model model) throws Exception {
        model.addAttribute("loginUrl",LOGIN_URL);
        return "login";
    }
}
