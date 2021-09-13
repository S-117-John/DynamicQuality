package com.zebone.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zebone.core.entity.RoleDO;
import com.zebone.core.entity.UserDO;
import com.zebone.core.repository.RoleRepository;
import com.zebone.core.repository.UserRepository;
import com.zebone.core.vo.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author 卡卡西
 */
@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @GetMapping("api/currentUser")
    public String currentUser(Principal principal){
        CurrentUser currentUser = new CurrentUser();
        UserDO user = userRepository.findByUsername(principal.getName());
        if(ObjectUtils.isEmpty(user.getRoles())){
            if("admin".equals(principal.getName())){
                currentUser.setAccess("admin");
            }
        }else {
//            String[] roles = StringUtils.delimitedListToStringArray(user.getRoles(),",");
//            List<String> roleString = Arrays.asList(roles);
//            List<RoleDO> roleList = roleRepository.findAllById(roleString);
//            List<String> roleCodeList = roleList.stream().map(a->a.getCode()).collect(Collectors.toList());
//            currentUser.setAccess(StringUtils.collectionToDelimitedString(roleCodeList,","));
            currentUser.setAccess(user.getRoles());
        }


        currentUser.setName(principal.getName());
        JSONObject object = new JSONObject();
        object.put("success",true);
        object.put("data",currentUser);
        return JSON.toJSONString(object);
    }

    @PostMapping("api/login/account")
    public String login(UserDO account){
        return "";
    }


    @GetMapping("login")
    public String logOut(String param){
        return "";
    }





}
