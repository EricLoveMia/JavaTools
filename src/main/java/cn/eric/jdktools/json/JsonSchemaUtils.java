package cn.eric.jdktools.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: jsonschema工具类
 * @author: 钱旭
 * @date: 2022-02-18 16:23
 **/
public class JsonSchemaUtils {

    /**
     * 返回当前数据类型
     * @param source
     * @return
     */
    public String getTypeValue(Object source){

        if(source instanceof String){
            return "String";
        }

        if(source instanceof Integer){
            return "Integer";
        }

        if(source instanceof Float){
            return "Float";
        }

        if(source instanceof Long){
            return "Long";
        }

        if(source instanceof Double){
            return "Double";
        }

        if(source instanceof Date){
            return "Date";
        }

        if(source instanceof Boolean){
            return "Boolean";
        }

        return "null";
    }


    /**
     * 把Object变成JsonSchema
     * @param source
     * @return
     */
    public Object generateJsonSchema(Object source){

        Object result = new Object();

        //判断是否为JsonObject
        if(source instanceof JSONObject){
            JSONObject jsonResult = JSONObject.fromObject(result);
            JSONObject sourceJSON = JSONObject.fromObject(source);
            Iterator iterator = sourceJSON.keys();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                Object nowValue = sourceJSON.get(key);

                if(nowValue == null || nowValue.toString().equals("null")){
                    jsonResult.put(key,"null");

                }else if(isJsonObject(nowValue)){
                    jsonResult.put(key,generateJsonSchema(nowValue));
                }else if(isJsonArray(nowValue)){
                    JSONArray tempArray = JSONArray.fromObject(nowValue);
                    JSONArray newArray = new JSONArray();

                    if(tempArray != null && tempArray.size() > 0 ){
                        for(int i = 0 ;i < tempArray.size(); i++){
                            newArray.add(generateJsonSchema(tempArray.get(i)));
                        }
                        jsonResult.put(key,newArray);
                    }
                }else if(nowValue instanceof List){
                    List<Object> newList = new ArrayList<Object>();

                    for(int i = 0;i<((List) nowValue).size();i++){
                        newList.add(((List) nowValue).get(i));
                    }

                    jsonResult.put(key,newList);
                }else {

                    jsonResult.put(key,getTypeValue(nowValue));
                }

            }
            return jsonResult;
        }


        if(source instanceof JSONArray){
            JSONArray jsonResult = JSONArray.fromObject(source);
            JSONArray tempArray = new JSONArray();
            if(jsonResult != null && jsonResult.size() > 0){
                for(int i = 0 ;i < jsonResult.size(); i++){
                    tempArray.add(generateJsonSchema(jsonResult.get(i)));
                }
                return tempArray;
            }

        }

