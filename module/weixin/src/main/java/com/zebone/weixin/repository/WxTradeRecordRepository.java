package com.zebone.weixin.repository;

import com.zebone.weixin.entity.WxTradeRecordDO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WxTradeRecordRepository extends JpaRepository<WxTradeRecordDO,String>, JpaSpecificationExecutor<WxTradeRecordDO> {
}
