package com.zebone.alipay.repository;

import com.zebone.alipay.entity.AliPayBillDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AliPayBillRepository extends JpaRepository<AliPayBillDO,String> {
}
