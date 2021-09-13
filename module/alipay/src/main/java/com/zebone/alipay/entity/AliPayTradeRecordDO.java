package com.zebone.alipay.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "payment_ali_trade_record")
public class AliPayTradeRecordDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    /**
     * 买家支付宝账号
     */
    private String buyerLogonId;


    /**
     * 商家订单号
     */
    private String outTradeNo;

    /**
     * 本次交易打款给卖家的时间
     */
    private Date sendPayDate;

    /**
     * 交易的订单金额，单位为元，两位小数
     */
    private Double totalAmount;

    /**
     * 支付宝交易号
     */
    @Column(unique = true)
    private String tradeNo;

    /**
     * 交易状态：
     * WAIT_BUYER_PAY（交易创建，等待买家付款）、
     * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
     * TRADE_SUCCESS（交易支付成功）、
     * TRADE_FINISHED（交易结束，不可退款）
     */
    private String tradeStatus;


}
