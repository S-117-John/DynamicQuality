package com.zebone.core.repository;

import com.zebone.core.entity.DictTypeDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DictTypeRepository extends JpaRepository<DictTypeDO,String>, JpaSpecificationExecutor<DictTypeDO> {
}
