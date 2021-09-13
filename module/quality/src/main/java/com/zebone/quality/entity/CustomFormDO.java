package com.zebone.quality.entity;


import com.zebone.quality.vo.FieldProps;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "quality_custom_form")
@Data
public class CustomFormDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column
    private String formName;

    @Column
    private String formCode;

    @Column
    private String name;

    @Column
    private String code;

    @Column
    private String type;

    @Column(length = 2000)
    private String rules;

    @Column
    private String required;

    @Transient
    private String valueEnum;

    @Column
    private Integer sort;

    @Transient
    private FieldProps fieldProps;

    @Column
    private String initData;

    @Transient
    private List<String> dependencies;

    @Transient
    private String requiredJson;
}
