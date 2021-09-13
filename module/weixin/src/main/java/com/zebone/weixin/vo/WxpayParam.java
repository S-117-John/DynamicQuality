package com.zebone.weixin.vo;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxpayParam implements Serializable {

    @JSONField(name = "appid")
    private String appId;

    @JSONField(name = "mchid")
    private String mchId;

    /**
     * 商品描述
     */
    @JSONField(name = "body")
    private String body;

    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    @JSONField(name = "notify_url")
    private String notifyUrl;
    /**
     * 订单总金额，单位为分
     */
    @JSONField(name = "total_fee")
    private Integer totalFee;

    @JSONField(name = "spbill_create_ip")
    private String spbillCreateIp;

    @JSONField(name = "trade_type")
    private String tradeType;

    /**
     * 微信订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;

    /**
     * 退款金额
     */
    @JSONField(name = "refund_fee")
    private Integer refundFee;

    /**
     * 商户退款单号
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;

    /**
     * 下载对账单的日期，格式：20140603
     */
    @JSONField(name = "bill_date")
    private String billDate;
}
