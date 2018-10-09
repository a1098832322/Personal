package com.dsw.zfjd.utils;

import com.dsw.zfjd.config.StringConstant;
import com.dsw.zfjd.utils.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Auther:郑龙
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
    private XSSFWorkbook wb;

    public XSSFWorkbook getWb() {
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
     * 内部构造类
     */
    public static abstract class Builder {
        /**
         * 工作簿
         */
        private XSSFWorkbook wb = null;

        /**
         * 主sheet
         */
        private XSSFSheet sheet = null;

        /**
         * 表格大标题（通常用于表示下载于哪个版块：eg.接处警问题、强制措施问题  等）
         */
        private String mTitle;

        /**
         * 二级小标题头（用于导出web显示的表格头：eg. 案件编号、警情编号  等）
         */
        private String[] colTitles = new String[20];

        /**
         * 有多少行
         */
        private int rows = 3;

        /**
         * 有多少列
         */
        private int columns = 20;

        /**
         * 显示的数据List
         */
        private List mData = new ArrayList<>();

        /**********************************
         *             getter             *
         * ********************************/

        public XSSFWorkbook getWb() {
            return this.wb;
        }

        public XSSFSheet getSheet() {
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
         * @return
         */
        public Builder initSheet() {
            wb = new XSSFWorkbook();
            return this;
        }

        /**
         * 传参初始化一步到位的方法
         *
         * @param title
         * @param data
         * @param columnTitle
         * @return
         */
        public Builder initSheet(@NotBlank(message = "导出表格所属板块大标题不能为null") String title
                , @NotBlank(message = "数据数组不能为null值") List data
                , String... columnTitle) throws BizException {
            wb = new XSSFWorkbook();
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
            XSSFCellStyle style = wb.createCellStyle();
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
                XSSFRow row = sheet.getRow(0); //获取第一行
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
            XSSFRow row1 = sheet.getRow(1);

            //填充二级小标题头,若为空则显示默认列名
            for (int i = 0; i < columns; i++) {
                final int index = i;
                row1.getCell(i).setCellValue(Optional.ofNullable(colTitles).map(title -> colTitles[index]).orElse("Column " + String.valueOf(i)));
            }

            return this;
        }


        /**
         * 使用抽象方法，方便后续自己自由填充表格数据
         */
        public abstract void writeData();

        /**
         * 返回构造出来的对象
         *
         * @return
         */
        public DataExporter build() {
            this.setStyle();//绘制表格框架
            this.writeData();//填充表格数据
            return new DataExporter(this);
        }


    }
}
