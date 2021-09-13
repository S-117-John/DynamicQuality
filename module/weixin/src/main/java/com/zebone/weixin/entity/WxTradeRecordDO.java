package com.zebone.weixin.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment_wx_trade_record")
@Data
public class WxTradeRecordDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String appId;

    private String mchId;

    private String deviceInfo;

    private String openid;

    private String tradeType;

    private String tradeState;

    private String bankType;

    private Integer totalFee;

    private Integer settlementTotalFee;

    private String feeType;

    private Integer cashFee;

    private String cashFeeType;

    private Integer couponFee;

    private Integer couponCount;

    private String couponTypeN;

    private String 	couponIdN;

    private Integer couponFeeN;

    @Column(unique = true)
    private String transactionId;

    private String outTradeNo;

    private String attach;

    /**
     * 订单支付时间，格式为yyyyMMddHHmmss
     */
    private Date timeEnd;

    private String tradeStateDesc;
}
