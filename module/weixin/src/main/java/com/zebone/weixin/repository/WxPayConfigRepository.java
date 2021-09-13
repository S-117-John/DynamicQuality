package com.zebone.weixin.repository;

import com.zebone.weixin.entity.WxPayConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WxPayConfigRepository extends JpaRepository<WxPayConfigDO,String> {

    WxPayConfigDO findFirstByAppId(String appId);
}
