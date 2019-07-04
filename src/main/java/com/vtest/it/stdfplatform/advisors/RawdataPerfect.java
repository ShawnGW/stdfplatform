package com.vtest.it.stdfplatform.advisors;

import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.services.FailDieCheck.impl.AdjacentFailDieCheck;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class RawdataPerfect {
    @Autowired
    private AdjacentFailDieCheck adjacentFailDieCheck;

    @AfterReturning(value = "execution(* generateRawdata(..))&&target(com.vtest.it.stdfplatform.datadeal.GenerateRawdataInitInformation)", returning = "rawdataInitBean")
    public void optimizeRawdataBean(RawdataInitBean rawdataInitBean) {
        adjacentFailDieCheck.perfectDeal(rawdataInitBean);
    }
}
