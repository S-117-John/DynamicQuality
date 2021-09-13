package com.zebone.quality.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.zebone.quality.entity.CustomFormDO;
import com.zebone.quality.repository.CustomFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/quality/form")
public class DynamicFormController {

    @Autowired
    private CustomFormRepository repository;

    @PostMapping
    public void save(String param){
        CustomFormDO customForm = JSON.parseObject(param,CustomFormDO.class);
        if(!ObjectUtils.isEmpty(customForm.getRequired())&&customForm.getRequired().contains("y")){
            customForm.setRequired("y");
        }else {
            customForm.setRequired(null);
        }
        repository.save(customForm);
    }

    @GetMapping
    public String list(String param){
        JSONObject root = JSON.parseObject(param);
        String formCode = root.getString("formCode");
        if(ObjectUtils.isEmpty(formCode)){
            return "";
        }
        List<CustomFormDO> customFormList = repository.findByFormCodeOrderBySortAsc(formCode);

        return JSON.toJSONString(customFormList);
    }

    @DeleteMapping
    public void delete(String param){
        JSONObject root = JSON.parseObject(param);
        String id = root.getString("id");
        if(ObjectUtils.isEmpty(id)){
            return;
        }
        repository.deleteById(id);
    }

    @GetMapping("all")
    public String getAll(String param){
        List<CustomFormDO> customFormList = repository.findAll();
        if(customFormList.size()>0){
            customFormList = customFormList.stream().filter(distinctByKey(CustomFormDO::getFormCode)).collect(Collectors.toList());
        }

        return JSON.toJSONString(customFormList);
    }


    @GetMapping("rules")
    public String getRules(String param){
        JSONObject root = JSON.parseObject(param);
        String formCode = root.getString("formCode");
        String code = root.getString("code");
        CustomFormDO customForm = repository.findByFormCodeAndCode(formCode,code);
        return customForm!=null?customForm.getRules():"";

    }

    @PostMapping("rules")
    public void saveRules(String param){
        JSONObject root = JSON.parseObject(param);
        CustomFormDO customForm = root.getObject("data",CustomFormDO.class);
        String rules = root.getString("rules");
        if(ObjectUtils.isEmpty(rules)||ObjectUtils.isEmpty(customForm.getId())){
            return;
        }
        customForm.setRules(rules);
        repository.save(customForm);
    }


    @PutMapping
    public void editForm(String param){
        CustomFormDO customForm = JSON.parseObject(param,CustomFormDO.class);
        String formCode = customForm.getFormCode();
        String formName = customForm.getFormName();
        String id = customForm.getId();
        if(ObjectUtils.isEmpty(formName)||ObjectUtils.isEmpty(formCode)||ObjectUtils.isEmpty(id)){
            return;
        }
        CustomFormDO temp = repository.getById(id);
        List<CustomFormDO> list = repository.findByFormCodeOrderBySortAsc(temp.getFormCode());
        for (CustomFormDO customFormDO : list) {
            customFormDO.setFormCode(formCode);
            customFormDO.setFormName(formName);
        }
        repository.saveAll(list);

    }


    public <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> set = ConcurrentHashMap.newKeySet();
        System.out.println("----------");
        return t -> set.add(keyExtractor.apply(t));
    }
}
