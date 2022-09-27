package cn.eric.jdktools.json;

import java.io.File;

import java.io.IOException;
import java.net.URL;

import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;
import com.sun.codemodel.JCodeModel;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description:
 * @author: 钱旭
 * @date: 2022-09-09 10:56
 **/
public class JsonToPojo {
    /**
     * @param args
     */

    public static void main(String[] args) {

        String packageName = "cn.eric.jdktools";
        File inputJson = new File("json/inputSchema.json");
        File outputPojoDirectory = new File("json" + File.separator + "convertedPojo");
        outputPojoDirectory.mkdirs();

        try {
            new JsonToPojo().convert2JSON(inputJson.toURI().toURL(), outputPojoDirectory, packageName, inputJson.getName().replace(".json", ""));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Encountered issue while converting to pojo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void convert2JSON(URL inputJson, File outputPojoDirectory, String packageName, String className) throws
            IOException {

        JCodeModel codeModel = new JCodeModel();
        URL source = inputJson;
        GenerationConfig config = new DefaultGenerationConfig() {
//            @Override
//            public boolean isGenerateBuilders() { // set config option by overriding method
//                return true;
//            }
            @Override
            public boolean isGenerateBuilders() { // set config option by overriding method
                return false;
            }

            @Override
            public boolean isIncludeHashcodeAndEquals() {
                return false;
            }

            @Override
            public boolean isIncludeToString() {
                return false;
            }

            @Override
            public boolean isIncludeAdditionalProperties() {
                return false;
            }

            public SourceType getSourceType(){
                return SourceType.JSONSCHEMA;
            }
        };

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new EricAnnotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(codeModel,className,packageName,source);
        codeModel.build(outputPojoDirectory);
    }
}