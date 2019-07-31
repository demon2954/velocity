package zone.excem.velocity.test;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import zone.excem.bean.EntityProperty;
import zone.excem.db.EntityMaker;
import zone.excem.util.StringUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {

	static String packageName = "com.kingdee.liby";// 类包
	static String templateDir = "\\src\\main\\resources\\template\\";
	static String sourcePath = System.getProperty("user.dir") + templateDir;
	static String resultDir = "\\src\\main\\java\\out";
	static String targetPath = System.getProperty("user.dir") + resultDir + "\\" + packageName.replace(".", "\\");

	public static void main(String[] args) throws Exception {
		String tableName = "liby_employee";
		makeJava(tableName);
		makeXml(tableName);
	}

	public static void makeJava(String tableName) throws IOException {
		// 实体名
		String domainName = StringUtil.underlineToCamel(tableName, true);
		List<EntityProperty> entityPropertys = EntityMaker.makeEntityProperty(tableName);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Service.vm", "service/" + domainName + "Service.java");
		map.put("ServiceImpl.vm", "service/impl/" + domainName + "ServiceImpl.java");
		map.put("Facade.vm", "facade/" + domainName + "Facade.java");
		map.put("FacadeImpl.vm", "facade/impl/" + domainName + "FacadeImpl.java");
		map.put("Mapper.java.vm", "dao/" + domainName + "Mapper.java");
		map.put("Entity.vm", "entity/" + domainName + ".java");

		Properties pro = new Properties();
		pro.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		pro.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		pro.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, sourcePath);
		VelocityEngine ve = new VelocityEngine(pro);

		for (String templateFile : map.keySet()) {
			String targetFile = (String) map.get(templateFile);

			VelocityContext context = new VelocityContext();
			context.put("domainName", domainName);
			context.put("packageName", packageName);
			context.put("entityPropertys", entityPropertys);

			Template t = ve.getTemplate(templateFile, "UTF-8");

			File file = new File(targetPath, targetFile);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream outStream = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
			BufferedWriter sw = new BufferedWriter(writer);
			t.merge(context, sw);
			sw.flush();
			sw.close();
			outStream.close();
			System.out.println("成功生成Java文件:" + (targetPath + targetFile).replaceAll("/", "\\\\"));
		}
	}

	private static void makeXml(String tableName) throws IOException {
		// 实体名
		String domainName = StringUtil.underlineToCamel(tableName, true);
		
		List<EntityProperty> entityPropertys = EntityMaker.makeEntityProperty(tableName);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Mapper.xml.vm", "mapper/" + domainName + "Mapper.xml");

		Properties pro = new Properties();
		pro.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		pro.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		pro.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, sourcePath);
		VelocityEngine ve = new VelocityEngine(pro);

		for (String templateFile : map.keySet()) {
			String targetFile = (String) map.get(templateFile);

			VelocityContext context = new VelocityContext();
			context.put("tableName", tableName);
			context.put("domainName", domainName);
			context.put("packageName", packageName);
			context.put("entityPropertys", entityPropertys);
			context.put("idJavaName", entityPropertys.get(0).getJavaName());
			context.put("idDbName", entityPropertys.get(0).getDbName());
			context.put("idDbType", entityPropertys.get(0).getDbType());
			context.put("idJavaType", entityPropertys.get(0).getJavaLongType());
			Template t = ve.getTemplate(templateFile, "UTF-8");

			File file = new File(targetPath, targetFile);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream outStream = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
			BufferedWriter sw = new BufferedWriter(writer);
			t.merge(context, sw);
			sw.flush();
			sw.close();
			outStream.close();
			System.out.println("成功生成Xml文件:" + (targetPath + targetFile).replaceAll("/", "\\\\"));
		}
	
	}
}