package com.vtest.it.stdfplatform.datadeal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StartDeal {
    @Autowired
    private StdfPlatformDataDeal stdfPlatformDataDeal;
    @Scheduled(fixedDelay = 2000)
    public void stdfDealStart(){
        stdfPlatformDataDeal.deal(null);
    }
}
