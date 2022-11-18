package cn.eric.jdktools.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import lombok.Data;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description:
 * @author: 钱旭
 * @date: 2022-10-09 09:30
 **/
public class JsonObjectTest {


    public static void main(String[] args) {
        Test test = JSONObject.parseObject("{\"id\":\"1111111111\",\"body\":{\"isDeleted\":\"0\"}}", Test.class);


        JSONObject toJSON = (JSONObject) JSONObject.toJSON(test.getBody());
        toJSON.put("age",23);
        toJSON.put("desc","222");

        System.out.println(JSON.toJSONString(test));

    }


    @Data
    static class Test {

        String id;

        Object body;
    }
}
