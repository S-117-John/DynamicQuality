package com.zebone.core.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "sys_role")
@Data
public class RoleDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String name;

    private String code;

    private String remark;

    @Transient
    private String key;

    public String getKey() {
        return code;
    }
}
