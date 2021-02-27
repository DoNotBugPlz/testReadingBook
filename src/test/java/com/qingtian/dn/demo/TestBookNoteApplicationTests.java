package com.qingtian.dn.demo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBookNoteApplicationTests {

    /*Java 书籍*/
    @Test
    public void testEffectiveJava() {
        /*测试computeIfAbsent方法*/
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("test", "1223，324");
        testMap.put("amazing", "122331，23324");
        testMap.computeIfAbsent("342", (i) -> Long.parseLong(i));
        System.out.println(testMap);

    }

    /*testMap*/
    @Test
    public void testMap() {
        List<Map<String, Object>> initialList = new ArrayList<>();
        Map<String, Object> temp1 = new HashMap<>();
        Map<HashSet, Map> temp2 = new HashMap<>();
        HashSet<Long> keyHashSet = new HashSet<>();
        keyHashSet.add(21L);
        temp1.put("啦啦", new ArrayList<String>());
        temp2.put(keyHashSet, temp1);
        keyHashSet.add(2131L);
        System.out.println(temp2.get(keyHashSet));

    }

    @Test
    public void testEasyExcel() {
        //这里需要获得线
        String fileNameForLine = "/Users/dn/Desktop/大豆油2020年价格预警线.xlsx";
        List<ExcelDate> resultList = getExcelDates(fileNameForLine);
        ExcelDate excelDate = resultList.get(0);
        WarningPrcieLine warningPrcieLine = BeanUtil.copyProperties(excelDate, WarningPrcieLine.class, "");
        BigDecimal avgValue = resultList.stream().map(ExcelDate::getAvgValue).reduce(BigDecimal::add)
                .map(element -> element.divide(BigDecimal.valueOf(resultList.size()), 7, BigDecimal.ROUND_HALF_UP)).orElse(BigDecimal.ZERO);
        warningPrcieLine.setNormalValue(avgValue);
        double higherLine = resultList.stream().map(ExcelDate::getAvgValue).filter(element -> element.compareTo(avgValue) > 0).mapToDouble(BigDecimal::doubleValue).average().orElse(0);
        warningPrcieLine.setHigherRiseValue(BigDecimal.valueOf(higherLine));
        double lowerLine = resultList.stream().map(ExcelDate::getAvgValue).filter(element -> element.compareTo(avgValue) < 0).mapToDouble(BigDecimal::doubleValue).average().orElse(0);
        warningPrcieLine.setLowerRiseValue(BigDecimal.valueOf(lowerLine));
        double highestLine = resultList.stream().map(ExcelDate::getAvgValue).filter(element -> element.compareTo(warningPrcieLine.getHigherRiseValue()) > 0)
                .mapToDouble(BigDecimal::doubleValue)
                .average().orElse(0);
        double lowestLine = resultList.stream().map(ExcelDate::getAvgValue).filter(element -> element.compareTo(warningPrcieLine.getLowerRiseValue()) < 0)
                .mapToDouble(BigDecimal::doubleValue)
                .average().orElse(0);
        warningPrcieLine.setHighestRiseValue(BigDecimal.valueOf(highestLine));
        warningPrcieLine.setLowestRiseValue(BigDecimal.valueOf(lowestLine));
        String fileNameForData = "/Users/dn/Desktop/大豆油2020年平均价.xlsx";
        List<ExcelDate> excelDates = getExcelDates(fileNameForData);
        long highestCont = excelDates.stream().map(ExcelDate::getAvgValue).filter(element -> element.compareTo(warningPrcieLine.getHighestRiseValue()) > 0).count();
        long lowestCont = excelDates.stream().map(ExcelDate::getAvgValue).filter(element -> element.compareTo(warningPrcieLine.getLowestRiseValue()) < 0).count();
        System.out.println(StrUtil.format("通过一年数据生成的价格预警线,最高预警线为{},高预警线为{}，平均值为{},低预警线为{},最低预警线为{}",warningPrcieLine.getHighestRiseValue(),warningPrcieLine.getHigherRiseValue()
        ,warningPrcieLine.getNormalValue(),warningPrcieLine.getLowerRiseValue(),warningPrcieLine.getLowestRiseValue()) );
        System.out.println(StrUtil.format("{}的指标类型为{}的最高风险的预警数量是{}个", warningPrcieLine.getGoodsName() + "_" + warningPrcieLine.getSpec() + "_" + warningPrcieLine.getMeasurementUnit(), warningPrcieLine.getIndexName(), highestCont + lowestCont));
    }

    private List<ExcelDate> getExcelDates(String fileName) {
        List<ExcelDate> resultList = new ArrayList<>(300);
        EasyExcel.read(fileName)
                .head(ExcelDate.class)
                .sheet()
                .registerReadListener(new AnalysisEventListener<ExcelDate>() {
                    @Override
                    public void invoke(ExcelDate rowDate, AnalysisContext analysisContext) {
                        resultList.add(rowDate);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                        System.out.println("数据读取完毕！");
                    }
                }).doRead();
        return resultList;
    }

}