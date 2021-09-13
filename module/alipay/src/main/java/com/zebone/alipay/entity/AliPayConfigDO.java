package com.zebone.alipay.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "payment_ali_config")
@Data
public class AliPayConfigDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "gateway")
    private String gateway;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "pay_public_key",length = 2000)
    private String payPublicKey;

    @Column(name = "private_key",length = 2000)
    private String privateKey;

    @Column(name = "public_key",length = 2000)
    private String publicKey;
}
