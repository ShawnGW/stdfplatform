package com.vtest.it.stdfplatform.pojo.rcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shawn.sun
 * @date 2020/11/12  16:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParameterSpecBean {
    private String parameter;
    private String testNumber;
    private String lowLimit;
    private String highLimit;
    private String utils;
    private String lowSpec;
    private String highSpec;
}
