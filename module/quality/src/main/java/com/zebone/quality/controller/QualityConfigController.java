package com.zebone.quality.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.quality.entity.QualityConfigDO;
import com.zebone.quality.repository.QualityConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/quality/config")
public class QualityConfigController {

    @Autowired
    private QualityConfigRepository repository;

    @GetMapping
    public String diseaseList(String param){
        QualityConfigDO config = JSON.parseObject(param,QualityConfigDO.class);
        Specification<QualityConfigDO> specification = new Specification<QualityConfigDO>() {
            @Override
            public Predicate toPredicate(Root<QualityConfigDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(!ObjectUtils.isEmpty(config.getType())){
                    predicates.add(criteriaBuilder.equal(root.get("type"),config.getType()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<QualityConfigDO> list = repository.findAll(specification);
        return JSON.toJSONString(list);
    }

    @PostMapping
    public void save(String param){
        QualityConfigDO config = JSON.parseObject(param,QualityConfigDO.class);
        repository.save(config);
    }

}
