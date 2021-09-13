package com.zebone.quality.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zebone.quality.entity.QualityConfigDO;
import com.zebone.quality.repository.QualityConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/quality/patient")
public class PatientController {

    @Autowired
    private QualityConfigRepository configRepository;

    @GetMapping("config")
    public String config(String param){
        JSONObject root = JSON.parseObject(param);
        String formCode = root.getString("formCode");
        QualityConfigDO config = configRepository.findByCode(formCode);
        return JSON.toJSONString(config);
    }


}
