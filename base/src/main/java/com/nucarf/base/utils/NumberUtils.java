package com.nucarf.base.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Amor on 2018/5/25.
 */

public class NumberUtils {

    /**
     * 四舍五入保留两位
     *
     * @param money
     * @return
     */
    public static String totalMoney(String money) {
//        Double mAmountF = Double.parseDouble(money) / 100;
        Double mAmountF = Double.parseDouble(money);
        BigDecimal bigDec = new BigDecimal(mAmountF);
        double total = bigDec.setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total);
    }

    //获取截取后的金额字符串类型
    public static String getEndAmountString(String pAmount) {
        if (!TextUtils.isEmpty(pAmount)) {
            Double mAmountF = Double.parseDouble(pAmount);
            return amountToFolat(mAmountF);
        } else {
            return "0";
        }
    }

    //将金额转成 float 并且 只截取整数
    public static String amountToFolat(Double f) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String str = df.format(f).toString();
//        if(str.split("\\.")[1].equals("00")){
        str = str.split("\\.")[0];
//        }
        return str;
    }

    /**
     * 转换万
     *
     * @param number
     * @return
     */
    public static String formatWan(String number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        if (bigDecimal.compareTo(BigDecimal.valueOf(10000)) == 1) {
            // 转换为万元（除以10000）
            BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000"));
            // 保留两位小数
            DecimalFormat formater = new DecimalFormat("0.00");
            // 四舍五入
            formater.setRoundingMode(RoundingMode.HALF_UP);    // 5000008.89

            // 格式化完成之后得出结果
            String formatNum = formater.format(decimal) + "W";
            return formatNum;
        } else {
            return number;
        }

    }

    /**
     * 让 Map按key进行排序
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 实现一个比较器类
     */
    static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareTo(s2);  //从小到大排序
        }
    }

    /**
     * 数字抹零
     * 抹零字段 结算开关（1关 2抹百位 3抹拾位 4抹个位 5抹小数位 6小数点后一位 7小数点后2位（不进位） 8小数点后2位（进位））
     *
     * @param position
     * @return
     */
    public static String getFormatNumber(String position, String number) {
        Double multiply = BigDecimalUtils.multiply(Double.parseDouble(number), 100);
        switch (Integer.parseInt(position)) {
            case 1:
            case 7:
                return ((int) multiply.doubleValue()) / 100f + "";
            case 2:
                Double divide4 = BigDecimalUtils.divide(multiply, 100000);
                return ((int) divide4.doubleValue()) * 1000 + "";
            case 3:
                Double divide = BigDecimalUtils.divide(multiply, 10000);
                return ((int) divide.doubleValue()) * 100 + "";
            case 4:
                Double divide1 = BigDecimalUtils.divide(multiply, 1000);
                return ((int) divide1.doubleValue()) * 10 + "";
            case 5:
                return (int) Double.parseDouble(number) + "";
            case 6:
                Double divide2 = BigDecimalUtils.divide(multiply, 10);
                return ((int) divide2.doubleValue()) / 10f + "";
            case 8:
                return numberHalfUp(number);
            default:
                return number;
        }
    }

    public static String numberHalfUp(String money) {
        Double mAmountF = Double.parseDouble(money);
        BigDecimal bigDec = new BigDecimal(mAmountF);
        double total = bigDec.setScale(2, BigDecimal.ROUND_CEILING)
                .doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total);
    }

    public static String numberHalfDown(String money) {
        Double mAmountF = Double.parseDouble(money);
        BigDecimal bigDec = new BigDecimal(mAmountF);
        double total = bigDec.setScale(2, BigDecimal.ROUND_FLOOR)
                .doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total);
    }

    public static void main(String[] args) {
        String number = "1234.55555";
//        String number = "0.99";
        String formatNumber = getFormatNumber(1 + "", number);
        System.out.println(1 + "--position结果：" + formatNumber);
        String formatNumber2 = getFormatNumber(2 + "", number);
        System.out.println(2 + "--position结果：" + formatNumber2);
        String formatNumber3 = getFormatNumber(3 + "", number);
        System.out.println(3 + "--position结果：" + formatNumber3);
        String formatNumber4 = getFormatNumber(4 + "", number);
        System.out.println(4 + "--position结果：" + formatNumber4);
        String formatNumber5 = getFormatNumber(5 + "", number);
        System.out.println(5 + "--position结果：" + formatNumber5);
        String formatNumber6 = getFormatNumber(6 + "", number);
        System.out.println(6 + "--position结果：" + formatNumber6);
        String formatNumber7 = getFormatNumber(7 + "", number);
        System.out.println(7 + "--position结果：" + formatNumber7);
        String formatNumber8 = getFormatNumber(8 + "", number);
        System.out.println(8 + "--position结果：" + formatNumber8);
    }

}
