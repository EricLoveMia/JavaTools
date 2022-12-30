package cn.eric.jdktools.chart;

import org.icepear.echarts.Bar;
import org.icepear.echarts.Line;
import org.icepear.echarts.Option;
import org.icepear.echarts.Pie;
import org.icepear.echarts.charts.line.LineSeries;
import org.icepear.echarts.charts.pie.PieSeries;
import org.icepear.echarts.components.coord.cartesian.CategoryAxis;
import org.icepear.echarts.components.coord.cartesian.ValueAxis;
import org.icepear.echarts.components.legend.Legend;
import org.icepear.echarts.components.title.Title;
import org.icepear.echarts.components.tooltip.Tooltip;
import org.icepear.echarts.origin.util.SeriesOption;
import org.icepear.echarts.render.Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @description: 来源： https://juejin.cn/post/7182410481867423803
 * @author: eric
 * @date: 2022-12-30 16:54
 **/
public class EchartsData {

    private static final String htmlHome = "html/echarts/";

    public static void main(String[] args) {

        getBar();

        getLine1();

        getLine2();

        getPie();

    }

    /**
     * 柱状图
     * 使用 {@link Bar} 构建
     */
    static void getBar() {

        // All methods in ECharts Java supports method chaining
        Bar bar = new Bar()
                .setLegend()
                .setTooltip("item")
                .addXAxis(new String[]{"Matcha Latte", "Milk Tea", "Cheese Cocoa", "Walnut Brownie"})
                .addYAxis()
                .addSeries("2020", new Number[]{43.3, 83.1, 86.4, 72.4})
                .addSeries("2021", new Number[]{85.8, 73.4, 65.2, 53.9})
                .addSeries("2022", new Number[]{93.7, 55.1, 82.5, 39.1});
        Engine engine = new Engine();
        // The render method will generate our EChart into a HTML file saved locally in the current directory.
        // The name of the HTML can also be set by the first parameter of the function.
        engine.render(htmlHome + "demo-bar.html", bar);

        // json 格式数据
        System.out.println(engine.renderJsonOption(bar));

    }

    /**
     * 折线图
     * ECharts中，一切图表皆Option，使用 {@link Line} 构建
     */
    static void getLine1() {

        // All methods in ECharts Java supports method chaining
        Line line = new Line()
                .setLegend()
                .setTooltip("item")
                .addXAxis(new String[]{"Matcha Latte", "Milk Tea", "Cheese Cocoa", "Walnut Brownie"})
                .addYAxis()
                .addSeries("2020", new Number[]{43.3, 83.1, 86.4, 72.4})
                .addSeries("2021", new Number[]{85.8, 73.4, 65.2, 53.9})
                .addSeries("2022", new Number[]{93.7, 55.1, 82.5, 39.1});
        Engine engine = new Engine();
        // The render method will generate our EChart into a HTML file saved locally in the current directory.
        // The name of the HTML can also be set by the first parameter of the function.
        engine.render(htmlHome + "demo-line1.html", line);

        // json 格式数据
        System.out.println(engine.renderJsonOption(line));

    }


    /**
     * 折线图
     * ECharts中，一切图表皆Option，使用 {@link Option} 构建
     */
    static void getLine2() {

        Option option = new Option();
        option.setLegend(new Legend().setData(new String[]{"2020", "2021", "2022"}));
        option.setTooltip(new Tooltip().setTrigger("item"));
        option.setXAxis(new CategoryAxis().setData(new String[]{"Matcha Latte", "Milk Tea", "Cheese Cocoa", "Walnut Brownie"}));
        option.setYAxis(new ValueAxis().setType("value"));
        LineSeries lineSeries1 = new LineSeries().setName("2020").setData(new Number[]{43.3, 83.1, 86.4, 72.4});
        LineSeries lineSeries2 = new LineSeries().setName("2021").setData(new Number[]{85.8, 73.4, 65.2, 53.9});
        LineSeries lineSeries3 = new LineSeries().setName("2022").setData(new Number[]{93.7, 55.1, 82.5, 39.1});
        option.setSeries(new SeriesOption[]{lineSeries1, lineSeries2, lineSeries3});
        Engine engine = new Engine();
        System.out.println(engine.renderJsonOption(option));
        engine.render( htmlHome + "demo-line2.html", option);

    }

    /**
     * 饼图
     * 使用 {@link Pie} 构建
     * ref <a href="https://echarts.apache.org/examples/zh/editor.html?c=pie-simple">...</a>
     */
    static void getPie() {

        List<PieData> list = new ArrayList<>();

        list.add(new PieData("直接访问", 335));
        list.add(new PieData("邮件营销", 310));
        list.add(new PieData("联盟广告", 234));
        list.add(new PieData("视频广告", 135));
        list.add(new PieData("搜索引擎", 1548));

        PieSeries pieSeries = new PieSeries()
                .setName("访问来源")
                .setType("pie")
                .setRadius("55%")
                .setData(list);

        Pie pie = new Pie()
                .setTitle(new Title().setText("Referer of a Website").setSubtext("Fake Data").setLeft("center"))
                .setLegend(new Legend().setOrient("vertical").setLeft("left"))
                .setTooltip("item")
                .addSeries(pieSeries);
        Engine engine = new Engine();
        // The render method will generate our EChart into a HTML file saved locally in the current directory.
        // The name of the HTML can also be set by the first parameter of the function.
        engine.render(htmlHome + "demo-pie.html", pie);

        // json 格式数据
        System.out.println(engine.renderJsonOption(pie));

    }


    /**
     * 饼图数据
     */
    static class PieData {
        private String name;
        private Integer value;

        public PieData(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

    }

}
