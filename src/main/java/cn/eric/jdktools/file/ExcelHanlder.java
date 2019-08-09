package cn.eric.jdktools.file;

import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;
import java.util.Map;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: ExcelHanlder
 * @Description: TODO
 * @company lsj
 * @date 2019/4/24 15:53
 **/
public class ExcelHanlder {

    //默认高度
    private static short DEFAULT_ROW_HEIGHT = 400;
    //默认宽度
    private static int DEFAULT_CELL_WIDTH = 3000;


    /**
     *
     * @param book 工作簿对象，【可选】
     * @param hanlder 自定义类型处理【可选】
     * @param titles 标题
     * @param columns 列名（Map类型处理，自定义可选）
     * @param columnsWidth 宽度
     * @param height 行高
     * @param sheetTitle 表标题
     * @param datas 数据
     * @return
     */
    @SuppressWarnings("all")
    public static XSSFWorkbook exportExcel(XSSFWorkbook book, ExcelTypeHanlder hanlder, String[] titles, String[] columns
            , Integer[] columnsWidth, Short height, String sheetTitle, List datas){

        if(book==null){
            book = new XSSFWorkbook();
        }

        int size = DEFAULT_CELL_WIDTH;

        //列大小
        if(columnsWidth!=null&&columnsWidth.length==1){
            size = columnsWidth[0];
        }
        if(height==null){
            height = DEFAULT_ROW_HEIGHT;
        }
        XSSFSheet sheet = book.createSheet(sheetTitle);
        int rowindex = 0;
        XSSFRow firstrow = sheet.createRow(rowindex);
        rowindex++;
        sheet.setDefaultColumnWidth(size);
        firstrow.setHeight(height);

        XSSFFont font = book.createFont();
        font.setBold(true);
        XSSFCellStyle cellstyle = book.createCellStyle();
        cellstyle.setFont(font);
        cellstyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //标题
        if(titles!=null){
            int index = 0;
            for (String title : titles) {
                XSSFCell cell = firstrow.createCell(index);
                cell.setCellStyle(cellstyle);
                cell.setCellValue(title);
                //列宽度设置
                if(columnsWidth==null||columnsWidth.length==0||columnsWidth.length==1){
                    sheet.setColumnWidth(cell.getColumnIndex(), size);
                }else{
                    if((columnsWidth.length-1)>=index){
                        sheet.setColumnWidth(cell.getColumnIndex(), columnsWidth[index]==null?size:columnsWidth[index]);
                    }else{
                        sheet.setColumnWidth(cell.getColumnIndex(), size);
                    }
                }
                index++;
            }
        }
        if(datas==null){
            return book;
        }

        //写入数据
        for (Object data : datas) {

            //map 类型处理
            if(data instanceof Map){
                Map<String,Object> map = (Map<String, Object>) data;
                XSSFRow row = sheet.createRow(rowindex);
                int i = 0;
                for (String column : columns) {
                    XSSFCell cell = row.createCell(i);
                    Object val = map.get(column);
                    if(hanlder!=null&&val==null){
                        Object temp = hanlder.dataNullHander(column,map);
                        cell.setCellValue(temp!=null?temp.toString():"");
                    }else{
                        cell.setCellValue(val!=null?val.toString():"");
                    }
                    i++;
                }
                row.setHeight(DEFAULT_ROW_HEIGHT);
                rowindex++;
            }else{
                //其他处理
                if(hanlder!=null){
                    Object obj = data;
                    XSSFRow row = sheet.createRow(rowindex);
                    hanlder.typeHanlder(data, row);
                    rowindex++;
                }
            }
        }
        return book;
    }


    /**
     *
     * @param book 工作簿对象，【可选】
     * @param titles 标题
     * @param columns 列名（Map类型处理，自定义可选）
     * @param sheetTitle 表标题
     * @param datas 数据
     * @return
     */
    public static XSSFWorkbook exportExcel(XSSFWorkbook book, String[] titles, String[] columns, String sheetTitle, List<Map<String,String>> datas){
        return exportExcel(book, null, titles, columns,null,null, sheetTitle, datas);
    }

    /**
     * @param titles 标题
     * @param columns 列名（Map类型处理，自定义可选）
     * @param sheetTitle 表标题
     * @param datas 数据
     * @return
     */
    @SuppressWarnings("all")
    public static XSSFWorkbook exportExcel(String[] titles, String[] columns, String sheetTitle, List<Map<String,String>> datas, ExcelTypeHanlder hanlder){
        return exportExcel(null, hanlder, titles, columns,null,null, sheetTitle, datas);
    }

    public static XSSFWorkbook exportExcel(String[] titles, String[] columns, String sheetTitle, List<Map<String,String>> datas){
        return exportExcel(null, null, titles, columns,null,null, sheetTitle, datas);
    }

    //自定义处理对象回调
    public static abstract class ExcelTypeHanlder<T>{
        //类型处理
        public void typeHanlder(T data, XSSFRow row){

        }

        //空数据处理
        public Object dataNullHander(String column,T obj){
            return null;
        }
    }


}