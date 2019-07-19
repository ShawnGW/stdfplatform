package com.vtest.it.stdfplatform.advisors;

import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.services.generateReport.PrimaryTestYieldReport;
import com.vtest.it.stdfplatform.services.vtptmt.impl.VtptmtServices;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GenerateReportPrimaryTestYield {
    @Autowired
    private PrimaryTestYieldReport primaryTestYieldReport;
    @Autowired
    private VtptmtServices vtptmtServices;

    @AfterReturning(value = "execution(* com.vtest.it.stdfplatform.services.rawdatatools.GenerateRawdataTemp.generateTempRawdata(..))&&args(rawdataInitBean,..)", returning = "flag")
    public void generateReport(RawdataInitBean rawdataInitBean, boolean flag) {
        try {
            if (flag) {
                String customerCode = rawdataInitBean.getProperties().get("Customer Code");
                String device = rawdataInitBean.getProperties().get("Device Name");
                if (vtptmtServices.checkDeviceIfGeneratePrimaryYieldReport(customerCode, device)) {
                    String lot = rawdataInitBean.getProperties().get("Lot ID");
                    String cpStep = rawdataInitBean.getProperties().get("CP Process");
                    primaryTestYieldReport.write(customerCode, device, lot, cpStep);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
