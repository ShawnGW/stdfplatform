package com.vtest.it.stdfplatform.services.tools;

import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Service
public class GetOrder {
    private static final String REGEX = "yyyyMMddHHmmss";

    public ArrayList<File> Order(File[] fileList) throws ParseException {
        ArrayList<File> files = new ArrayList<>();
        HashMap<Long, File> fileOrderMap = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat(REGEX);
        for (File file : fileList) {
            String[] nameTokens = file.getName().split("_");
            int length = nameTokens.length;
            String rp = nameTokens[length - 4].substring(2, 3);
            String testStartTimeR = (nameTokens[length - 2] + nameTokens[length - 1].substring(0, 6));
            Long time = format.parse(testStartTimeR).getTime();
            Long finalTime = Long.valueOf(rp + time.toString());
            fileOrderMap.put(finalTime, file);
        }
        Long[] numbers = fileOrderMap.keySet().toArray(new Long[0]);
        Arrays.sort(numbers);
        for (Long number : numbers) {
            files.add(fileOrderMap.get(number));
        }
        return files;
    }

}
