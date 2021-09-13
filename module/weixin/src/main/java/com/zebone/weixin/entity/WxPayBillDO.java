package com.zebone.weixin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment_wx_bill")
@Data
public class WxPayBillDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "trade_date")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date tradeDate;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "mch_id")
    private String mchId;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "out_trade_no")
    private String outTradeNo;

    @Column(name = "open_id")
    private String openid;

    @Column(name = "trade_type")
    private String tradeType;

    @Column(name = "trade_state")
    private String tradeState;

    @Column(name = "bank_type")
    private String bankType;

    @Column(name = "fee_type")
    private String feeType;

    @Column(name = "total_fee")
    private String totalFee;

    /**
     * 退款金额
     */
    @Column(name = "refund_fee")
    private Integer refundFee;

    /**
     * 商户退款单号
     */
    @Column(name = "out_refund_no")
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    @Column(name = "refund_id")
    private String refundId;

    /**
     * 退款类型
     */
    @Column(name = "refund_type")
    private String refundType;

    /**
     * 退款状态
     */
    @Column(name = "refund_state")
    private String refundState;

    private String body;
}
