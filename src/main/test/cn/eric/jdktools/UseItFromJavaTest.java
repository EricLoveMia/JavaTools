package cn.eric.jdktools;

import cn.eric.jdktools.base.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
//import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
//import com.kjetland.jackson.jsonSchema.SubclassesResolver;
//import com.kjetland.jackson.jsonSchema.SubclassesResolverImpl;

import java.time.OffsetDateTime;
import java.util.*;

public class UseItFromJavaTest {

    static class MyJavaPojo {
        public String name;
    }

    public UseItFromJavaTest() {
//        // Just make sure it compiles
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonSchemaGenerator g1 = new JsonSchemaGenerator(objectMapper);
//        // TODO - This is not very beautiful from Java - Need to improve Java API
//        JsonNode jsonNode;
//        jsonNode = g1.generateJsonSchema(Test.class);
//        //g1.generateJsonSchema(MyJavaPojo.class, "My title", "My description");
//
//        //g1.generateJsonSchema(objectMapper.constructType(MyJavaPojo.class));
//        //g1.generateJsonSchema(objectMapper.constructType(MyJavaPojo.class), "My title", "My description");
//
//        String s = jsonNode.toString();
//        System.out.println(s);
//        // Create custom JsonSchemaConfig from java
//        Map<String,String> customMapping = new HashMap<>();
//        customMapping.put(OffsetDateTime.class.getName(), "date-time");
//        JsonSchemaConfig config = JsonSchemaConfig.create(
//                true,
//                Optional.of("A"),
//                false,
//                false,
//                true,
//                true,
//                true,
//                true,
//                true,
//                customMapping,
//                false,
//                new HashSet<>(),
//                new HashMap<>(),
//                new HashMap<>(),
//                null,
//                true,
//                null);
//        JsonSchemaGenerator g2 = new JsonSchemaGenerator(objectMapper, config);
//        jsonNode = g2.generateJsonSchema(Test.class);
//        System.out.println(jsonNode.toString());
//        // Config SubclassesResolving
//
//        final SubclassesResolver subclassesResolver = new SubclassesResolverImpl()
//                .withClassesToScan(Arrays.asList(
//                        "this.is.myPackage"
//                ));


    }


    public static void main(String[] args) {
        UseItFromJavaTest test = new UseItFromJavaTest();
    }
}