        return getTypeValue(source);

    }



    /**
     * JSON格式比对
     * @param currentJSON
     * @param expectedJSON
     * @return
     */
    public JSONObject diffJson(JSONObject currentJSON,JSONObject expectedJSON){

        JSONObject jsonDiff = new JSONObject();

        Iterator iterator = expectedJSON.keys();

        while (iterator.hasNext()){
            String key = (String)iterator.next();
            Object expectedValue = expectedJSON.get(key);
            Object currentValue = currentJSON.get(key);
            if(!expectedValue.toString().equals(currentValue.toString())){
                JSONObject tempJSON = new JSONObject();
                tempJSON.put("value",currentValue);
                tempJSON.put("expected",expectedValue);
                jsonDiff.put(key,tempJSON);
            }
        }
        return jsonDiff;
    }


    /**
     * 判断是否为JSONObject
     * @param value
     * @return
     */
    public boolean isJsonObject(Object value){

        try{
            if(value instanceof JSONObject) {
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 判断是否为JSONArray
     * @param value
     * @return
     */
    public boolean isJsonArray(Object value){

        try{

            if(value instanceof JSONArray){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            return false;
        }
    }


    /**
     * JSON格式比对，值不能为空,且key需要存在
     * @param current
     * @param expected
     * @return
     */
    public JSONObject diffFormatJson(Object current,Object expected){

        JSONObject jsonDiff = new JSONObject();

        if(isJsonObject(expected)) {

            JSONObject expectedJSON = JSONObject.fromObject(expected);
            JSONObject currentJSON = JSONObject.fromObject(current);

            Iterator iterator = JSONObject.fromObject(expectedJSON).keys();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Object expectedValue = expectedJSON.get(key);

                if (!currentJSON.containsKey(key)) {
                    JSONObject tempJSON = new JSONObject();
                    tempJSON.put("actualKey", "不存在此" + key);
                    tempJSON.put("expectedKey", key);
                    jsonDiff.put(key, tempJSON);

                }

                if (currentJSON.containsKey(key)) {

                    Object currentValue = currentJSON.get(key);

                    if (expectedValue != null && currentValue == null || expectedValue.toString() != "null" && currentValue.toString() == "null") {
                        JSONObject tempJSON = new JSONObject();
                        tempJSON.put("actualValue", "null");
                        tempJSON.put("expectedValue", expectedValue);
                        jsonDiff.put(key, tempJSON);
                    }

                    if (expectedValue != null && currentValue != null) {
                        if (isJsonObject(expectedValue) && !JSONObject.fromObject(expectedValue).isNullObject() || isJsonArray(expectedValue) && !JSONArray.fromObject(expectedValue).isEmpty()) {
                            JSONObject getResultJSON = new JSONObject();
                            getResultJSON = diffFormatJson(currentValue, expectedValue);
                            if (getResultJSON != null) {
                                jsonDiff.putAll(getResultJSON);
                            }
                        }
                    }
                }
            }
        }

        if(isJsonArray(expected)){
            JSONArray expectArray = JSONArray.fromObject(expected);
            JSONArray currentArray = JSONArray.fromObject(current);

            if(expectArray.size() != currentArray.size()){
                JSONObject tempJSON = new JSONObject();
                tempJSON.put("actualLenth",currentArray.size());
                tempJSON.put("expectLenth",expectArray.size());
                jsonDiff.put("Length",tempJSON);
            }

            if(expectArray.size() != 0){
                Object expectIndexValue = expectArray.get(0);
                Object currentIndexValue = currentArray.get(0);

                if(expectIndexValue != null && currentIndexValue != null){
                    if (isJsonObject(expectIndexValue) && !JSONObject.fromObject(expectIndexValue).isNullObject() || isJsonArray(expectIndexValue) && !JSONArray.fromObject(expectIndexValue).isEmpty()) {
                        JSONObject getResultJSON = new JSONObject();
                        getResultJSON = diffFormatJson(currentIndexValue, expectIndexValue);
                        if (getResultJSON != null) {
                            jsonDiff.putAll(getResultJSON);
                        }
                    }
                }
            }
        }

        return jsonDiff;
    }


    public static void main(String[] args) {

        JsonSchemaUtils diffMethod = new JsonSchemaUtils();


        String str1 = "{\"status\":201,\"msg\":\"今天您已经领取过，明天可以继续领取哦！\",\"res\":{\"remainCouponNum\":\"5\",\"userId\":\"123123213222\"}}";

        JSONObject jsonObject1 = JSONObject.fromObject(str1);


        String str2 = "{\"status\":201,\"msg2\":\"今天您已经领取过，明天可以继续领取哦！\",\"res\":{\"remainCouponNum\":\"5\",\"userId\":\"123123213222\"}}";

        JSONObject jsonObject2 = JSONObject.fromObject(str2);


        String str3 = "{\"status\":null,\"msg\":\"今天您已经领取过，明天可以继续领取哦！\",\"res\":{\"remainCouponNum\":\"5\",\"userId\":\"123123213222\"}}";

        JSONObject jsonObject3 = JSONObject.fromObject(str3);


        String str4 = "{\"status\":\"201\",\"msg\":\"今天您已经领取过，明天可以继续领取哦！\",\"res\":{\"remainCouponNum\":\"5\",\"userId\":\"123123213222\"}}";
        JSONObject jsonObject4 = JSONObject.fromObject(str4);

        System.out.println("转换成JSONschame:" + diffMethod.generateJsonSchema(jsonObject1).toString());

        System.out.println("当前str2没有msg字段: " + diffMethod.diffFormatJson(jsonObject2,jsonObject1).toString());

        System.out.println("当前str3中的status为null值:" + diffMethod.diffFormatJson(jsonObject3,jsonObject1).toString());

        System.out.println("当前str4中的:" + diffMethod.diffFormatJson(jsonObject4,jsonObject1).toString());
    }

}
