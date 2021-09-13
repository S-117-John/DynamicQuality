package com.zebone.core.repository;

import com.zebone.core.entity.DictDataDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DictDataRepository extends JpaRepository<DictDataDO,String>, JpaSpecificationExecutor<DictDataDO> {

    List<DictDataDO> findByDictType(String type);
}
