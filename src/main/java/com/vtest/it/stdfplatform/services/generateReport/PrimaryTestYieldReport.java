package com.vtest.it.stdfplatform.services.generateReport;

import com.vtest.it.stdfplatform.pojo.mes.PrimaryTestYieldBean;
import com.vtest.it.stdfplatform.services.tester.impl.TesterInforImpl;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class PrimaryTestYieldReport {
    @Autowired
    private TesterInforImpl testerInfor;
    @Value("${system.properties.stdf.report-path}")
    private String reportPath;
    @Value("${system.properties.stdf.report-ftp-path}")
    private String reportFtpPath;
    @Value("${system.properties.stdf.report-mail-path}")
    private String reportMailPath;
    @Async
    public void write(String customerCode, String device, String lot, String cpStep) throws IOException {
        ArrayList<PrimaryTestYieldBean> list = testerInfor.getPrimaryTestYield(lot, cpStep);
        Set<Integer> siteSet = new TreeSet<>();
        ExcelInitModel model = new ExcelInitModel();
        TreeMap<String, TreeMap<Integer, Double>> resultMap = new TreeMap<>();
        for (PrimaryTestYieldBean bean : list) {
            siteSet.add(bean.getSiteId());
            if (resultMap.containsKey(bean.getWaferNo())) {
                resultMap.get(bean.getWaferNo()).put(bean.getSiteId(), bean.getYield());
            } else {
                TreeMap<Integer, Double> siteYieldMap = new TreeMap<>();
                siteYieldMap.put(bean.getSiteId(), bean.getYield());
                resultMap.put(bean.getWaferNo(), siteYieldMap);
            }
        }
        XSSFWorkbook workbook = model.workbook;
        XSSFSheet sheet = workbook.createSheet("Summary");
        sheet.createRow(0);
        sheet.getRow(0).createCell(0).setCellValue("custom_code");
        sheet.getRow(0).createCell(1).setCellValue("device");
        sheet.getRow(0).createCell(2).setCellValue("lot_id");
        sheet.getRow(0).createCell(3).setCellValue("cp_step");
        sheet.getRow(0).createCell(4).setCellValue("wafer_no");
        int i = 5;
        for (Integer site : siteSet) {
            sheet.getRow(0).createCell(i++).setCellValue("Site" + site);
        }
        for (int j = 0; j < resultMap.size(); j++) {
            sheet.createRow(j + 1);
        }
        int row = 1;
        for (Map.Entry<String, TreeMap<Integer, Double>> entry : resultMap.entrySet()) {
            sheet.getRow(row).createCell(0).setCellValue(customerCode);
            sheet.getRow(row).createCell(1).setCellValue(device);
            sheet.getRow(row).createCell(2).setCellValue(lot);
            sheet.getRow(row).createCell(3).setCellValue(cpStep);
            sheet.getRow(row).createCell(4).setCellValue(entry.getKey());
            int col = 5;
            for (Integer site : entry.getValue().keySet()) {
                sheet.getRow(row).createCell(col).setCellStyle(model.Data_Style);
                sheet.getRow(row).getCell(col++).setCellValue(entry.getValue().get(site));
            }
            row++;
        }
        File directory = new File(reportFtpPath + customerCode + "/" + device + "/" + lot + "/" + cpStep);
        File directory1 = new File(reportMailPath + customerCode + "/" + device + "/" + lot + "/" + cpStep);
        File directory2 = new File(reportPath + customerCode + "/" + device + "/" + lot + "/" + cpStep);
        if (directory.exists()) {
            directory.mkdirs();
        }
        if (directory1.exists()) {
            directory1.mkdirs();
        }
        if (directory2.exists()) {
            directory2.mkdirs();
        }
        File srcFile = new File("/TempBySiteReport/" + lot + "_BySiteSummaryReport.xlsx");
        workbook.write(new FileOutputStream(srcFile));
        workbook.close();
        FileUtils.copyFile(srcFile, new File(directory.getPath() + "/" + srcFile.getName()));
        FileUtils.copyFile(srcFile, new File(directory1.getPath() + "/" + srcFile.getName()));
        FileUtils.copyFile(srcFile, new File(directory2.getPath() + "/" + srcFile.getName()));
        FileUtils.forceDelete(srcFile);
    }
}
