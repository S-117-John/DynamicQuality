package com.zebone.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.weixin.config.MyWxConfig;
import com.zebone.weixin.entity.WxPayConfigDO;
import com.zebone.weixin.entity.WxRefundRecordDO;
import com.zebone.weixin.entity.WxTradeRecordDO;
import com.zebone.weixin.repository.WxPayConfigRepository;
import com.zebone.weixin.repository.WxRefundRecordRepository;
import com.zebone.weixin.repository.WxTradeRecordRepository;
import com.zebone.weixin.sdk.WXPay;
import com.zebone.weixin.sdk.WXPayUtil;
import com.zebone.weixin.vo.WxpayParam;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "api/wx")
public class WxPayController {

    @Autowired
    private WxPayConfigRepository payConfigRepository;

    @Autowired
    private WxTradeRecordRepository tradeRecordRepository;

    @Autowired
    private WxRefundRecordRepository refundRecordRepository;

    @GetMapping("micropay")
    public String micropay(String param) {
        System.out.println(param);
        return "";
    }

    /**
     * 除付款码支付场景以外，商户系统先调用该接口在微信支付服务后台生成预支付交易单，
     * 返回正确的预支付交易会话标识后再按Native、JSAPI、APP等不同场景生成交易串调起支付
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("nativePay")
    public String nativePay(@RequestBody String param) throws Exception {
        WxpayParam wxpayParam = JSON.parseObject(param, WxpayParam.class);
        WxPayConfigDO wxPayConfig = payConfigRepository.findFirstByAppId(wxpayParam.getAppId());
        wxpayParam.setMchId(wxPayConfig.getMchId());
        wxpayParam.setNotifyUrl(wxPayConfig.getNotifyUrl());
        wxpayParam.setTradeType("NATIVE");
        MyWxConfig myWxConfig = new MyWxConfig();
        myWxConfig.setApiKey(wxPayConfig.getPaySignKey());
        myWxConfig.setAppId(wxpayParam.getAppId());
        myWxConfig.setMchId(wxPayConfig.getMchId());
        WXPay wxPay = new WXPay(myWxConfig);
        Map<String, String> data = new HashMap<>();
        data.put("body", wxpayParam.getBody());
        data.put("out_trade_no", wxpayParam.getOutTradeNo());
        data.put("total_fee", wxpayParam.getTotalFee() + "");
        InetAddress addr = InetAddress.getLocalHost();
        data.put("spbill_create_ip", addr.getHostAddress());
        data.put("notify_url", wxpayParam.getNotifyUrl());
        data.put("trade_type", "NATIVE");
        Map<String, String> result = wxPay.unifiedOrder(data);
        return JSON.toJSONString(result);
    }

    /**
     * 该接口提供所有微信支付订单的查询，商户可以通过查询订单接口主动查询订单状态，完成下一步的业务逻辑。
     * <p>
     * 需要调用查询接口的情况：
     * <p>
     * ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知（查单实现可参考：支付回调和查单实现指引）；
     * ◆ 调用支付接口后，返回系统错误或未知交易状态情况；
     * ◆ 调用付款码支付API，返回USERPAYING的状态；
     * ◆ 调用关单或撤销接口API之前，需确认支付状态；
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("orderquery")
    public String orderquery(@RequestBody String param) throws Exception {
        WxpayParam wxpayParam = JSON.parseObject(param, WxpayParam.class);
        WxPayConfigDO wxPayConfig = payConfigRepository.findFirstByAppId(wxpayParam.getAppId());
        MyWxConfig myWxConfig = new MyWxConfig();
        myWxConfig.setApiKey(wxPayConfig.getPaySignKey());
        myWxConfig.setAppId(wxpayParam.getAppId());
        myWxConfig.setMchId(wxPayConfig.getMchId());
        WXPay wxPay = new WXPay(myWxConfig);
        Map<String, String> data = new HashMap<>();
        data.put("out_trade_no", wxpayParam.getOutTradeNo());
        data.put("transaction_id", wxpayParam.getTransactionId());
        Map<String, String> result = wxPay.orderQuery(data);
        String dataResult = JSON.toJSONString(result);
        if("SUCCESS".equals(MapUtils.getString(result,"return_code"))){
            WxTradeRecordDO wxTradeRecord = JSON.parseObject(dataResult,WxTradeRecordDO.class);
            tradeRecordRepository.save(wxTradeRecord);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 以下情况需要调用关单接口：商户订单支付失败需要生成新单号重新发起支付，
     * 要对原订单号调用关单，避免重复支付；系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * <p>
     * 注意：订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟。
     *
     * @param param
     * @return
     */
    @PostMapping("closeorder")
    public String closeorder(@RequestBody String param) throws Exception {
        WxpayParam wxpayParam = JSON.parseObject(param, WxpayParam.class);
        WxPayConfigDO wxPayConfig = payConfigRepository.findFirstByAppId(wxpayParam.getAppId());
        MyWxConfig myWxConfig = new MyWxConfig();
        myWxConfig.setApiKey(wxPayConfig.getPaySignKey());
        myWxConfig.setAppId(wxpayParam.getAppId());
        myWxConfig.setMchId(wxPayConfig.getMchId());
        WXPay wxPay = new WXPay(myWxConfig);
        Map<String, String> data = new HashMap<>();
        data.put("out_trade_no", wxpayParam.getOutTradeNo());
        Map<String, String> result = wxPay.closeOrder(data);
        return JSON.toJSONString(result);
    }

