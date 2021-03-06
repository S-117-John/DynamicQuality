package com.zebone.alipay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.opencsv.CSVReader;
import com.zebone.alipay.entity.AliPayBillDO;
import com.zebone.alipay.entity.AliPayConfigDO;
import com.zebone.alipay.repository.AliPayBillRepository;
import com.zebone.alipay.repository.AliPayConfigRepository;
import com.zebone.alipay.utils.BillUtil;
import com.zebone.alipay.vo.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/alipay/bill")
public class AliPayBillController {

    @Autowired
    private AliPayConfigRepository configRepository;

    @Autowired
    private AliPayBillRepository repository;


    @GetMapping
    public String list(HttpServletRequest request){
        List<AliPayBillDO> list = repository.findAll();
        ResponseVO response = new ResponseVO();
        response.setData(list);
        response.setTotal(list.size());
        response.setSuccess(true);
        return JSON.toJSONString(response);
    }



    @GetMapping("day")
    public String day(HttpServletRequest servletRequest) throws Exception {
        repository.deleteAll();
        String billDate = servletRequest.getParameter("billDate");
        AliPayConfigDO config = configRepository.findAll().stream().findFirst().orElseThrow(()->new RuntimeException("????????????????????????"));
        String privateKey = config.getPrivateKey();
        String alipayPublicKey = config.getPayPublicKey();
        String appId = config.getAppId();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey, "json", "GBK", alipayPublicKey, "RSA2");
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        JSONObject root = new JSONObject();
        root.put("bill_type","trade");
        root.put("bill_date",billDate);
        request.setBizContent(root.toString());
        AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            String url = response.getBillDownloadUrl();

            List<String> fileNames = BillUtil.downloadBill(url);
            System.out.println(fileNames);
            fileNames.forEach(a->{
                try{
                    File csv = new File(a);
                    CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csv), "GBK"));
                    String[] header = reader.readNext();   //???readnext???????????????????????????stream??????
                    for (String s : header) {
                        System.out.print(s + ",");
                    }
                    List<String[]> list = reader.readAll(); //????????????????????????????????????
                    if (a.contains("??????")) {

                    }else {
                        for (int i = 4; i < list.size() - 4; i++) {
                            //??????
                            String billNo = StringUtils.substringBetween(list.get(0)[0], "[", "]").replaceAll("\t", "");
                            //??????????????????
                            String tradeNo = list.get(i)[0].replaceAll("\t", "");
                            //????????????
                            String outTradeNo = list.get(i)[1].replaceAll("\t", "");
                            //????????????
                            String bizType = list.get(i)[2].replaceAll("\t", "");
                            //????????????
                            String productName = list.get(i)[3].replaceAll("\t", "");
                            //??????????????????
                            Date tradeCreateTime = DateUtils.parseDate(list.get(i)[4].replaceAll("\t", ""),"yyyy-MM-dd HH:mm:ss");
                            //??????????????????
                            Date tradeEndTime = DateUtils.parseDate(list.get(i)[5].replaceAll("\t", ""),"yyyy-MM-dd HH:mm:ss");
                            //????????????
                            String buyerId = list.get(i)[10].replaceAll("\t", "");
                            //????????????
                            String billAmount = list.get(i)[11].replaceAll("\t", "");
                            //????????????
                            String receiptAmount = list.get(i)[12].replaceAll("\t", "");
                            //????????????
                            String refundBath = list.get(i)[21].replaceAll("\t", "");

                            AliPayBillDO bill = new AliPayBillDO();
                            bill.setTradeNo(tradeNo);
                            bill.setOutTradeNo(outTradeNo);
                            bill.setBizType(bizType);
                            bill.setSubject(productName);
                            bill.setStartTime(tradeCreateTime);
                            bill.setEndTime(tradeEndTime);
                            bill.setBuyerLogonId(buyerId);
                            bill.setTotalAmount(Double.valueOf(billAmount));
                            bill.setReceiptAmount(Double.valueOf(receiptAmount));
                            bill.setRefundBatchNo(refundBath);
                            repository.save(bill);
                        }
                    }
                }catch (Exception e){

                }
            });


        } else {
            System.out.println("????????????");
        }
        return "";
    }
}
