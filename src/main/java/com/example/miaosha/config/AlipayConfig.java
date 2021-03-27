package com.example.miaosha.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
    @Value("${ali.pay.partner")
    public String partner = "";

    // 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
    @Value("${ali.pay.partner")
    public String seller_id = partner;

    //商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
    public String private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCxAfkO5UPd+JSH5Hsic12wMgPstGxkHOau8sl4q7FrC4Jq3ES" +
            "4oXDLt3+jsKl1CM2NRGxg6QgUp1IWmk14K2KBHkUW7NLRpWaiY75hq7xNWEeb6jO1YN/EyZWPOY1mPJbrg5u78zYCQtvRbbpxQwPqvQI2/3tpZiEZEEBJqMcEvq+" +
            "GJR6nSe/+a6rKrVwH9YKYsZZJOzrdHrjdEeafACcKYmHYUF0yrX78M7Mi9xr118G7QDbkCSh1Ixz/Ls63V7RmL0QYdT9emqXvMM5iQiKyX1JT0crbXw2fh5bJSKlXBT" +
            "WK6CXne/rtScP4R5K+dsy+YMz8GoFVvjvCiaoaPTWtAgMBAAECggEAXXS3NQAjZw0aDu25faT/sEmX/N/94pXHzlLtwMKV9/ipsRX/SqQiU8zcnWZm8h3luVKJDlm7J" +
            "Hx7QOfTQ0FUzutWexYq8PmtSMv09sifsQ6NMGrlRjyRqOruBiLS30naPRrNnNx6hZMbUGXo7Gf/p26HvtYBKNL9YqhL6Rgti/joSwujSc2Ctyu0kN19doDW8zlJnm427Z" +
            "7tgrgmTZUBq6koJeji1G7R8i5m3H1ki6MR0fcRcAP5m+Jni9x2NnolNiO9+pt/6RzzLs/ar4Ot6UNM3oB7gr1ft/D2a2xOsGFL7lZBZP+RIXV/zsoA+dONjGS3F+fmo" +
            "jEEnmF0OZ/G2QKBgQDnjDED85nt3mBCg/G+hW1nNrRKZNmfop/3dFzY76aq7aJeq1Ivs5A3GzXj1npUoNlxGfehVXp6TY7avnqCxiCnsWvtWq1ayJmIqIozNicFFUTmh6" +
            "SAA7L9Cwe/XGdH2sP5RnFUv/1JXerM/JQ1A7CfQKmkXXvITrweGjCTtKVf2wKBgQDDs1Bf+FL6DkzPr+iJOTYMOvfrQY7jsJb4NEyjsn3Ynf6Hyrgu6ENlBW733XcC1+ss" +
            "AAz44Uam706CwAu3Pe8YTgG8d4tLyhFrFFlETgR+3cVtVoCkj7kvqJLaTs7PWMWqLldrjMlyPJ9WH+y4a65fzPrqyzmgdsV1J93XFoibFwKBgQCNs4rPm0Xpf3yzWCIacXdZ" +
            "ge3l1UAlZJW3D3w+urLpyhHjE79xN9ebEcjyDPDCV8maYMTd8/zHWi2Biq5Y171mQrsE/8g1Vaw4KMSiwJIylyiaWAqA2UKxfw2Xu4ntPGFQbR6wOepc+t/V4QEmTILLgvw" +
            "OfbNiC2UumuJ9kkDenwKBgCJHztSA00gqKRpYXgtrkiQiUDtNiJIp5MgloScrpQ/E3Uk8IXdWCDgx83ZHvQkqeW+DssIF9DJZyQ7uRR8cIj3HrFYvOp9UDikLr3dfkzoF7wd" +
            "wF6i75FyM0uya0W9QxY9B/t5AEymHpQGZJgZIFouBhJKMdtO8+mnDSgDbWaKRAoGAH2jh37VsdY5nGlNQfCM9h4s9mJbuG9JUdC1J4DqXOulfjMxZRRqezloC1G87KhhyJm5" +
            "CI8P+3STSGYXNx7ecpEEaRH7vnroBCcqYSQKDSMzpzbyXTMoxHoznETfqN0gVOGOkwNX8A94rnHpBBuq3UlkqJdnM1EtzWLamrxTeMC8=";

    // 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsQH5DuVD3fiUh+R7InNdsDID7LRsZBzmrv" +
            "LJeKuxawuCatxEuKFwy7d/o7CpdQjNjURsYOkIFKdSFppNeCtigR5FFuzS0aVmomO+Yau8TVhHm+oztWDfxMmVjzmNZjyW64Obu/M2AkLb0W26cUMD6r0CNv" +
            "97aWYhGRBASajHBL6vhiUep0nv/muqyq1cB/WCmLGWSTs63R643RHmnwAnCmJh2FBdMq1+/DOzIvca9dfBu0A25AkodSMc/y7Ot1e0Zi9EGHU/" +
            "Xpql7zDOYkIisl9SU9HK218Nn4eWyUipVwU1iugl53v67UnD+EeSvnbMvmDM/BqBVb47womqGj01rQIDAQAB";


    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//    public static String notify_url = "http://www.kk.com/alipay/notify_url.jsp";
    public String notify_url = "http://www.kk.com/kk/alipay/h5pay/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//    public static String return_url = "http://www.kk.com/alipay/return_url.jsp";
    public String return_url = "http://www.kk.com/kk/alipay/h5pay/return";

    // 签名方式
    public String sign_type = "RSA";

    // 字符编码格式 目前支持utf-8
    public String input_charset = "utf-8";

    // 支付类型 ，无需修改
    public String payment_type = "1";

    // 调用的接口名，无需修改
    public String service = "alipay.wap.create.direct.pay.by.user";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
