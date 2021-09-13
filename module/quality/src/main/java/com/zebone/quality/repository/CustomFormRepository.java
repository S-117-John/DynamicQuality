package com.zebone.quality.repository;



import com.zebone.quality.entity.CustomFormDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomFormRepository extends JpaRepository<CustomFormDO, String> {

    List<CustomFormDO> findByFormCodeOrderBySortAsc(String code);

    CustomFormDO findByFormCodeAndCode(String formCode,String code);
}
