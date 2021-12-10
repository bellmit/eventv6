package cn.ffcs.shequ.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class ValidateXml {


    /**
	* validateXML 
	* @param xsdPath 传入验证文件
	* @param xmlData 传入验证内容
	* @return
	*/
	public static boolean validateXML(String xsdPath,String xmlData){
		// 建立schema工厂
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
	
		// 建立验证文档文件对象，利用此文件对象所封装的文件进行schema验证
		File schemaFile = new File(xsdPath);
	
		// 利用schema工厂，接收验证文档文件对象生成Schema对象
		Schema schema = null;
		try {
			schema = schemaFactory.newSchema(schemaFile);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	
		// 通过Schema产生针对于此Schema的验证器，利用schenaFile进行验证
		Validator validator = schema.newValidator();
	
		// 得到验证的数据源
		Source source = new StreamSource(xmlData);
	
		// 开始验证，成功输出success!!!，失败输出fail
		// 参数还可以用文件的String转为的inputstreamnew
		// //ByteArrayInputStream(text.getBytes("UTF-8"));
		try {
			validator.validate(source);
		}catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
