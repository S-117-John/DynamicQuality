package com.zebone.alipay.repository;

import com.zebone.alipay.entity.AliPayTradeRecordDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AliPayTradeRecordRepository extends JpaRepository<AliPayTradeRecordDO,String> {

    List<AliPayTradeRecordDO> findBySendPayDateIsGreaterThan(Date date);

}
