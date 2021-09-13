package com.zebone.core.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sys_user")
@Data
public class UserDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(unique = true)
    private String username;

    private String password;

    private String name;

    private String roles;

    @Transient
    private String deptId;

    @Transient
    private String deptName;

    @Transient
    private List<RoleDO> roleList;
}
