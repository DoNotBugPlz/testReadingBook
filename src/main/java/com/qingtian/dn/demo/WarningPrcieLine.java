package com.qingtian.dn.demo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WarningPrcieLine {
    private String goodsName;
    private String spec;
    private String measurementUnit;
    private String indexName;
    private BigDecimal normalValue;
    private BigDecimal higherRiseValue;
    private BigDecimal lowerRiseValue;
    private BigDecimal highestRiseValue;
    private BigDecimal lowestRiseValue;

}
