package com.zzg.mybatis.generator.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


/**
 * 引入lombok, 删除get,set方法
 * 
 * @author admin
 *
 */
public class AddSwaggerCode {
	public static void main(String[] args) throws Exception {
		String path = "E:\\GITLAB\\ruixun-opensip\\ruixun-opensip-api\\src\\main\\java\\com\\vanrui\\model\\";
		recursiveFiles(path);
	}

	/**
	 * Xiwi 遍历文件/文件夹 - 函数 [String]path 文件路径
	 * 
	 * @throws Exception
	 */
	public static void recursiveFiles(String path) throws Exception {
		File file = new File(path);// 创建 File对象
		File files[] = file.listFiles();// 取 文件/文件夹
		// 对象为空 直接返回
		if (files == null) {
			return;
		}
		// 目录下文件
		if (files.length == 0) {
			System.out.println(path + "该文件夹下没有文件");
		}
		// 存在文件 遍历 判断
		for (File f : files) {
			// 判断是否为 文件夹
			if (f.isDirectory()) {// 为 文件夹继续遍历
				recursiveFiles(f.getAbsolutePath());
			} else if (f.isFile()) {// 判断是否为 文件
				String filePath = f.getAbsolutePath();
				if (filePath.endsWith("Example.java")) {

				}else if(filePath.endsWith(".java")){
					addSwaggerCode(filePath);
				}else{
					
				}
			} else {
				System.out.print("未知错误文件");
			}
		}
	}

	//简化Entity类
	public static void addSwaggerCode(String path) throws Exception {

		StringBuffer bs = new StringBuffer();
		String str = null;
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);

		String comment = "";
		while ((str = br.readLine()) != null) {
			if (str.indexOf("package com") != -1) {
				bs.append(str);
				bs.append("\r");
				bs.append("import io.swagger.annotations.ApiModel;\r");
				bs.append("import io.swagger.annotations.ApiParam;\r");
				continue;
			}
			if (str.indexOf("*") != -1 && str.indexOf("/**") == -1 && str.indexOf("*/") == -1) {
				comment = str.substring(str.indexOf("*") +1 ).trim();
			}
			if (str.indexOf("private") != -1 && StringUtils.isNotEmpty(comment)) {
				bs.append("    @ApiParam(value=\"").append(comment).append("\")\r");
				bs.append(str);
				comment="";
				continue;
			}
			if (str.indexOf("public class") != -1) {
				bs.append("@ApiModel\r");
			}
			if (str.indexOf("ApiModel") != -1) {
				path="D:/111.java";
				break;
			}

			bs.append(str);
			bs.append("\r");
		}

		FileWriter writer;

		writer = new FileWriter(path);
		writer.write(bs.toString());
		writer.flush();
		writer.close();

		br.close();
		fr.close();

		System.out.println(bs);
	}

}
