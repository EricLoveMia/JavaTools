package cn.eric.jdktools.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Company: 苏州渠成易销网络科技有限公司
 *
 * @version 1.0.0
 * @description: 测试
 * @author: 钱旭
 * @date: 2022-01-07 14:29
 **/
public class Test {
//    @JsonProperty(required = true,value = "name")
//    @JsonPropertyDescription("This is a property description")

    @Schema(title = "名称",description = "这是一个名称")
    private String name;

//    @JsonProperty(value = "child")
//    @JsonPropertyDescription("This is a property description")
    @Schema(title = "孩子",description = "这是一个孩子")
    private TestChild child;

    //    @JsonCreator
//    public Test (
//            @JsonProperty("name") String name,
//            @JsonProperty("child") TestChild child) {
//        this.name = name;
//        this.child = child;
//    }
    public String getName () {
        return name;
    }
    public TestChild getChild () {
        return child;
    }
}
