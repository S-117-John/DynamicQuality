package com.zebone.alipay.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.alipay.entity.AliPayTradeRecordDO;
import com.zebone.alipay.repository.AliPayTradeRecordRepository;
import com.zebone.alipay.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/alipay/trade/record")
public class AliPayTradeRecordController {

    @Autowired
    private AliPayTradeRecordRepository repository;

    @GetMapping
    public String list(){
        List<AliPayTradeRecordDO> list = repository.findAll();
        ResponseVO response = new ResponseVO();
        response.setData(list);
        response.setSuccess(true);
        response.setTotal(list.size());
        return JSON.toJSONString(response);
    }
}
