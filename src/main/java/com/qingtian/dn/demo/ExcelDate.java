package com.qingtian.dn.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExcelDate {
    @ExcelProperty("goods_name")
    private String goodsName;
    @ExcelProperty("spec")
    private String spec;
    @ExcelProperty("measurement_unit")
    private String measurementUnit;
    @ExcelProperty("index_name")
    private String indexName;
    @ExcelProperty("price_date_num")
    private Integer priceDateNum;
    @ExcelProperty(value = "avg_value",converter = StringNumberConverter.class)
    private BigDecimal avgValue;
}
