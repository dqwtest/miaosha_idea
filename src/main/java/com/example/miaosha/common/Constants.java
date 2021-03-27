package com.example.miaosha.common;

/**
 * 提交统计次数
 */
public class Constants {

    public static String CLOSE_ORDER_INFO_TASK_LOCK = "CLOSE_ORDER_INFO_KEY";
    public static String COUNTLOGIN = "count:login";

    public enum orderStatus {
        ORDER_NOT_PAY("新建未支付");

        private String name;

        orderStatus(String name) {
            this.name = name;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
