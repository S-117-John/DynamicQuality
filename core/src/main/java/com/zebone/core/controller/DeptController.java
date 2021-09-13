package com.zebone.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zebone.core.entity.DeptDO;
import com.zebone.core.repository.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 卡卡西
 */
@RestController
@RequestMapping("api/sys/dept")
public class DeptController {

    @Autowired
    private DeptRepository repository;

    @PostMapping
    public void save(String param){
        DeptDO dept = JSON.parseObject(param,DeptDO.class);
        repository.save(dept);
    }

    @GetMapping
    public String list(String param){
        List<DeptDO> list = repository.findAll();
        return JSON.toJSONString(list);
    }

    @DeleteMapping
    public void delete(String param){
        DeptDO dept = JSON.parseObject(param,DeptDO.class);
        repository.deleteById(dept.getId());
    }

    @GetMapping("select")
    public String select(){
        List<DeptDO> list = repository.findAll();
        JSONArray array = new JSONArray();
        for (DeptDO dept : list) {
            JSONObject obj = new JSONObject();
            obj.put("label",dept.getName());
            obj.put("value",dept.getId());
            array.add(obj);
        }
        return JSON.toJSONString(array);
    }
}
