package com.zebone.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("wx")
public class TestController {

    @GetMapping("/sign")
    public void sign(HttpServletRequest request){

        System.out.println("");

    }
}
