package com.vtest.it.stdfplatform.advisors;

import com.vtest.it.stdfplatform.pojo.system.WaferInitInformationBean;
import org.apache.commons.io.FileUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Aspect
@Component
public class DataSourceBeforeDeal {
    @Value("${system.properties.stdf.file-source}")
    private String sourcePath;

    @Around(value = "execution(* deal(..))&&target(com.vtest.it.stdfplatform.datadeal.StdfPlatformDataDeal)")
    public void dealRightFiles(ProceedingJoinPoint proceedingJoinPoint) {
        File dataSource = new File(sourcePath);
        File[] customers = dataSource.listFiles();
        HashMap<WaferInitInformationBean, File[]> resources = new HashMap<>();
        for (File customer : customers) {
            if (directoryCheckAndDeal(customer)) {
                String customerChar = customer.getName();
                for (File device : customer.listFiles()) {
                    if (directoryCheckAndDeal(device)) {
                        String deviceChar = device.getName();
                        for (File lot : device.listFiles()) {
                            if (directoryCheckAndDeal(lot)) {
                                String lotChar = lot.getName();
                                for (File cpStep : lot.listFiles()) {
                                    if (directoryCheckAndDeal(cpStep)) {
                                        String cpStepChar = cpStep.getName();
                                        for (File waferId : cpStep.listFiles()) {
                                            if (directoryCheckAndDeal(waferId)) {
                                                String waferIdChar = waferId.getName();
                                                if (fileTimeCheck(waferId)){
                                                    WaferInitInformationBean waferInitInformationBean = new WaferInitInformationBean();
                                                    waferInitInformationBean.setCustomCode(customerChar);
                                                    waferInitInformationBean.setDevice(deviceChar);
                                                    waferInitInformationBean.setLot(lotChar);
                                                    waferInitInformationBean.setCp(cpStepChar);
                                                    waferInitInformationBean.setWaferId(waferIdChar);
                                                    resources.put(waferInitInformationBean, waferId.listFiles());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        try {
            proceedingJoinPoint.proceed(new Object[]{resources});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    private boolean directoryCheckAndDeal(File file) {
        if (file.isFile()) {
            try {
                FileUtils.forceDelete(file);
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (file.listFiles().length == 0) {
                try {
                    FileUtils.forceDelete(file);
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    private boolean fileTimeCheck(File file){
        long now=System.currentTimeMillis();
        File[] datas=file.listFiles();
        for (File data : datas) {
            long fileLastModifyTime=data.lastModified();
            if (((now-fileLastModifyTime)/1000)<60){
                return false;
            }
        }
        return true;
    }
}
