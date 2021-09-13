package com.zebone.quality.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "quality_disease_data")
public class QualityDiseaseDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String type;

    private String caseId;

    @Column(length = 8000)
    private String data;

    private Date createDate;
}
