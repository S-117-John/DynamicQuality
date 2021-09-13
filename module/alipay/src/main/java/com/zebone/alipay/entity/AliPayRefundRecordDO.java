package com.zebone.alipay.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "payment_ali_refund_record")
public class AliPayRefundRecordDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String buyerLogonId;

    /**
     * 本次退款是否发生了资金变化
     */
    private String fundChange;

    /**
     * 退款时间
     */
    private Date gmtRefundPay;

    /**
     * 商家订单号
     */
    private String outTradeNo;

    /**
     * 本次退款请求，对应的退款金额
     */
    private Double refundAmount;

    /**
     * 退款申请时间
     */
    private Date refundDate;

    /**
     * 本次退款请求，对应的退款金额
     */
    private Double refundFee;

    /**
     * 退款状态。枚举值：
     * REFUND_SUCCESS 退款处理成功；
     * 未返回该字段表示退款请求未收到或者退款失败；
     * 注：如果退款查询发起时间早于退款时间，或者间隔退款发起时间太短，
     * 可能出现退款查询时还没处理成功，后面又处理成功的情况，
     * 建议商户在退款发起后间隔10秒以上再发起退款查询请求。
     */
    private String refundStatus;

    /**
     * 支付宝交易号
     */
    @Column(unique = true)
    private String tradeNo;



}
