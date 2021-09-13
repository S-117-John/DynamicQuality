package com.zebone.alipay.repository;

import com.zebone.alipay.entity.AliPayRefundRecordDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AliPayRefundRecordRepository extends JpaRepository<AliPayRefundRecordDO,String> {

    List<AliPayRefundRecordDO> findByGmtRefundPayIsGreaterThan(Date date);
}
