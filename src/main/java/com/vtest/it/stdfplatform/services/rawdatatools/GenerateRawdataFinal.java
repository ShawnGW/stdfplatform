package com.vtest.it.stdfplatform.services.rawdatatools;


import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.services.tools.DiffUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GenerateRawdataFinal {
    @Value("${system.properties.stdf.rawdata-path}")
    private String rawdataPath;

    public void generateFinalRawdata(File file, RawdataInitBean rawdataInitBean) {
        String customerCode = rawdataInitBean.getProperties().get("Customer Code");
        String device = rawdataInitBean.getProperties().get("Device Name");
        String lot = rawdataInitBean.getProperties().get("Lot ID");
        String cpProcess = rawdataInitBean.getProperties().get("CP Process");
        String waferId = rawdataInitBean.getProperties().get("Wafer ID");
        File destFile = new File(rawdataPath + customerCode + "/" + device + "/" + lot + "/" + cpProcess + "/" + waferId + ".raw");
        try {
            FileUtils.copyFile(file, destFile);
            if (!DiffUtil.check(file, destFile)) {
                try {
                    FileUtils.forceDelete(destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                generateFinalRawdata(file, rawdataInitBean);
            } else {
                FileUtils.forceDelete(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            generateFinalRawdata(file, rawdataInitBean);
        }
    }
}
