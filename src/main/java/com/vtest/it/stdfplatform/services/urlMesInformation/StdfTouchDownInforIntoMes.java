package com.vtest.it.stdfplatform.services.urlMesInformation;


import com.vtest.it.stdfplatform.pojo.mes.MesProperties;
import com.vtest.it.stdfplatform.services.vtptmt.impl.VtptmtInforImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * bin summary into MES
 *
 * @author shawn.sun
 * @version 2.0
 * @category IT
 * @since 2018.3.15
 */
@Service
public class StdfTouchDownInforIntoMes {
    @Autowired
    private VtptmtInforImpl vtptmtInfor;
    @Autowired
    private GetStreamFromMes getStreamFromMes;

    public void write(String lotNumber, String waferID, String CP, String infors) {
        try {
            MesProperties properties = vtptmtInfor.getProperties();
            String URL = "?Action=UpdateWaferTestTime&ACode=";
            int RandomNumber = (int) ((Math.random() * 100000000) / 1);
            URL = URL + properties.getAcode() + "&ItemName=WaferLot:" + lotNumber + "|WaferID:" + waferID + "|CP:" + CP.trim() + infors + "&rand=" + RandomNumber;
            getStreamFromMes.getStream(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
