package com.zebone.quality.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zebone.core.entity.DictDataDO;
import com.zebone.core.repository.DictDataRepository;
import com.zebone.quality.anno.DynamicUpload;
import com.zebone.quality.entity.CustomFormDO;
import com.zebone.quality.entity.QualityConfigDO;
import com.zebone.quality.entity.QualityDiseaseDO;
import com.zebone.quality.repository.CustomFormRepository;
import com.zebone.quality.repository.QualityConfigRepository;
import com.zebone.quality.repository.QualityDiseaseRepository;
import com.zebone.quality.vo.FieldProps;
import com.zebone.quality.vo.Options;
import net.hasor.core.AppContext;
import net.hasor.dataway.DatawayService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/quality/view")
public class QualityController {

    @Autowired
    private CustomFormRepository customFormRepository;

    @Autowired
    private DictDataRepository dictDataRepository;

    @Autowired
    private QualityDiseaseRepository qualityDiseaseRepository;

    @Autowired
    private QualityConfigRepository configRepository;

    @Autowired
    private AppContext appContext;

    @GetMapping
    public String form(String param){

        JSONObject root = JSON.parseObject(param);
        String formCode = root.getString("formCode");
        String caseId = root.getString("caseId");
        List<CustomFormDO> list = customFormRepository.findByFormCodeOrderBySortAsc(formCode);
        formCode = formCode.toLowerCase();
        String id = root.getString("id");
        JSONObject data = new JSONObject();
        if(!ObjectUtils.isEmpty(id)){
            QualityDiseaseDO qualityDisease = qualityDiseaseRepository.getById(id);
            String result = qualityDisease.getData();
            data = JSON.parseObject(result);
        }
        Map<String,Object> valueMap = new HashMap<>();
        if(!ObjectUtils.isEmpty(caseId)&&ObjectUtils.isEmpty(id)){

            QualityConfigDO config = configRepository.findByCode(formCode);
            String url = config.getIcd10();
            DatawayService dataway = appContext.getInstance(DatawayService.class);
            Map<String, Object> paramData = new HashMap<String, Object>() {{
                put("caseId", caseId);
            }};

// 结果
            try {
                Map<String, Object> result = (Map<String, Object>) dataway.invokeApi( url, paramData);
                valueMap = (Map<String, Object>) result.get("value");

                System.out.println(result);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        for (CustomFormDO customFormDO : list) {
            List<String> dependencies = new ArrayList<>();
            String rulesJson = customFormDO.getRules();
            if(!ObjectUtils.isEmpty(rulesJson)){
                JSONArray jsonArray = JSON.parseArray(rulesJson);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    dependencies.add(object.getString("target"));
                }
                customFormDO.setDependencies(dependencies);
            }




            String dictType = "";
            if(!ObjectUtils.isEmpty(customFormDO.getCode())&&customFormDO.getCode().contains("CM")){
                dictType = formCode.toUpperCase()+"_"+customFormDO.getCode().toUpperCase().replaceAll("-","_");
            }else {
                dictType = customFormDO.getCode().toUpperCase().replaceAll("-","_");
            }
            List<DictDataDO> dictDataList = dictDataRepository.findByDictType(dictType);
            FieldProps fieldProps = new FieldProps();
            List<Options> options = new ArrayList<>();
            for (DictDataDO dictData : dictDataList) {
                Options option = new Options();
                option.setLabel(dictData.getDictLabel());
                option.setValue(dictData.getDictValue());
                options.add(option);
            }
            fieldProps.setOptions(options);
            customFormDO.setFieldProps(fieldProps);
            if(!ObjectUtils.isEmpty(id)){
                String initvalue = data.getString(customFormDO.getCode());
                customFormDO.setInitData(initvalue);
            }

//            导入患者信息
            if(valueMap.size()>0){
                String value = MapUtils.getString(valueMap,customFormDO.getCode());
                customFormDO.setInitData(value);
            }

        }


        return JSONObject.toJSONString(list);
    }


    @DynamicUpload
    @PostMapping
    public String save(String param){
        JSONObject root = JSON.parseObject(param);
        String caseid = root.getString("caseId");
        String type = root.getString("formCode");
        String id = root.getString("id");
        QualityDiseaseDO qualityDisease = new QualityDiseaseDO();
        if(!ObjectUtils.isEmpty(id)){
            qualityDisease.setId(id);
        }
        qualityDisease.setCaseId(caseid);
        qualityDisease.setCreateDate(new Date());
        qualityDisease.setType(type);
        qualityDisease.setData(param);
        qualityDiseaseRepository.save(qualityDisease);
        System.out.println(param);
        return "";
    }

    @GetMapping("list")
    public String list(String param){
        List<QualityDiseaseDO> list = qualityDiseaseRepository.findAll();
        return JSON.toJSONString(list);
    }

    @DeleteMapping
    public void delete(String param){
        QualityDiseaseDO qualityDisease = JSON.parseObject(param,QualityDiseaseDO.class);
        qualityDiseaseRepository.deleteById(qualityDisease.getId());
    }
}
