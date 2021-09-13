package com.zebone.core.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.core.entity.DictDataDO;
import com.zebone.core.entity.SysConfigDO;
import com.zebone.core.repository.SysConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/sys/config")
public class SysConfigController {

    @Autowired
    private SysConfigRepository repository;

    @GetMapping
    public String list(String param){
        SysConfigDO sysConfig = JSON.parseObject(param,SysConfigDO.class);
        Specification<SysConfigDO> specification = new Specification<SysConfigDO>() {
            @Override
            public Predicate toPredicate(Root<SysConfigDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(!ObjectUtils.isEmpty(sysConfig.getConfigName())){
                    predicates.add(criteriaBuilder.equal(root.get("configName"),sysConfig.getConfigName()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<SysConfigDO> list = repository.findAll(specification);
        return JSON.toJSONString(list);
    }

    @PostMapping
    public void save(String param){
        SysConfigDO sysConfig = JSON.parseObject(param,SysConfigDO.class);
        repository.save(sysConfig);
    }

    @DeleteMapping
    public void delete(String param){
        SysConfigDO sysConfig = JSON.parseObject(param,SysConfigDO.class);
        repository.deleteById(sysConfig.getId());

    }
}
