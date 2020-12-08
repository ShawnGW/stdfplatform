package com.vtest.it.stdfplatform.pojo.rcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author shawn.sun
 * @date 2020/08/20  15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaferInformationBean {
    private Map<String, ParameterBean> parameterLimitMap;
    private Map<String, Double> parameterSigmaMa;
    private List<String> parameterList;
    private Map<CoordinateBean, DieInformationBean> specialDieMap;
}