    /**
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，
     * 卖家可以通过退款接口将支付款退还给买家，微信支付将在收到退款请求并且验证成功之后，
     * 按照退款规则将支付款按原路退到买家账号上。
     * <p>
     * 注意：
     * <p>
     * 1、交易时间超过一年的订单无法提交退款
     * <p>
     * 2、微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金额不能超过订单金额。 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号
     * <p>
     * 3、请求频率限制：150qps，即每秒钟正常的申请退款请求次数不超过150次
     * <p>
     * 4、每个支付订单的部分退款次数不能超过50次
     * <p>
     * 5、如果同一个用户有多笔退款，建议分不同批次进行退款，避免并发退款导致退款失败
     * <p>
     * 6、申请退款接口的返回仅代表业务的受理情况，具体退款是否成功，需要通过退款查询接口获取结果。
     * <p>
     * 7、一个月之前的订单申请退款频率限制为：5000/min
     *
     * @param param
     * @return
     */
    @PostMapping("refund")
    public String refund(@RequestBody String param) throws Exception {
        WxpayParam wxpayParam = JSON.parseObject(param, WxpayParam.class);
        WxPayConfigDO wxPayConfig = payConfigRepository.findFirstByAppId(wxpayParam.getAppId());
        MyWxConfig myWxConfig = new MyWxConfig();
        myWxConfig.setApiKey(wxPayConfig.getPaySignKey());
        myWxConfig.setAppId(wxpayParam.getAppId());
        myWxConfig.setMchId(wxPayConfig.getMchId());
        WXPay wxPay = new WXPay(myWxConfig);
        Map<String, String> data = new HashMap<>();
        data.put("out_trade_no", wxpayParam.getOutTradeNo());
        data.put("transaction_id", wxpayParam.getTransactionId());
        data.put("out_refund_no", wxpayParam.getOutRefundNo());
        data.put("total_fee", wxpayParam.getTotalFee() + "");
        data.put("refund_fee", wxpayParam.getRefundFee() + "");
        Map<String, String> result = wxPay.refund(data);
        String dataResult = JSON.toJSONString(result);
        if("SUCCESS".equals(MapUtils.getString(result,"return_code"))){
            WxRefundRecordDO refundRecord = JSON.parseObject(dataResult,WxRefundRecordDO.class);
            refundRecordRepository.save(refundRecord);
        }
        return dataResult;
    }

    /**
     * 提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，
     * 用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
     *
     * @param param
     * @return
     */
    @PostMapping("refundquery")
    public String refundquery(@RequestBody String param) throws Exception {
        WxpayParam wxpayParam = JSON.parseObject(param, WxpayParam.class);
        WxPayConfigDO wxPayConfig = payConfigRepository.findFirstByAppId(wxpayParam.getAppId());
        MyWxConfig myWxConfig = new MyWxConfig();
        myWxConfig.setApiKey(wxPayConfig.getPaySignKey());
        myWxConfig.setAppId(wxpayParam.getAppId());
        myWxConfig.setMchId(wxPayConfig.getMchId());
        WXPay wxPay = new WXPay(myWxConfig);
        Map<String, String> data = new HashMap<>();
        data.put("transaction_id", wxpayParam.getTransactionId());
        Map<String, String> result = wxPay.refundQuery(data);
        return JSON.toJSONString(result);
    }

    @PostMapping("downloadbill")
    public String downloadBill(@RequestBody String param) throws Exception {
        WxpayParam wxpayParam = JSON.parseObject(param, WxpayParam.class);
        WxPayConfigDO wxPayConfig = payConfigRepository.findFirstByAppId(wxpayParam.getAppId());
        MyWxConfig myWxConfig = new MyWxConfig();
        myWxConfig.setApiKey(wxPayConfig.getPaySignKey());
        myWxConfig.setAppId(wxpayParam.getAppId());
        myWxConfig.setMchId(wxPayConfig.getMchId());
        WXPay wxPay = new WXPay(myWxConfig);
        Map<String, String> data = new HashMap<>();
        data.put("bill_date", wxpayParam.getBillDate());
        data.put("bill_type", "ALL");
        Map<String, String> billResult = wxPay.downloadBill(data);
        String result = MapUtils.getString(billResult, "data");
        String tradeMsg = result.substring(result.indexOf("`"));
        String tradeInfo = tradeMsg.substring(0, tradeMsg.indexOf("总交易单数"))
                .replaceFirst(DateFormatUtils.
                        format(DateUtils
                                .addDays(DateUtils
                                        .parseDate(wxpayParam
                                                .getBillDate(),"yyyyMMdd"), -1), "yyyy-MM-dd"), "")
                .replaceAll("`", "");// 去掉汇总数据

        String totalTemp = result.substring(result.indexOf("总交易单数")).replaceAll("总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额", "");
        String totalMsg = totalTemp.replaceAll("`", "").trim();
        String[] tradeArray = tradeInfo.split(DateFormatUtils.format(DateUtils.addDays(new Date(), -1),"yyyy-MM-dd HH:mm:ss")); //通过交易时间分隔   订单数
        for (String s : tradeArray) {
            System.out.println("交易信息：" + s);
            //23:06:31,wx0402a678abdc05c8,1250397401,0,,4200001000202104251354651391,W1250397401716FC7A0C2E2C69E5EDCE,ods6wuMe6PWoYyAuTnpU_16LxEMA,JSAPI,SUCCESS,OTHERS,CNY,3.50,0.00,0,0,0.00,0.00,,,『粤TUB281』中山市人民医院临停缴费,webpay,0.00000,0.00%

        }
        return JSON.toJSONString(result);
    }

    @PostMapping("payNotify")
    public String payNotify(@RequestBody String param) throws Exception {
        System.out.println(param);
        Map<String,String> paramMap = WXPayUtil.xmlToMap(param);
        return "";
    }
}
