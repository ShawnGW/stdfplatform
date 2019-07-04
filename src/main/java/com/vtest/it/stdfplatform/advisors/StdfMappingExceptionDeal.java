package com.vtest.it.stdfplatform.advisors;

import com.vtest.it.stdfplatform.pojo.system.WaferInitInformationBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.DataParseIssueBean;
import com.vtest.it.stdfplatform.services.rawdataCheck.GetIssueBean;
import com.vtest.it.stdfplatform.services.vtptmt.impl.VtptmtInforImpl;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Aspect
@Component
public class StdfMappingExceptionDeal {
    @Autowired
    private GetIssueBean getIssueBean;
    @Autowired
    private VtptmtInforImpl vtptmtInfor;

    @AfterThrowing(value = "execution(* generateRawdata(..))&&target(com.vtest.it.stdfplatform.datadeal.GenerateRawdataInitInformation)&&args(waferInitInformationBean,waferIdOrderList)", throwing = "exception")
    public void dealException(Exception exception, WaferInitInformationBean waferInitInformationBean, ArrayList<File> waferIdOrderList) {
        try {
            DataParseIssueBean dataParseIssueBean = getIssueBean.getDataBeanForException(5, exception.getMessage(), waferInitInformationBean, waferIdOrderList);
            ArrayList<DataParseIssueBean> list = new ArrayList<>();
            list.add(dataParseIssueBean);
            vtptmtInfor.dataErrorsRecord(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
