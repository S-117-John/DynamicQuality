package com.zebone.core.repository;

import com.zebone.core.entity.DeptDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<DeptDO,String> {
}
