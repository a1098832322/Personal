package com.dsw.iot.utils;


import com.dsw.iot.config.StringConstant;
import com.dsw.iot.utils.exception.BizException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.eclipse.sisu.Nullable;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 郑龙
 * @Date:2018-10-08 14:39
 * @Description:一个提供将数据导出为Excel格式数据表的工具类<br> <B>方法说明：</B><br>
 * <li>使用builder方法构造，按需要选择输出参数。其中大标题和主体数据不能为null</li>
 * <li>需要重写writeData()方法，该方法主要用于将需要的数据填充进表单中，所以需要自定义实现。</li>
 * <li>在build()后使用getWb()获得表格文件，可将此文件写出或流式传输用于下载</li>
 * <li>详情可参考PoiTest类中的方法</li>
 */
public class DataExporter {
    /**
     * 工作簿
     */
    private SXSSFWorkbook wb;

    /**
     * 获得工作簿
     *
     * @return SXSSFWorkbook 构造完成的xlsx工作簿数据
     */
    public SXSSFWorkbook getWb() {
        return wb;
    }

    /**
     * 使用构造者模式
     *
     * @param builder
     */
    public DataExporter(Builder builder) {
        this.wb = builder.getWb();
    }

    /**
     * 将表格文件转换为InputStream输出下载
     *
     * @param workbook 数据表
     * @return
     */
    public static InputStream excelToInputStream(SXSSFWorkbook workbook) {
        InputStream in = null;
        try {
            //临时
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //创建临时文件
            workbook.write(out);
            byte[] bookByteAry = out.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }


    /**
     * 内部构造类
     */
    public static abstract class Builder {
        /**
         * 工作簿
         */
        private SXSSFWorkbook wb = null;

        /**
         * 主sheet
         */
        private SXSSFSheet sheet = null;

        /**
         * 表格大标题（通常用于表示下载于哪个版块：eg.接处警问题、强制措施问题  等）
         */
        private String mTitle;

        /**
         * 二级小标题头（用于导出web显示的表格头：eg. 案件编号、警情编号  等）
         */
        private String[] colTitles = new String[20];

        /**
         * 默认有多少行
         */
        private int rows = 3;

        /**
         * 默认有多少列
         */
        private int columns = 20;

        /**
         * 显示的数据List
         */
        private List mData = new ArrayList<>();

        /**
         * 需要修改的sheet的索引号
         */
        private int sheetIndex = 0;

        /**
         * 需要修改的sheet名字
         */
        private String sheetName = "";

        /**********************************
         *             getter             *
         * ********************************/

        public SXSSFWorkbook getWb() {
            return this.wb;
        }

        public SXSSFSheet getSheet() {
            return this.sheet;
        }

        public List getData() {
            return this.mData;
        }

        public int getRows() {
            return this.rows;
        }

        public int getColumns() {
            return this.columns;
        }


        /**
         * 默认初始化方法
         *
         * @return Builder
         */
        public Builder initSheet() {
            //设置允许在创建时访问的工作簿表格大小，默认为100，
            // 超过100时从最小的开始，row将不可访问，当设置为-1时表示在创建表时允许所有row可读
            wb = new SXSSFWorkbook(-1);
            return this;
        }

        /**
         * 使用一份已有的数据表初始化表单构造器
         *
         * @param wb 源工作簿
         * @return Builder
         */
        public Builder initSheet(SXSSFWorkbook wb) {
            this.wb = wb;
            return this;
        }

        /**
         * 传参初始化一步到位的方法
         *
         * @param title       主表标题
         * @param data        数据源
         * @param columnTitle 二级标题
         * @return Builder
         */
        public Builder initSheet(@NotNull(message = "导出表格所属板块大标题不能为null") String title
                , @NotNull(message = "数据数组不能为null值") List data
                , String... columnTitle) throws BizException {
            wb = new SXSSFWorkbook(-1);
            this.setTitle(title);
            this.setData(data);
            this.setColumnTitles(columnTitle);
            return this;
        }

        /**
         * 将表格文件写入多个sheet时调用此方法构造。
         *
         * @param sxssfWorkbook 源表单数据
         * @param title         主表标题
         * @param data          数据源
         * @param columnTitle   二级标题
         * @return Builder
         */
        public Builder initSheet(@NotNull(message = "要写入的xls表单") SXSSFWorkbook sxssfWorkbook
                , @NotNull(message = "导出表格所属板块大标题不能为null") String title
                , @NotNull(message = "数据数组不能为null值") List data
                , String... columnTitle) throws BizException {
            wb = sxssfWorkbook;
            this.setTitle(title);
            this.setData(data);
            this.setColumnTitles(columnTitle);
            return this;
        }

        /**
         * 设置生成的数据长度(生成xx行数据)
         *
         * @param size
         * @return
         */
        public Builder setRowSize(int size) {
            this.rows = size;
            return this;
        }

        /**
         * 设置生成的数据列数量
         *
         * @param size
         * @return
         */
        public Builder setColumnSize(int size) {
            this.columns = size;
            return this;
        }

        /**
         * 设置单元格枚举值(针对单个单元格)
         *
         * @param firstRow 开始行
         * @param endRow   结束行
         * @param firstCol 开始列
         * @param endCol   结束列
         * @param values   枚举值
         * @return Builder
         */
        public Builder setEnumCellValues(int firstRow, int endRow, int firstCol,
                                         int endCol, String... values) {
            DataValidationHelper dvHelper = sheet.getDataValidationHelper();
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow
                    , endRow, firstCol, endCol);
            DataValidationConstraint dvConstraint = dvHelper
                    .createExplicitListConstraint(values);
            DataValidation validation = dvHelper.createValidation(
                    dvConstraint, addressList);
            //处理Excel兼容性问题
            if (validation instanceof XSSFDataValidation) {
                validation.setSuppressDropDownArrow(true);
                validation.setShowErrorBox(true);
            } else {
                validation.setSuppressDropDownArrow(false);
            }
            sheet.addValidationData(validation);

            return this;
        }


        /**
         * 设置大标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) throws BizException {
            if (StringUtils.isNotBlank(title)) {
                this.mTitle = title;
            } else {
                throw new BizException(StringConstant.MAIN_TITLE_CANT_BE_NULL);
            }
            return this;
        }


        /**
         * 设置sheet的名字
         *
         * @param sheetName sheet名
         * @return
         */
        public Builder setSheetName(@Nullable String sheetName) {
            this.sheetName = "";//初始化
            if (StringUtils.isNotBlank(sheetName)) {
                //获取当前sheet索引
                int index = 0;
                try {
                    index = wb.getNumberOfSheets();
                    this.sheetIndex = index;
                } catch (Exception e) {
                    this.sheetIndex = 0;
                }

                this.sheetName = sheetName;
            }
            return this;
        }

        /**
         * 设置指定索引的sheet的名字
         *
         * @param index     sheet索引
         * @param sheetName sheet名
         * @return
         */
        public Builder setSheetName(int index, String sheetName) {
            this.sheetName = "";//初始化
            //设置sheet名
            if (StringUtils.isNotBlank(sheetName)) {
                this.sheetIndex = index;
                this.sheetName = sheetName;
            }

            return this;
        }

        /**
         * 设置二级小标题头
         *
         * @param colTitles
         * @return
         */
        public Builder setColumnTitles(String... colTitles) throws BizException {
            if (Optional.ofNullable(colTitles).isPresent()) {
                this.colTitles = colTitles;
                this.setColumnSize(colTitles.length);
            } else {
                //throw new BizException(StringConstant.SUB_TITLE_CANT_BE_NULL);
            }
            return this;
        }

        /**
         * 设置具体的数据
         *
         * @param data
         * @return
         */
        public Builder setData(List data) throws BizException {
            if (Optional.ofNullable(data).isPresent()) {
                this.mData = data;
                this.setRowSize(data.size());
            } else {
                throw new BizException(StringConstant.DATA_ARRAY_CANT_BE_NULL);
            }
            return this;
        }

        /**
         * 设置单元格样式
         *
         * @return
         */
        private Builder setStyle() {
            //创建一个sheet
            sheet = wb.createSheet();
            // 创建单元格样式
            CellStyle style = wb.createCellStyle();
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER); //文字水平居中
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//文字垂直居中
            style.setBorderBottom(BorderStyle.THIN); //底边框加黑
            style.setBorderLeft(BorderStyle.THIN);  //左边框加黑
            style.setBorderRight(BorderStyle.THIN); // 右边框加黑
            style.setBorderTop(BorderStyle.THIN); //上边框加黑


            //如果大标题不为空，则合并并居中显示文档标题
            if (StringUtils.isNotBlank(mTitle)) {
                //为单元格添加背景样式
                for (int i = 0; i < rows + 2; i++) { //需要xx+2行表格,因为包含了两级标题
                    Row row = sheet.createRow(i); //创建行
                    for (int j = 0; j < columns; j++) {//需要xx列
                        row.createCell(j).setCellStyle(style);
                    }
                }

                //合并单元格显示标题
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns - 1));
                //填入数据
                SXSSFRow row = sheet.getRow(0); //获取第一行
                row.getCell(0).setCellValue(mTitle); //在第一行中创建并写入title
            } else {
                //为单元格添加背景样式
                for (int i = 0; i < rows + 1; i++) {
                    Row row = sheet.createRow(i); //创建行
                    for (int j = 0; j < columns; j++) {//需要xx列
                        row.createCell(j).setCellStyle(style);
                    }
                }
            }

            //获取第二行，为每一列添加字段
            SXSSFRow row1 = sheet.getRow(1);

            //填充二级小标题头,若为空则显示默认列名
            for (int i = 0; i < columns; i++) {
                final int index = i;
                row1.getCell(i).setCellValue(Optional.ofNullable(colTitles).map(title -> colTitles[index]).orElse("Column " + String.valueOf(i)));
            }

            //获得总列数  设置列宽为25个字符宽度
            int coloumNum = sheet.getRow(1).getPhysicalNumberOfCells();
            for (int i = 0; i < coloumNum; i++)
                sheet.setColumnWidth(i, 25 * 256);

            return this;
        }

        /**
         * 修改sheet名(如果有)
         */
        private void changeSheetName() {
            if (StringUtils.isNotBlank(this.sheetName)) {
                this.wb.setSheetName(this.sheetIndex, this.sheetName);
            }
        }

        /**
         * 使用抽象方法，方便后续自己自由填充表格数据
         */
        public abstract void writeData();

        /**
         * 返回构造出来的对象
         *
         * @return DataExporter
         */
        public DataExporter build() {
            this.setStyle();//绘制表格框架
            this.writeData();//填充表格数据
            this.changeSheetName();//修改sheet名称（如果有）
            return new DataExporter(this);
        }


    }
}
