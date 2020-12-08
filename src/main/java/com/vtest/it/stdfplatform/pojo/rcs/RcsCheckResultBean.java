package com.vtest.it.stdfplatform.pojo.rcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shawn.sun
 * @date 2020/11/27  16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RcsCheckResultBean {
    private String waferLot;
    private String waferId;
    private String step;
    private String ruleCode;
    private String ruleResult;
}
