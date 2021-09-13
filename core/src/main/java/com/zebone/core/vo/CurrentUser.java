package com.zebone.core.vo;

import com.zebone.core.entity.RoleDO;
import lombok.Data;

import java.util.List;

@Data
public class CurrentUser {

    private String name;

    private String avatar;

    private String userid;

    private String email;

    private String signature;

    private String title;

    private String group;

    private String notifyCount;

    private String unreadCount;

    private String country;

    private String access;

    private String address;

    private String phone;




}
