package com.zebone.core.repository;

import com.zebone.core.entity.SysConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysConfigRepository extends JpaRepository<SysConfigDO,String> , JpaSpecificationExecutor<SysConfigDO> {

    SysConfigDO findByConfigKey(String key);

}
