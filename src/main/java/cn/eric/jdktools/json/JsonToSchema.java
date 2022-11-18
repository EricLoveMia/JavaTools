package cn.eric.jdktools.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saasquatch.jsonschemainferrer.AdditionalPropertiesPolicies;
import com.saasquatch.jsonschemainferrer.JsonSchemaInferrer;
import com.saasquatch.jsonschemainferrer.RequiredPolicies;
import com.saasquatch.jsonschemainferrer.SpecVersion;

import java.util.Arrays;

/**
 * @version 1.0.0
 * @description:
 * @author: eric
 * @date: 2022-09-20 16:02
 **/
public class JsonToSchema {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonSchemaInferrer inferrer = JsonSchemaInferrer.newBuilder()
            .setSpecVersion(SpecVersion.DRAFT_04)
            .setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.noOp())
            .setRequiredPolicy(RequiredPolicies.noOp())
            .build();



    public static void main(String[] args) throws Exception  {
//        final JsonNode sample1 = mapper.readTree(
//                "{\"code\":200,\"message\":\"测试通过\",\"data\":{\"data1\":\"test\",\"data2\":[\"test1\",\"test2\"]}}{\"code\":200,\"message\":\"测试通过\",\"data\":[		{\"data1\":\"test\"		}]}{	\"code\":200,	\"message\":\"测试通过\",	\"data\":{		\"data1\":\"test\",		\"data2\":[{	\"object1\":{		\"stu_id\":\"123\",		\"stu_name\":\"张三\"}},{	\"object2\":{		\"work_id\":\"456\",\"work_name\":\"李四\"}}		]	}}");
//        final JsonNode resultForSample1 = inferrer.inferForSample(sample1);
//        for (JsonNode j : Arrays.asList(sample1, resultForSample1)) {
//            System.out.println(mapper.writeValueAsString(j));
//        }



        final JsonNode sample3 = mapper.readTree(
                "{\"code\":200,\"message\":\"测试通过\",\"data\":{\"data1\":\"test\",\"data2\":[{\"test1\":\"111\",\"test2\":\"111\"}]}}{\"code\":200,\"message\":\"测试通过\",\"data\":[		{\"data1\":\"test\"		}]}{	\"code\":200,	\"message\":\"测试通过\",	\"data\":{		\"data1\":\"test\",		\"data2\":[{	\"object1\":{		\"stu_id\":\"123\",		\"stu_name\":\"张三\"}},{	\"object2\":{		\"work_id\":\"456\",\"work_name\":\"李四\"}}		]	}}");
        final JsonNode resultForSample3 = inferrer.inferForSample(sample3);
        for (JsonNode j : Arrays.asList(sample3, resultForSample3)) {
            System.out.println(mapper.writeValueAsString(j));
        }


//        final JsonNode sample1 = mapper.readTree(
//                "{\"🙈\":\"https://saasquatch.com\",\"🙉\":[-1.5,2,\"hello@saasquat.ch\",false],\"🙊\":3,\"weekdays\":[\"MONDAY\",\"TUESDAY\"]}");
//        final JsonNode sample2 = mapper.readTree(
//                "{\"🙈\":1,\"🙉\":{\"🐒\":true,\"🐵\":[2,\"1234:5678::\"],\"🍌\":null},\"🙊\":null,\"months\":[\"JANUARY\",\"FEBRUARY\"]}");
//        final JsonNode resultForSample1 = inferrer.inferForSample(sample1);
//        final JsonNode resultForSample1And2 =
//                inferrer.inferForSamples(Arrays.asList(sample1, sample2));
//        for (JsonNode j : Arrays.asList(sample1, sample2, resultForSample1, resultForSample1And2)) {
//            System.out.println(mapper.writeValueAsString(j));
//        }

    }

}
