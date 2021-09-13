package com.zebone.alipay.vo;

import lombok.Data;

@Data
public class ResponseVO {

    private Object data;

    private Boolean success;

    private Integer total;
}
