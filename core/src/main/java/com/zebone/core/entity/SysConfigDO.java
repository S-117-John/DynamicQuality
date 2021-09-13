package com.zebone.core.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sys_config")
@Data
public class SysConfigDO {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String configName;

    private String configKey;

    private String configValue;


    private String configType;

    private String remarks;
}
