package com.zebone.quality.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.zebone.core.entity.SysConfigDO;
import com.zebone.core.repository.SysConfigRepository;


import com.zebone.quality.entity.QualityConfigDO;
import com.zebone.quality.repository.QualityConfigRepository;
import com.zebone.quality.vo.UploadResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

@Component
@Aspect
public class DynamicUploadAop {

    @Autowired
    private QualityConfigRepository qualityConfigRepository;

    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Around("@annotation(com.zebone.quality.anno.DynamicUpload)")
    public Object around(ProceedingJoinPoint joinPoint) {
        String param = "";
        Object object = null;
        JSONObject jsonObject = new JSONObject();
        Object[] args = joinPoint.getArgs();
        try {
//            Object[] args = joinPoint.getArgs();
            if (args.length != 1) {
                return null;
            }
            param = (String)args[0];
            JSONObject root = JSON.parseObject(param);
            String formCode = root.getString("formCode");

            QualityConfigDO qualityConfig = qualityConfigRepository.findByCode(formCode);
            String apiUrl = qualityConfig.getInterfaceUrl();
            SysConfigDO sysConfig =  sysConfigRepository.findByConfigKey("quality.url");
            String url = sysConfig.getConfigValue()+apiUrl;
            root.remove("formCode");
            jsonObject.put("code","0");
            String httpPostAddr = url;
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
            converterList.remove(1);    //移除StringHttpMessageConverter
            HttpMessageConverter<?> converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
            converterList.add(1, converter);    //convert顺序错误会导致失败
            restTemplate.setMessageConverters(converterList);
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> formEntity = new HttpEntity<String>(JSON.toJSONString(root), headers);
            String result = restTemplate.postForObject(httpPostAddr,formEntity,String.class);
            UploadResult uploadResult = JSON.parseObject(result, UploadResult.class);
            Integer resultCode = Optional.ofNullable(uploadResult).map(a->a.getCode()).orElse(null);
            if(resultCode==1000){
                String errorMessage = Optional.ofNullable(uploadResult).map(a->a.getMessage()).orElse("上传失败");
                jsonObject.put("code","1");
                jsonObject.put("msg",errorMessage);
            }

            object = joinPoint.proceed(args);
            object = jsonObject;
        } catch (Exception e) {
            object = joinPoint.proceed(args);
            jsonObject.put("code","1");
            jsonObject.put("msg",e.getMessage());
            object = jsonObject;
        } finally {
            return JSON.toJSONString(object);
        }
    }
}
