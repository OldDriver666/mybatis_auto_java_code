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
public class ReduceCode {
	public static void main(String[] args) throws Exception {
		String path = "E:/ruixun-master/ruixun-opensip-api/src/main/java/com/vanrui/reqvo/";
		String domain = "";
		recursiveFiles(path, domain);
	}

	/**
	 * Xiwi 遍历文件/文件夹 - 函数 [String]path 文件路径
	 * 
	 * @throws Exception
	 */
	public static void recursiveFiles(String path, String domain) throws Exception {
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
				recursiveFiles(f.getAbsolutePath(), domain);
			} else if (f.isFile()) {// 判断是否为 文件
				String filePath = f.getAbsolutePath();
				if (filePath.endsWith(domain + "Mapper.xml")) {
					reduceMapperXml(filePath);
				}else if(filePath.endsWith(domain + "Mapper.java")){
					reduceMapperJava(filePath, domain);
				}else if(filePath.endsWith(domain + "Example.java")){
					
				}else if(filePath.endsWith(domain + ".java")){
					reduceGetSet(filePath);
				}else{
					
				}
			} else {
				System.out.print("未知错误文件");
			}
		}
	}

	//简化Entity类
	public static void reduceGetSet(String path) throws Exception {

		StringBuffer bs = new StringBuffer();
		String str = null;
		FileReader fr = new FileReader(path);
		String comment = "";
		BufferedReader br = new BufferedReader(fr);

		while ((str = br.readLine()) != null) {
			
			if (str.indexOf("package com") != -1) {
				bs.append(str);
				bs.append("\r");
				bs.append("import javax.persistence.GeneratedValue;\r");
				bs.append("import javax.persistence.GenerationType;\r");
				bs.append("import javax.persistence.Id;\r");
				bs.append("import lombok.Data;\r\r");
				bs.append("import io.swagger.annotations.ApiModel;\r");
				bs.append("import io.swagger.annotations.ApiParam;\r");
				continue;
			}


			if (str.indexOf("*") != -1 && str.indexOf("/**") == -1 && str.indexOf("*/") == -1) {
				comment = str.substring(str.indexOf("*") +1 ).trim();
			}
			if (str.indexOf("private") != -1 && StringUtils.isNotEmpty(comment)) {
				if (str.indexOf("private Integer id;") != -1 || str.indexOf("private Long id;") != -1) {
					bs.append("    @Id\r");
					bs.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\r");
				}
				bs.append("    @ApiParam(value=\"").append(comment).append("\")\r");
				comment="";
				bs.append(str);
				continue;

			}

			if (str.indexOf("public class") != -1) {
				bs.append("@Data\r");
				bs.append("@ApiModel\r");
			}
			if (str.indexOf("public") != -1 && str.indexOf("class") == -1) {
				break;
			}

			if (str.indexOf("ApiModel") != -1) {
				path="D:/111.java";
				break;
			}

			bs.append(str);
			bs.append("\r");
		}
		bs.append("}");

		FileWriter writer;

		writer = new FileWriter(path);
		writer.write(bs.toString());
		writer.flush();
		writer.close();

		br.close();
		fr.close();

		System.out.println(bs);
	}
	//简化Mapper.java类
	public static void reduceMapperJava(String path, String domain) throws Exception {
		
		StringBuffer bs = new StringBuffer();
		String str = null;
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		
		while ((str = br.readLine()) != null) {
			if (str.indexOf("package") != -1 && str.indexOf("com") != -1) {
				bs.append(str);
				bs.append("\r");
				bs.append("import com.vanrui.utils.MyMapper;\r");
				bs.append("import org.apache.ibatis.annotations.Mapper;\r");
				continue;
			}
			if (str.indexOf("interface") != -1) {
				bs.append("@Mapper\r");
				bs.append(str.substring(0, str.indexOf("{")-1));
				bs.append(" extends MyMapper<");
				bs.append(domain);
				bs.append("> {");
				bs.append("\r");
				break;
			}
			bs.append(str);
			bs.append("\r");
		}
		bs.append("}");
		
		FileWriter writer;
		
		writer = new FileWriter(path);
		writer.write(bs.toString());
		writer.flush();
		writer.close();
		
		br.close();
		fr.close();
		
		System.out.println(bs);
	}
	
	//简化Mapper.xml类
	public static void reduceMapperXml(String path) throws Exception {
		
		StringBuffer bs = new StringBuffer();
		String str = null;
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		
		while ((str = br.readLine()) != null) {
			bs.append(str);
			bs.append("\r");
			if (str.indexOf("</resultMap>") != -1) {
				break;
			}
		}
		bs.append("</mapper>");
		
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
