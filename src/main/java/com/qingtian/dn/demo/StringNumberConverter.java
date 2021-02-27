package com.qingtian.dn.demo;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.math.BigDecimal;

public class StringNumberConverter implements Converter<BigDecimal> {

    @Override
    public Class<BigDecimal> supportJavaTypeKey() {
        return BigDecimal.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public BigDecimal convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        BigDecimal result;
        CellDataTypeEnum dataType=cellData.getType();
        if(dataType==CellDataTypeEnum.NUMBER){
            result = cellData.getNumberValue();
        } else{
            result = new BigDecimal(cellData.getStringValue());
        }
        return result;
    }

    @Override
    public CellData<String> convertToExcelData(BigDecimal bigDecimal, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new CellData<>();
    }


}
