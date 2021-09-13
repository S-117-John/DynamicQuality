package com.zebone.weixin.vo;

import lombok.Data;

import java.util.Date;

@Data
public class WxTradeRecordParam {

    private String tradeState;

    private Integer totalFee;

    private String transactionId;

    private String outTradeNo;

    private String timeEnd;
}
