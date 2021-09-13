package com.zebone.alipay.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "payment_ali_bill")
public class AliPayBillDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 商品名称
     */
    private String subject;

    /**
     * 买家支付宝账号
     */
    private String buyerLogonId;


    /**
     * 商家订单号
     */
    private String outTradeNo;



    /**
     * 交易的订单金额，单位为元，两位小数
     */
    private Double totalAmount;


    /**
     * 实收金额
     */
    private Double receiptAmount;

    /**
     * 支付宝交易号
     */
    @Column(unique = true)
    private String tradeNo;


    private String remark;


    private Date startTime;

    private Date endTime;

    /**
     * 退款批次号
     */
    private String refundBatchNo;

}
