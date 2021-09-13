package com.zebone.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.weixin.config.MyWxConfig;
import com.zebone.weixin.entity.WxPayBillDO;
import com.zebone.weixin.entity.WxPayConfigDO;
import com.zebone.weixin.entity.WxTradeRecordDO;
import com.zebone.weixin.repository.WxPayBillRepository;
import com.zebone.weixin.repository.WxPayConfigRepository;
import com.zebone.weixin.sdk.WXPay;
import com.zebone.weixin.vo.ResponseVO;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("api/wx/pay/bill")
public class WxPayBillController {

    @Autowired
    private WxPayConfigRepository payConfigRepository;

    @Autowired
    private WxPayBillRepository repository;

    @GetMapping
    public String list(){
        List<WxPayBillDO> list = repository.findAll();
        ResponseVO response = new ResponseVO();
        response.setData(list);
        response.setSuccess(true);
        response.setTotal(list.size());
        return JSON.toJSONString(response);
    }

    @GetMapping("downloadbill")
    public void downloadbill(String billDate) throws Exception {
        billDate = billDate.replace("-", "");
        WxPayConfigDO wxPayConfig = payConfigRepository
                .findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new Exception("未获取微信配置信息"));
        MyWxConfig myWxConfig = new MyWxConfig();
        myWxConfig.setApiKey(wxPayConfig.getPaySignKey());
        myWxConfig.setAppId(wxPayConfig.getAppId());
        myWxConfig.setMchId(wxPayConfig.getMchId());
        WXPay wxPay = new WXPay(myWxConfig);
        Map<String, String> data = new HashMap<>();
        data.put("bill_date", billDate);
        data.put("bill_type", "SUCCESS");
        Map<String, String> billResult = wxPay.downloadBill(data);
        String result = MapUtils.getString(billResult, "data");
        String tradeMsg = result.substring(result.indexOf("`"));
        Date date = DateUtils.parseDate(billDate, "yyyyMMdd");
        String tradeInfo = tradeMsg.substring(0, tradeMsg.indexOf("总交易单数")).replaceAll("`", "");// 去掉汇总数据
        String lineSeparator = System.getProperty("line.separator", "\n");
        String[] tradeArray = tradeInfo.split(lineSeparator);
        List<WxPayBillDO> recordList = new ArrayList<>();
        for (String s : tradeArray) {
            if(ObjectUtils.isEmpty(s)){
                continue;
            }
            WxPayBillDO billDO = new WxPayBillDO();
            billDO.setTradeDate(DateUtils.parseDate(s.split(",")[0],"yyyy-MM-dd HH:mm:ss"));
            billDO.setTransactionId(s.split(",")[5]);
            billDO.setOutTradeNo(s.split(",")[6]);
            billDO.setTradeType(s.split(",")[8]);
            billDO.setTradeState(s.split(",")[9]);
            billDO.setTotalFee(s.split(",")[12]);
            billDO.setBody(s.split(",")[14]);
            recordList.add(billDO);
        }
        repository.deleteAll();
        repository.saveAll(recordList);
    }

    public static void main(String[] args) {
        String lineSeparator = System.getProperty("line.separator", "\n");
        System.out.println("1"+lineSeparator+"1");
    }
}
