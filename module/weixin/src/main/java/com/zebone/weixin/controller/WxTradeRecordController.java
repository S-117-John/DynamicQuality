package com.zebone.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.zebone.weixin.config.MyWxConfig;
import com.zebone.weixin.entity.WxPayConfigDO;
import com.zebone.weixin.entity.WxTradeRecordDO;
import com.zebone.weixin.repository.WxPayConfigRepository;
import com.zebone.weixin.repository.WxTradeRecordRepository;
import com.zebone.weixin.sdk.WXPay;
import com.zebone.weixin.vo.ResponseVO;
import com.zebone.weixin.vo.WxTradeRecordParam;
import com.zebone.weixin.vo.WxpayParam;
import lombok.SneakyThrows;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("api/wx/trade/record")
public class WxTradeRecordController {

    @Autowired
    private WxTradeRecordRepository repository;



    @GetMapping
    public String list(HttpServletRequest request) {
        String tradeTime = request.getParameter("timeEnd");
        String transNo = request.getParameter("transactionId");
        String orderNo = request.getParameter("outTradeNo");
        Specification<WxTradeRecordDO> specification = new Specification<WxTradeRecordDO>() {
            @SneakyThrows
            @Override
            public Predicate toPredicate(Root<WxTradeRecordDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (!ObjectUtils.isEmpty(tradeTime)) {
                    Date date = DateUtils.parseDate(tradeTime,"yyyy-MM-dd");
                    predicates.add(criteriaBuilder.greaterThan(root.get("timeEnd"),date));
                }
                if(!ObjectUtils.isEmpty(transNo)){
                    predicates.add(criteriaBuilder.equal(root.get("transactionId"),transNo));
                }
                if(!ObjectUtils.isEmpty(orderNo)){
                    predicates.add(criteriaBuilder.equal(root.get("outTradeNo"),orderNo));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        ResponseVO response = new ResponseVO();
        List<WxTradeRecordDO> list = repository.findAll(specification);
        response.setTotal(list.size());
        response.setSuccess(true);
        response.setData(list);
        return JSON.toJSONString(response);

    }


}
