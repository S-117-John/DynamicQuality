package com.zebone.alipay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.zebone.alipay.entity.AliPayConfigDO;
import com.zebone.alipay.entity.AliPayRefundRecordDO;
import com.zebone.alipay.entity.AliPayTradeRecordDO;
import com.zebone.alipay.repository.AliPayConfigRepository;
import com.zebone.alipay.repository.AliPayRefundRecordRepository;
import com.zebone.alipay.repository.AliPayTradeRecordRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/alipay")
public class AliPayController {

    @Autowired
    private AliPayConfigRepository configRepository;

    @Autowired
    private AliPayTradeRecordRepository tradeRecordRepository;

    @Autowired
    private AliPayRefundRecordRepository refundRecordRepository;

    @PostMapping("query")
    public String query(@RequestBody String param) throws AlipayApiException {
        JSONObject root = JSON.parseObject(param);
        String appId = root.getString("app_id");
        AliPayConfigDO config = configRepository.findFirstByAppId(appId);
        String privateKey = config.getPrivateKey();
        String alipayPublicKey = config.getPayPublicKey();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey, "json", "GBK", alipayPublicKey, "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent(root.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            String result = JSON.toJSONString(response);
            AliPayTradeRecordDO tradeRecord = JSON.parseObject(result,AliPayTradeRecordDO.class);
            tradeRecordRepository.save(tradeRecord);
        } else {
            System.out.println("调用失败");
        }
        return JSON.toJSONString(response);
    }

    @PostMapping("precreate")
    public String precreate(@RequestBody String param) throws AlipayApiException {
        JSONObject root = JSON.parseObject(param);
        String appId = root.getString("app_id");
        AliPayConfigDO config = configRepository.findFirstByAppId(appId);
        String privateKey = config.getPrivateKey();
        String alipayPublicKey = config.getPayPublicKey();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey, "json", "GBK", alipayPublicKey, "RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("");
        request.setBizContent(root.toString());
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return JSON.toJSONString(response);
    }

    @PostMapping("close")
    public String close(@RequestBody String param) throws AlipayApiException {
        JSONObject root = JSON.parseObject(param);
        String appId = root.getString("app_id");
        AliPayConfigDO config = configRepository.findFirstByAppId(appId);
        String privateKey = config.getPrivateKey();
        String alipayPublicKey = config.getPayPublicKey();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey, "json", "GBK", alipayPublicKey, "RSA2");
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizContent(root.toString());
        AlipayTradeCloseResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return JSON.toJSONString(response);
    }

    @PostMapping("refund")
    public String refund(@RequestBody String param) throws AlipayApiException {
        JSONObject root = JSON.parseObject(param);
        String appId = root.getString("app_id");
        AliPayConfigDO config = configRepository.findFirstByAppId(appId);
        String privateKey = config.getPrivateKey();
        String alipayPublicKey = config.getPayPublicKey();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey, "json", "GBK", alipayPublicKey, "RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(root.toString());
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            String result = JSON.toJSONString(response);
            AliPayRefundRecordDO refundRecord = JSON.parseObject(result,AliPayRefundRecordDO.class);
            refundRecordRepository.save(refundRecord);

        } else {
            System.out.println("调用失败");
        }
        return JSON.toJSONString(response);
    }

    @PostMapping("downloadurl")
    public String bill(@RequestBody String param) throws AlipayApiException {
        JSONObject root = JSON.parseObject(param);
        String appId = root.getString("app_id");
        AliPayConfigDO config = configRepository.findFirstByAppId(appId);
        String privateKey = config.getPrivateKey();
        String alipayPublicKey = config.getPayPublicKey();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey, "json", "GBK", alipayPublicKey, "RSA2");
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        request.setBizContent(root.toString());
        AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return JSON.toJSONString(response);
    }

    @GetMapping("data")
    public String data(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        JSONObject object = new JSONObject();
        JSONArray alipayList = new JSONArray();
        List<AliPayTradeRecordDO> list = tradeRecordRepository.findBySendPayDateIsGreaterThan(zero);
        long tradeAmount = list.stream().map(a->a.getTotalAmount()).count();
        object.put("trade",tradeAmount);

        JSONObject aliPayTrade = new JSONObject();
        double aliPayTradeSum = list.stream().mapToDouble(a->a.getTotalAmount()).sum();
        aliPayTrade.put("count",aliPayTradeSum);
        aliPayTrade.put("name","支付");
        alipayList.add(aliPayTrade);

        List<AliPayRefundRecordDO> refundList = refundRecordRepository.findByGmtRefundPayIsGreaterThan(zero);
        long refundAmount = refundList.stream().map(a->a.getId()).count();
        object.put("refund",refundAmount);
        JSONObject aliPayRefund = new JSONObject();
        double aliPayRefundSum = refundList.stream().mapToDouble(a->a.getRefundFee()).sum();
        aliPayRefund.put("count",aliPayRefundSum);
        aliPayRefund.put("name","退款");
        alipayList.add(aliPayRefund);

        object.put("total",alipayList);

        return JSON.toJSONString(object);
    }

}
