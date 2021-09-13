package com.zebone.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.weixin.entity.WxPayConfigDO;
import com.zebone.weixin.repository.WxPayConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/wx/pay/config")
public class WxPayConfigController {

    @Autowired
    private WxPayConfigRepository repository;

    @GetMapping
    public String list(){
        return JSON.toJSONString(repository.findAll());
    }

    @PostMapping
    public void save(String param){
        WxPayConfigDO wxPayConfig = JSON.parseObject(param,WxPayConfigDO.class);
        repository.save(wxPayConfig);
    }

    @DeleteMapping
    public void delete(String param){
        WxPayConfigDO wxPayConfig = JSON.parseObject(param,WxPayConfigDO.class);
        repository.deleteById(wxPayConfig.getId());
    }
}
