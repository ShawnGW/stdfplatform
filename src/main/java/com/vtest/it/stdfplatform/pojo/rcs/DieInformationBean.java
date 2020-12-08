package com.vtest.it.stdfplatform.pojo.rcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shawn.sun
 * @date 2020/08/20  13:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DieInformationBean {
    private int h;
    private int s;
    private int t;
    private List<Double> list;
}
