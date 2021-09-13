package com.zebone.core.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.core.entity.DictDataDO;
import com.zebone.core.entity.DictTypeDO;
import com.zebone.core.repository.DictDataRepository;
import com.zebone.core.repository.DictTypeRepository;
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
@RequestMapping("api/sys/dict")
public class DictController {

    @Autowired
    private DictTypeRepository typeRepository;

    @Autowired
    private DictDataRepository dataRepository;

    @GetMapping("type")
    public String typeList(String param){

        DictTypeDO dictType = JSON.parseObject(param,DictTypeDO.class);
        Specification<DictTypeDO> specification = new Specification<DictTypeDO>() {
            @Override
            public Predicate toPredicate(Root<DictTypeDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(!ObjectUtils.isEmpty(dictType.getDictType())){
                    predicates.add(criteriaBuilder.equal(root.get("dictType"),dictType.getDictType()));
                }
                if(!ObjectUtils.isEmpty(dictType.getDictName())){
                    predicates.add(criteriaBuilder.equal(root.get("dictName"),dictType.getDictName()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<DictTypeDO> list = typeRepository.findAll(specification);
        return JSON.toJSONString(list);
    }

    @PostMapping("type")
    public void saveType(String param){
        DictTypeDO dictType = JSON.parseObject(param,DictTypeDO.class);
        typeRepository.save(dictType);
    }

    @GetMapping("data")
    public String dataList(String param){
        DictDataDO dictData = JSON.parseObject(param,DictDataDO.class);
        Specification<DictDataDO> specification = new Specification<DictDataDO>() {
            @Override
            public Predicate toPredicate(Root<DictDataDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(!ObjectUtils.isEmpty(dictData.getDictType())){
                    predicates.add(criteriaBuilder.equal(root.get("dictType"),dictData.getDictType()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<DictDataDO> list = dataRepository.findAll(specification, Sort.by("treeSort").ascending());
        return JSON.toJSONString(list);
    }

    @PostMapping("data")
    public void saveData(String param){
        DictDataDO dictData = JSON.parseObject(param,DictDataDO.class);
        dataRepository.save(dictData);
    }

    @DeleteMapping("data")
    public void delete(String param){
        DictDataDO dictData = JSON.parseObject(param,DictDataDO.class);
        dataRepository.deleteById(dictData.getId());
    }

}
