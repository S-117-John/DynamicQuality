package com.zebone.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zebone.core.entity.DeptDO;
import com.zebone.core.entity.UserDO;
import com.zebone.core.entity.UserDeptDO;
import com.zebone.core.repository.DeptRepository;
import com.zebone.core.repository.UserDeptRepository;
import com.zebone.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author 卡卡西
 */
@RestController
@RequestMapping("api/sys/user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private UserDeptRepository userDeptRepository;

    @PostMapping
    public void save(String param) {
        UserDO user = JSON.parseObject(param, UserDO.class);
        if (ObjectUtils.isEmpty(user.getPassword())) {
            user.setPassword("123");
        }
        BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(cryptPasswordEncoder.encode(user.getPassword()));
        user = repository.save(user);
        JSONObject root = JSON.parseObject(param);
        String deptId = root.getString("deptId");
        if (!ObjectUtils.isEmpty(deptId)) {
            UserDeptDO userDept = new UserDeptDO();
            userDept.setUserId(user.getId());
            userDept.setDeptId(deptId);
            userDeptRepository.save(userDept);
        }
    }

    @GetMapping
    public String list() {
        List<UserDO> list = repository.findAll();
        list.forEach(a -> {
            a.setPassword(null);
            List<UserDeptDO> userDepts = userDeptRepository.findByUserId(a.getId());
            UserDeptDO userDept = userDepts.stream().findFirst().orElse(null);
            if (userDept != null) {
                a.setDeptId(userDept.getDeptId());
                DeptDO dept = deptRepository.getById(userDept.getDeptId());
                a.setDeptName(dept.getName());
            }
        });

        return JSON.toJSONString(list);
    }

    @DeleteMapping
    public void delete(String param) {
        UserDO user = JSON.parseObject(param, UserDO.class);
        repository.deleteById(user.getId());
    }

    @PostMapping("role")
    public void addRoles(String param) {
        JSONObject root = JSON.parseObject(param);

        UserDO user = root.getObject("user", UserDO.class);
        user = repository.getById(user.getId());
        List<String> roleIds = root.getObject("roleIds", new TypeReference<List<String>>() {
        });

        user.setRoles(StringUtils.collectionToDelimitedString(roleIds, ","));
        repository.save(user);

        System.out.println(param);
    }

    @GetMapping("role")
    public String userRole(String param) {
        UserDO user = JSON.parseObject(param, UserDO.class);
        user = repository.getById(user.getId());
        String roleString = user.getRoles();
        if (ObjectUtils.isEmpty(roleString)) {
            return "";
        }
        return JSON.toJSONString(StringUtils.delimitedListToStringArray(roleString, ","));

    }
}
