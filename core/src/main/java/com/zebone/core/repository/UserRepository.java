package com.zebone.core.repository;

import com.zebone.core.entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 卡卡西
 */
public interface UserRepository extends JpaRepository<UserDO,String> {

    UserDO findByUsername(String account);
}
