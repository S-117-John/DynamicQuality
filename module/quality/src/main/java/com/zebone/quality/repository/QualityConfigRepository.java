package com.zebone.quality.repository;

import com.zebone.quality.entity.QualityConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QualityConfigRepository extends JpaRepository<QualityConfigDO,String>, JpaSpecificationExecutor<QualityConfigDO> {

    QualityConfigDO findByCode(String code);
}
