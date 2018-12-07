package com.dsw.iot.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author:郑龙
 * @Date:2018-12-03 16:42
 * @Description:
 */
public class DataReader {
    /**
     * 解析后的结果集
     */
    @Getter
    private List resultList;

    public DataReader(Builder builder) {
        this.resultList = builder.getResultList();
    }

    /**
     * 构造器
     */
    public static abstract class Builder {
        /**
         * 工作簿
         */
        @Getter
        private Workbook wb;

        /**
         * 包含数据集的工作簿sheet
         */
        @Getter
        private Sheet sheet;

        /**
         * 结果集
         */
        @Getter
        private List resultList = null;

        /**
         * 行数
         */
        @Getter
        private int rows = 0;

        /**
         * 列数
         */
        @Getter
        private int columns = 0;

        /**
         * sheet索引
         */
        private int sheetIndex = 0;

        /**
         * 传入工作簿
         *
         * @param wb
         * @return
         */
        public Builder setWorkBook(Workbook wb) {
            this.wb = wb;
            return this;
        }

        /**
         * 设置结果集
         *
         * @param resultList
         * @return
         */
        public Builder setResultList(List<?> resultList) {
            this.resultList = resultList;
            return this;
        }

        /**
         * 设置需要读取的目标工作簿sheet索引数
         *
         * @param index
         * @return
         */
        public Builder setReadSheetIndex(int index) {
            this.sheetIndex = index;
            return this;
        }

        /**
         * 将解析结果添加进结果集
         *
         * @param o
         * @return
         */
        public Builder add(Object o) {
            this.resultList.add(o);
            return this;
        }

        /**
         * 自定义的解析方法
         */
        public abstract void readData() throws Exception;

        /**
         * 构造方法
         *
         * @return
         */
        public DataReader build() throws Exception {
            splitWorkBook();//得到关于表格的一些基础参数
            readData();//自定义解析表格
            return new DataReader(this);
        }

        /**
         * 拆分读取工作簿
         *
         * @throws NullPointerException
         */
        private void splitWorkBook() throws NullPointerException {
            if (this.wb == null) {
                throw new NullPointerException("工作簿为空!");
            }

            if (this.resultList == null) {
                throw new NullPointerException("需要引用的结果集为空!");
            }

            this.sheet = wb.getSheetAt(sheetIndex);
            this.rows = sheet.getLastRowNum();
            this.columns = sheet.getRow(0).getLastCellNum();
        }
    }
}
