package cn.eric.jdktools.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

/**
 * Company: 苏州渠成易销网络科技有限公司
 *
 * @version 1.0.0
 * @description: test
 * @author: 钱旭
 * @date: 2022-01-07 17:18
 **/
public class CreateJsonSchema {

//    public static void main(String[] args){
//        ObjectMapper mapper = new ObjectMapper();
//        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
//        JsonSchema schema = schemaGen.generateSchema(Test.class);
//        String toString = schema.get$schema();
//        System.out.println(toString);
//    }

//    public static void main(String[] args) {
//        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON);
//        SchemaGeneratorConfig config = configBuilder.build();
//        SchemaGenerator generator = new SchemaGenerator(config);
//        JsonNode jsonSchema = generator.generateSchema(Test.class);
//
//        System.out.println(jsonSchema.toString());
//    }

//    public static void main(String[] args) {
//        //Swagger2Module module = new Swagger2Module();
//        JacksonModule module = new JacksonModule();
//        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON)
//                .with(module);
//        SchemaGeneratorConfig config = configBuilder.build();
//        SchemaGenerator generator = new SchemaGenerator(config);
//        JsonNode jsonSchema = generator.generateSchema(InputProperties.class);
//
//        System.out.println(jsonSchema.toString());
//    }
}
