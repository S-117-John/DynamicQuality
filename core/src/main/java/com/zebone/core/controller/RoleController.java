package com.zebone.core.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.core.entity.RoleDO;
import com.zebone.core.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/sys/role")
public class RoleController {

    @Autowired
    private RoleRepository repository;

    @GetMapping
    public String list(String param){
        List<RoleDO> roleList = repository.findAll();
        return JSON.toJSONString(roleList);
    }

    @PostMapping
    public void save(String param){
        RoleDO role = JSON.parseObject(param,RoleDO.class);
        repository.save(role);
    }

    @DeleteMapping
    public void delete(String param){
        RoleDO role = JSON.parseObject(param,RoleDO.class);
        repository.deleteById(role.getId());
    }
}
