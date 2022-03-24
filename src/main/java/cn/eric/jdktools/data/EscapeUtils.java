package cn.eric.jdktools.data;


import org.apache.commons.text.StringEscapeUtils;

/**
 * Company: 苏州渠成易销网络科技有限公司
 *
 * @version 1.0.0
 * @description: 转义与反转义
 * @author: 钱旭
 * @date: 2022-01-20 10:13
 **/
public class EscapeUtils {

    public static void main(String[] args) {

        String json = StringEscapeUtils.unescapeEcmaScript("%22%7B%5C%22%24schema%5C%22%3A%5C%22https%3A//json-schema.org/draft/2019-09/schema%5C%22%2C%5C%22type%5C%22%3A%5C%22object%5C%22%2C%5C%22properties%5C%22%3A%7B%5C%22operate%5C%22%3A%7B%5C%22type%5C%22%3A%5C%22string%5C%22%2C%5C%22title%5C%22%3A%5C%22%u64CD%u4F5C%5C%22%2C%5C%22description%5C%22%3A%5C%22%u64CD%u4F5C%u7C7B%u578B%20sum%uFF1A%u6C42%u548C%20max%uFF1A%u6C42%u6700%u5927%u503C%5C%22%7D%7D%7D%22");
        System.out.println(json);
    }
}
