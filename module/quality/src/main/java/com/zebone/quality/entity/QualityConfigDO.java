package com.zebone.quality.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "quality_disease")
public class QualityConfigDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String code;
    private String formUrl;
    private String imagePath;
    private String interfaceUrl;
    private String name;
    private String type;
    private String status;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    private String remarks;
    private String icd9;
    private String ageCondition;
    private String age;
    private String dayCondition;
    private String day;
    private String icd10;
}
