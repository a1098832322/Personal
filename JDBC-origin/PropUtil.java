package com.zl.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 配置文件工具类
 * 
 * @author 郑龙
 *
 */
public class PropUtil {
	private static Properties prop = new Properties();
	static {
		try {
			prop.load(new InputStreamReader(PropUtil.class.getResourceAsStream("/jdbc.properties"), "utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Properties getInstance() {
		return prop;
	}

}
