package com.vtest.it.stdfplatform.datadeal;

import com.vtest.it.stdfplatform.pojo.system.WaferInitInformationBean;
import com.vtest.it.stdfplatform.services.tools.GetOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class StdfPlatformDataDeal {
    @Autowired
    private GetOrder getOrder;
    public void deal(HashMap<WaferInitInformationBean,File[]> resources){
        for (Map.Entry<WaferInitInformationBean,File[]> entry: resources.entrySet()) {
            ArrayList<File> waferIdOrderList = getOrder.Order(entry.getValue());
            System.out.println(entry.getKey().getWaferId());
        }
    }
}
