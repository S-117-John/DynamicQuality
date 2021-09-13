package com.zebone.alipay.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.alipay.entity.AliPayRefundRecordDO;
import com.zebone.alipay.repository.AliPayRefundRecordRepository;
import com.zebone.alipay.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/alipay/refund/record")
public class AliPayRefundRecordController {

    @Autowired
    private AliPayRefundRecordRepository refundRecordRepository;

    @GetMapping
    public String list(){
        List<AliPayRefundRecordDO> list = refundRecordRepository.findAll();
        ResponseVO response = new ResponseVO();
        response.setTotal(list.size());
        response.setSuccess(true);
        response.setData(list);
        return JSON.toJSONString(response);
    }
}
