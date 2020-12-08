package com.vtest.it.stdfplatform.pojo.rcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shawn.sun
 * @date 2020/08/20  13:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParameterBean {
    private double lowLimit;
    private double highLimit;
    private String unit;
}
