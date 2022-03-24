package cn.eric.jdktools.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: jsonschema工具
 * @author: 钱旭
 * @date: 2022-02-21 11:30
 **/
public class JsonSchemaUtil {

    /**
     * @Author eric
     * @Description 比较target与origin的不同
     * @Date 上午11:31 2022/2/21
     * @Param [origin, target]
     * @return void
     **/
    public static JsonCompareResult compare(String origin,String target){

        JsonCompareResult result = new JsonCompareResult();
        StringBuilder defect = new StringBuilder();
        StringBuilder change = new StringBuilder();
        // 多出来
        StringBuilder extra = new StringBuilder();

        JSONObject originObj = JSONObject.parseObject(origin);
        JSONObject targetObj = JSONObject.parseObject(target);

        // 解析原始json
        JSONObject jsonObject = originObj.getJSONObject("properties");
        // 解析对比json
        JSONObject targetJsonObject = targetObj.getJSONObject("properties");
        if(targetJsonObject == null && jsonObject != null){
            change.append("对象数据类型改变，新版本为空");
        }else if(targetJsonObject != null && jsonObject == null){
            change.append("对象数据类型改变，原版本为空");
        }else {
            for (Map.Entry entry : jsonObject.entrySet()) {
                String key = (String) entry.getKey();
                JSONObject valueObject = JSONObject.parseObject(entry.getValue().toString());
                String type = valueObject.getString("type");

                if (targetJsonObject.get(key) == null) {
                    defect.append("缺少参数key=").append(key).append(";");
                    continue;
                }
                if (targetJsonObject.get(key) != null) {
                    JSONObject targetObject = JSONObject.parseObject(targetJsonObject.get(key).toString());
                    String targetType = targetObject.getString("type");
                    if (!type.equals(targetType)) {
                        change.append("参数key=").append(key).append("类型发生变化，从").append(type).append("改变为")
                                .append(targetType).append(";");
                    }
                    // 如果都是对象
                    else if ("object".equals(type)) {
                        JsonCompareResult compare = compare(valueObject.toJSONString(), targetObject.toJSONString());
                        merge(defect,change,extra,compare,key);
                    }

                    // 如果都是数组
                    else if ("array".equals(type)) {
                        JsonCompareResult compare = compare(valueObject.getJSONObject("items").toJSONString(), targetObject.getJSONObject("items").toJSONString());
                        merge(defect,change,extra,compare,key);
                    }
                }
            }

            for (Map.Entry entry : targetJsonObject.entrySet()) {
                if (jsonObject.get(entry.getKey()) == null) {
                    JSONObject valueObject = JSONObject.parseObject(entry.getValue().toString());
                    String type = valueObject.getString("type");
                    extra.append("增加参数key=").append(entry.getKey()).append(",类型type=").append(type);
                }
            }
        }

        result.setChange(change.toString());
        result.setDefect(defect.toString());
        result.setExtra(extra.toString());
        return result;
    }

    private static void merge(StringBuilder defect, StringBuilder change, StringBuilder extra, JsonCompareResult compare, String key) {
        if(StringUtils.isNotBlank(compare.getChange())){
            change.append("对象key=").append(key).append("部分数据类型发生变化(").append(compare.getChange()).append(");");
        }
        if(StringUtils.isNotBlank(compare.getDefect())){
            defect.append("对象key=").append(key).append("部分数据被移除(").append(compare.getDefect()).append(");");
        }
        if(StringUtils.isNotBlank(compare.getExtra())){
            extra.append("对象key=").append(key).append("存在新增字段(").append(compare.getExtra()).append(");");
        }
    }


