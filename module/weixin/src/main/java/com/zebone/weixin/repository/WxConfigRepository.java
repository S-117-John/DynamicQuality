package com.zebone.weixin.repository;

import com.zebone.weixin.entity.WxConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WxConfigRepository extends JpaRepository<WxConfigDO,String> {
}
