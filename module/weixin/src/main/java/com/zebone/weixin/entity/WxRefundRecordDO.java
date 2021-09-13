package com.zebone.weixin.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment_wx_refund_record")
@Data
public class WxRefundRecordDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String appId;

    private String mchId;

    private Integer totalRefundCount;

    @Column(unique = true)
    private String transactionId;

    private String outTradeNo;

    private Integer totalFee;

    private Integer settlementTotalFee;

    private String feeType;

    private Integer cashFee;

    private Integer refundCount;

    private String outRefundNo;

    @Column(unique = true)
    private String refundId;

    private String refundChannel;

    private Integer refundFee;

    private Integer settlementRefundFee;

    private String couponType;

    private Integer couponRefundFee;

    private Integer couponRefundCount;

    private String couponRefundId;

    private Integer couponRefundFeeM;

    private String refundStatus;

    private String refundAccount;

    private String refundRecvAccount;

    private Date refundSuccessTime;

}
