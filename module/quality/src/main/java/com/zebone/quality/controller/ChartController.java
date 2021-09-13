package com.zebone.quality.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zebone.quality.entity.QualityDiseaseDO;
import com.zebone.quality.repository.QualityDiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/quality/chart")
public class ChartController {

    @Autowired
    private QualityDiseaseRepository repository;

    @GetMapping("index")
    public String getAll(){
        List<QualityDiseaseDO> result = repository.findAll();

        Map<String, List<QualityDiseaseDO>> list = result.parallelStream()
                .collect(Collectors.groupingBy(QualityDiseaseDO::getType));
        System.out.println(list);
        JSONArray jsonArray = new JSONArray();
        list.forEach((a,b)->{
            JSONObject object = new JSONObject();
            object.put("name",a);
            object.put("count",b.size());
            jsonArray.add(object);
        });
        return JSON.toJSONString(jsonArray);
    }
}
