package com.zebone.core.repository;

import com.zebone.core.entity.UserDeptDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDeptRepository extends JpaRepository<UserDeptDO,String> {

    List<UserDeptDO> findByUserId(String id);
}