    public static void main(String[] args) {
        String origin = "{\n" +
                "    \"type\":\"object\",\n" +
                "    \"description\":\"对象设计模板\",\n" +
                "    \"required\":[\n" +
                "        \"sql\",\n" +
                "        \"pageNo\",\n" +
                "        \"pageSize\",\n" +
                "        \"test\"\n" +
                "    ],\n" +
                "    \"title\":\"入参\",\n" +
                "    \"categoryId\":\"007dcc72c4000000\",\n" +
                "    \"properties\":{\n" +
                "        \"sql\":{\n" +
                "            \"type\":\"string\",\n" +
                "            \"title\":\"sql语句\",\n" +
                "            \"categoryId\":\"007dcc72c4000000\"\n" +
                "        },\n" +
                "        \"pageNo\":{\n" +
                "            \"type\":\"integer\",\n" +
                "            \"title\":\"页码\"\n" +
                "        },\n" +
                "        \"pageSize\":{\n" +
                "            \"type\":\"integer\",\n" +
                "            \"title\":\"显示数量\"\n" +
                "        },\n" +
                "        \"test\":{\n" +
                "           \"type\":\"string\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        String target = "{\n" +
                "    \"type\":\"object\",\n" +
                "    \"description\":\"对象设计模板\",\n" +
                "    \"required\":[\n" +
                "        \"sql\",\n" +
                "        \"pageNo\",\n" +
                "        \"pageSize\",\n" +
                "        \"tokenId\"\n" +
                "    ],\n" +
                "    \"title\":\"入参\",\n" +
                "    \"categoryId\":\"007dcc72c4000000\",\n" +
                "    \"properties\":{\n" +
                "        \"sql\":{\n" +
                "            \"type\":\"string\",\n" +
                "            \"title\":\"sql\",\n" +
                "            \"categoryId\":\"007dcc72c4000000\"\n" +
                "        },\n" +
                "        \"pageNo\":{\n" +
                "            \"type\":\"number\",\n" +
                "            \"title\":\"页码\",\n" +
                "            \"description\":\"大于1的整数，可以为空\"\n" +
                "        },\n" +
                "        \"pageSize\":{\n" +
                "            \"type\":\"number\",\n" +
                "            \"title\":\"每页数量\",\n" +
                "            \"description\":\"大于0的整数，可以为空\"\n" +
                "        },\n" +
                "        \"tokenId\":{\n" +
                "            \"type\":\"string\",\n" +
                "            \"title\":\"token\",\n" +
                "            \"categoryId\":\"007dcc72c4000000\"\n" +
                "        }\n" +
                "    }\n" +
                "}";


        String origin2 = "{\"type\":\"object\",\"description\":\"对象设计模板\",\"required\":[\"code\",\"msg\",\"data\"],\"title\":\"出参\",\"categoryId\":\"007dcc72c4000000\",\"properties\":{\"code\":{\"type\":\"integer\",\"title\":\"返回码\"},\"msg\":{\"type\":\"string\",\"title\":\"说明\",\"categoryId\":\"007dcc72c4000000\"},\"data\":{\"type\":\"object\",\"title\":\"数据\",\"properties\":{\"head\":{\"type\":\"array\",\"items\":{\"type\":\"object\",\"title\":\"\",\"properties\":{\"dataIndex\":{\"type\":\"string\",\"title\":\"数据索引\",\"categoryId\":\"007dcc72c4000000\"},\"title\":{\"type\":\"string\",\"title\":\"标题\",\"categoryId\":\"007dcc72c4000000\",\"description\":\"\"}},\"categoryId\":\"007dcc72c4000000\",\"required\":[\"dataIndex\",\"title\"]},\"title\":\"头信息\"},\"rows\":{\"type\":\"array\",\"items\":{\"type\":\"object\",\"properties\":{},\"description\":\"这是一个map\"},\"title\":\"行数据\",\"description\":\"\"}},\"required\":[\"head\",\"rows\"]}}}";
        String target2 = "{\"type\":\"object\",\"description\":\"对象设计模板\",\"required\":[\"code\",\"message\",\"data\"],\"title\":\"出参\",\"categoryId\":\"007dcc72c4000000\",\"properties\":{\"code\":{\"type\":\"string\",\"title\":\"编码\",\"categoryId\":\"007dcc72c4000000\"},\"message\":{\"type\":\"string\",\"title\":\"消息\",\"categoryId\":\"007dcc72c4000000\"},\"data\":{\"type\":\"object\",\"title\":\"数据\",\"properties\":{\"head\":{\"type\":\"array\",\"items\":{\"type\":\"string\"},\"title\":\"头\"},\"rows\":{\"type\":\"object\",\"title\":\"行数据\",\"properties\":{}}},\"required\":[\"head\",\"rows\"]}}}";


        JsonCompareResult compare = JsonSchemaUtil.compare(origin2, target2);
        System.out.println(JSON.toJSONString(compare));
    }
}
