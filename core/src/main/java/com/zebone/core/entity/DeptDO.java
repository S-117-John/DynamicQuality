package com.zebone.core.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 卡卡西
 */
@Data
@Entity
@Table(name = "sys_dept")
public class DeptDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "dept_name")
    private String name;

    @Column(name = "dept_code")
    private String code;

    @Column(name = "parent_code")
    private String parentCode = "";

    @Column(name = "parent_codes")
    private String parentCodes = "";

    @Column(name = "dept_type")
    private String officeType = "";



}
