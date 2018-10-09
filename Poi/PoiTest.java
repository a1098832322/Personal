package com.dsw.zfjd.poi;

import com.dsw.zfjd.utils.DataExporter;
import com.dsw.zfjd.utils.exception.BizException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther:郑龙
 * @Date:2018-10-08 12:00
 * @Description:将数据excel化导出的demo测试类
 */
@SpringBootTest
public class PoiTest {

    @Test
    public void test() {
        //构造测试数据
        List<Person> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Person p = new Person("张三" + i, i % 2 == 0 ? "女" : "男", i * 5 + "");
            data.add(p);
        }

        //表头
        String[] titles = {"姓名", "性别", "年龄", "", null};


        XSSFWorkbook xwb = null;
        try {
            xwb = new DataExporter.Builder() {
                @Override
                public void writeData() {
                    for (int i = 2; i < getRows() + 2; i++) {//因为含有2级标题，所以这里要从2开始循环计数，取值时需要-2,需注意！
                        XSSFRow dataRow = getSheet().getRow(i); //获取第i行

                        //将数据解析进相应的表格单元中
                        Person p = data.get(i - 2);
                        dataRow.getCell(0).setCellValue(p.getName());
                        dataRow.getCell(1).setCellValue(p.getSex());
                        dataRow.getCell(2).setCellValue(p.getYear());
                    }
                }
            }.initSheet("标题", data, titles).build().getWb();

            //保存为文件
            try {
                FileOutputStream fos = new FileOutputStream("E:\\test.xlsx");
                xwb.write(fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (BizException e) {
            e.printStackTrace();
        }


    }

    /**
     * 测试人员类
     */
    class Person {
        private String name, sex, year;

        public Person(String name, String sex, String year) {
            this.name = name;
            this.sex = sex;
            this.year = year;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }
}
