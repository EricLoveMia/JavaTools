package cn.eric.jdktools.json;

import cn.eric.jdktools.date.DateUtil;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.codemodel.*;
import org.apache.commons.lang3.StringUtils;
import org.jsonschema2pojo.AbstractAnnotator;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.rules.FormatRule;

import java.util.*;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description:
 * @author: 钱旭
 * @date: 2022-09-09 11:46
 **/
public class EricAnnotator extends AbstractAnnotator {

    private List<String> requiredList = new ArrayList<>();

    public EricAnnotator(GenerationConfig generationConfig) {
        super(generationConfig);
    }

//    public void propertyOrder(JDefinedClass clazz, JsonNode propertiesNode) {
//        JAnnotationArrayMember annotationValue = clazz.annotate(JsonPropertyOrder.class).paramArray("value");
//        Iterator properties = propertiesNode.fieldNames();
//
//        while(properties.hasNext()) {
//            annotationValue.param((String)properties.next());
//        }
//
//    }
    
    public void propertyField(JFieldVar field, JDefinedClass clazz, String propertyName, JsonNode propertyNode) {
        JAnnotationUse value = field.annotate(Property.class).param("value", propertyName);
        if (propertyNode.has("description")) {
            value.param("description", propertyNode.get("description").asText());
        }
        if (propertyNode.has("title")) {
            value.param("title", propertyNode.get("title").asText());
        }
        if (requiredList.contains(propertyName)) {
            value.param("required", true);
        }
    }

    public void propertyInclusion(JDefinedClass clazz, JsonNode schema) {
        JAnnotationUse model = clazz.annotate(Model.class).param("date", DateUtil.format(new Date(), DateUtil.PATTERN_YYYY_MM_DD_HHMMSS));
        // title
        JsonNode title = schema.get("title");
        if(title != null){
            model.param("name",title.asText());
        }

        // title
        JsonNode description = schema.get("description");
        if(description != null){
            model.param("description",title.asText());
        }

        // required
        JsonNode required = schema.get("required");
        if(required != null){
            requiredList = new ArrayList<>();
            int size = required.size();
            for (int i = 0; i < size; i++) {
                JsonNode jsonNode = required.get(i);
                String text = jsonNode.asText();
                requiredList.add(text);
            }

        }
    }



//    public void propertyGetter(JMethod getter, String propertyName) {
//        getter.annotate(Property.class).param("value", propertyName);
//    }
//
//    public void propertySetter(JMethod setter, String propertyName) {
//        setter.annotate(Property.class).param("value", propertyName);
//    }

//    public void dateField(JFieldVar field, JsonNode node) {
//        String pattern = null;
//        if (node.has("customDatePattern")) {
//            pattern = node.get("customDatePattern").asText();
//        } else if (node.has("customPattern")) {
//            pattern = node.get("customPattern").asText();
//        } else if (StringUtils.isNotEmpty(this.getGenerationConfig().getCustomDatePattern())) {
//            pattern = this.getGenerationConfig().getCustomDatePattern();
//        } else if (this.getGenerationConfig().isFormatDates()) {
//            pattern = FormatRule.ISO_8601_DATE_FORMAT;
//        }
//
//        if (pattern != null && !field.type().fullName().equals("java.lang.String")) {
//            field.annotate(JsonFormat.class).param("shape", JsonFormat.Shape.STRING).param("pattern", pattern);
//        }
//
//    }

//    public void dateTimeField(JFieldVar field, JsonNode node) {
//        String timezone = node.has("customTimezone") ? node.get("customTimezone").asText() : "UTC";
//        String pattern = null;
//        if (node.has("customDateTimePattern")) {
//            pattern = node.get("customDateTimePattern").asText();
//        } else if (node.has("customPattern")) {
//            pattern = node.get("customPattern").asText();
//        } else if (StringUtils.isNotEmpty(this.getGenerationConfig().getCustomDateTimePattern())) {
//            pattern = this.getGenerationConfig().getCustomDateTimePattern();
//        } else if (this.getGenerationConfig().isFormatDateTimes()) {
//            pattern = FormatRule.ISO_8601_DATETIME_FORMAT;
//        }
//
//        if (pattern != null && !field.type().fullName().equals("java.lang.String")) {
//            field.annotate(JsonFormat.class).param("shape", Shape.STRING).param("pattern", pattern).param("timezone", timezone);
//        }
//
//    }

}
