package com.vtest.it.stdfplatform.pojo.rcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author shawn.sun
 * @date 2020/08/20  17:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrrBinBean {
    private String h;
    private String s;
    private String t;
    private String x;
    private String y;
    private String o;
    private String n;
    private String i;
    private Map<String, PtrParameterBean> map;
}
