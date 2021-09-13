package com.zebone.alipay.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.alipay.entity.AliPayConfigDO;
import com.zebone.alipay.repository.AliPayConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ali/pay/config")
public class AlipayConfigController {

    @Autowired
    private AliPayConfigRepository repository;

    @GetMapping
    public String list(){
        return JSON.toJSONString(repository.findAll());
    }

    @PostMapping
    public void save(String param){
        AliPayConfigDO config = JSON.parseObject(param,AliPayConfigDO.class);
        repository.save(config);
    }

    @DeleteMapping
    public void delete(String param){
        AliPayConfigDO config = JSON.parseObject(param,AliPayConfigDO.class);
        repository.deleteById(config.getId());
    }
}
