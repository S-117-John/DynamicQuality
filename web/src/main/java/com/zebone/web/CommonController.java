package com.zebone.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/common")
public class CommonController {

    @Value("${system.title}")
    private String title;

    @GetMapping("title")
    public String getTielt(){
        return title;
    }
}
