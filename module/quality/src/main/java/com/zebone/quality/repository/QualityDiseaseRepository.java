package com.zebone.quality.repository;

import com.zebone.quality.entity.QualityDiseaseDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QualityDiseaseRepository extends JpaRepository<QualityDiseaseDO,String>, JpaSpecificationExecutor<QualityDiseaseDO> {
}
