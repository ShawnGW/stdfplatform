package com.vtest.it.stdfplatform.pojo.rcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shawn.sun
 * @date 2020/08/20  15:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParameterJudgementStandardBean {
    private double lowLimit;
    private double highLimit;
}
