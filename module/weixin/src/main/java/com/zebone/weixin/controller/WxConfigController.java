package com.zebone.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.weixin.entity.WxConfigDO;
import com.zebone.weixin.repository.WxConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wx/config")
public class WxConfigController {

    @Autowired
    private WxConfigRepository repository;

    @GetMapping
    public String list(String param){
        List<WxConfigDO> list = repository.findAll();
        return JSON.toJSONString(list);
    }

    @PostMapping
    public void save(String param){
        WxConfigDO wxConfig = JSON.parseObject(param,WxConfigDO.class);
        repository.save(wxConfig);
    }

    @DeleteMapping
    public void delete(String param){
        WxConfigDO wxConfig = JSON.parseObject(param,WxConfigDO.class);
        repository.deleteById(wxConfig.getId());

    }
}
