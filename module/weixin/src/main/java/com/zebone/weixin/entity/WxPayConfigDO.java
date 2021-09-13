package com.zebone.weixin.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "payment_wx_config")
public class WxPayConfigDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "certificate_path")
    private String certificatePath;

    @Column(name = "mch_id")
    private String mchId;

    @Column(name = "pay_sign_key")
    private String paySignKey;

    @Column(name = "notify_url")
    private String notifyUrl;

}
