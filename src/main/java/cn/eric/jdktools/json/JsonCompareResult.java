package cn.eric.jdktools.json;

/**
 * Company: ClickPaaS
 *
 * @version 1.0.0
 * @description: jsonschema比较返回数据封装
 * @author: 钱旭
 * @date: 2022-02-21 16:15
 **/
public class JsonCompareResult {

    private String defect;
    private String change;
    private String extra;

    public String getDefect() {
        return defect;
    }

    public void setDefect(String defect) {
        this.defect = defect;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
