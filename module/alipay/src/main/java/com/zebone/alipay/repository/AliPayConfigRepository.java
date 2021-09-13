package com.zebone.alipay.repository;


import com.zebone.alipay.entity.AliPayConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AliPayConfigRepository extends JpaRepository<AliPayConfigDO,String> {

    AliPayConfigDO findFirstByAppId(String appId);
}
