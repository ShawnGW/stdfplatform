package com.vtest.it.stdfplatform.pojo.rcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shawn.sun
 * @date 2020/08/20  13:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParameterAfterCalculateResultBean {
    private boolean flag;
    private double lowLimit;
    private double highLimit;
    private String unit;
    private double minValue;
    private double maxValue;
    private double average;
    private double middleValue;
    private double mostValue;
    private double standardDeviation;
    private double variance;
    private double valueOfQuarter;
    private double valueOfThreeQuarter;
}
