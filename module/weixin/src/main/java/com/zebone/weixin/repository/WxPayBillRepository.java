package com.zebone.weixin.repository;

import com.zebone.weixin.entity.WxPayBillDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WxPayBillRepository extends JpaRepository<WxPayBillDO,String> {
}
