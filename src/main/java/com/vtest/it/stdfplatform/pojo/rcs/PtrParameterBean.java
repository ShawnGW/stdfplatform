package com.vtest.it.stdfplatform.pojo.rcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shawn.sun
 * @date 2020/08/20  17:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PtrParameterBean {
    private String testNumber;
    private String site;
    private String result;
    private String parameter;
}
