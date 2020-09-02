package com.vtest.it.stdfplatform.services.urlMesInformation;


import com.vtest.it.stdfplatform.pojo.mes.MesProperties;
import com.vtest.it.stdfplatform.services.vtptmt.impl.VtptmtInforImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GetStreamFromMes {
    @Autowired
    private VtptmtInforImpl vtptmtInfor;

    private static final Logger LOGGER = LoggerFactory.getLogger(GetStreamFromMes.class);
    public BufferedReader getStream(String URL) throws IOException {
        LOGGER.info(URL);
        MesProperties properties = vtptmtInfor.getProperties();
        URL = URL.replaceAll("#", "%23");
        URL url = new URL(properties.getInitUrl() + URL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("HOST", properties.getHost());
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(10000);
        initUrlConnecttion(urlConnection, 0);
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader IsReader = new InputStreamReader(inputStream, "utf8");
        BufferedReader bufferedReader = new BufferedReader(IsReader);
        return bufferedReader;
    }

    private void initUrlConnecttion(HttpURLConnection urlConnection, int times) {
        try {
            urlConnection.connect();
        } catch (Exception e) {
            // TODO: handle exception
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            times++;
            if (times < 3) {
                initUrlConnecttion(urlConnection, times);
            }
        }
    }
}
