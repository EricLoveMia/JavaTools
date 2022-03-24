package cn.eric.jdktools.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Company: 苏州渠成易销网络科技有限公司
 *
 * @version 1.0.0
 * @description: 测试子类
 * @author: 钱旭
 * @date: 2022-01-07 14:31
 **/

public class TestChild {
    @JsonProperty(required = true)
    @JsonPropertyDescription("这是一个孩子")
    private String childName;
//    @JsonCreator
//    public TestChild (@JsonProperty("childName") String childName) {
//        this.childName = childName;
//    }
//
    public String getChildName () {
        return childName;
    }
}
