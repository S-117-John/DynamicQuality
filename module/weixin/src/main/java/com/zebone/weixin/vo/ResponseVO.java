package com.zebone.weixin.vo;

import lombok.Data;

@Data
public class ResponseVO {

    private Object data;

    private Boolean success;

    private Integer total;
}
