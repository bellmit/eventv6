package cn.ffcs.zhsq.util;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:动态改变log4j日志级别、
 *              log4j1.x 当用户没有设置log4j.configuration属性，则首先查找log4j.xml，然后查找log4j.properties
 *              log4j2.x 支持log4j2.xml设置时，查找log4j2.xml
 * @Author: ztc
 * @Date: 2018/9/27 17:39
 */
public class Log4jConfigUtil {

    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    private static Document document;
    private static final String LOG4J_FILE_NAME = "log4j2.xml";

    private enum LogLevel{

        ALL("ALL", Level.ALL),
        FATAL("FATAL",Level.FATAL),
        ERROR("ERROR",Level.ERROR),
        WARN("WARN",Level.WARN),
        INFO("INFO",Level.INFO),
        DEBUG("DEBUG",Level.DEBUG),
        TRACE("TRACE",Level.TRACE),
        OFF("OFF",Level.OFF);

        // 日志级别名称
        String levelInfo;
        // 日志级别
        Level level;

        private LogLevel(String levelInfo, Level level) {
            this.levelInfo = levelInfo;
            this.level = level;
        }
    }
    
    //构建documentBuilder对象，避免每次请求都重复执行这段代码，故写在静态代码块内 Log4jConfigUtil
    static{
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * log4j2 日志层级调整，需要支持log4j2.xml的配置
     * @param loggerName	日志名称
     * @param loggerLevel	日志层级
     * @return
     */
    public Boolean updateLoggerLevel(String loggerName, String loggerLevel) {
    	boolean successFlag = false;
    	
        if (StringUtils.isBlank(loggerName)) {
            throw new IllegalArgumentException("参数：日志名称[loggerName]为空，请检查！");
        }
        
        if(StringUtils.isBlank(loggerLevel)) {
        	throw new IllegalArgumentException("参数：日志级别[loggerLevel]为空，请检查！");
        } else {
        	boolean isLoggerLevelInvalid = true;
        	loggerLevel = loggerLevel.toUpperCase();
        	
        	
        	for(LogLevel logLevel : LogLevel.values()) {
        		if (loggerLevel.toUpperCase().equals(logLevel.levelInfo)) {
        			isLoggerLevelInvalid = false;
                    break;
                }
        	}
        	
        	if(isLoggerLevelInvalid) {
        		throw new IllegalArgumentException("非法的日志级别[" + loggerLevel + "]！");
        	}
        }
    	
    	String filePath = getClass().getClassLoader().getResource(LOG4J_FILE_NAME).getPath();
    	
    	if(StringUtils.isBlank(filePath)) {
        	throw new IllegalArgumentException("没有找到文件：[" + LOG4J_FILE_NAME + "]！");
        }
    	
    	Element loggerElement = null;
    	Map<String, Object> param = new HashMap<String, Object>();
    	NodeList nodeList = null;
    	
        param.put("filePath", filePath);
        
        initDocument(param);
        
    	if("root".equalsIgnoreCase(loggerName)) {
    		nodeList = document.getElementsByTagName("root");
    		
    		if(nodeList != null && nodeList.getLength() == 1) {
    			loggerElement = (Element)nodeList.item(0);
    		}
    	} else {
    		nodeList = document.getElementsByTagName("logger");
            
            if(nodeList != null) {
            	for(int index = 0, len = nodeList.getLength(); index < len; index++) {
            		loggerElement = (Element)nodeList.item(index);
            		
            		if(loggerName.equalsIgnoreCase(loggerElement.getAttribute("name"))) {
            			break;
            		}
            	}
            }
    	}
        
        if(loggerElement != null) {
        	loggerElement.setAttribute("level", loggerLevel);
        	
        	try {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty("encoding", "utf-8");
    			transformer.setOutputProperty("indent", "yes");
    			transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));
    			
    			successFlag = true;
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
        }

        return successFlag;
    }

    /**
     * 根据日志名称获取获取日志级别
     * 特殊说明：root节点的日志级别默认为 log4j.properties 中 log4j.rootLogger 设置的级别
     *          如果 log4j.rootLogger 中未配置，默认加载 log4j.xml 中 root 节点中 level 值
     * @param loggerName 必填
     * */
    public String getLoggerLevelByLoggerName(String loggerName) {

        if (StringUtils.isNotBlank(loggerName)) {
            Logger logger = loggerName.equalsIgnoreCase("ROOT") ? LogManager.getRootLogger() : LogManager.getLogger(loggerName);
            if(null != logger && null != logger.getLevel()){
                return logger.getLevel().toString();
            }

        } else {
            throw new IllegalArgumentException("参数：日志名称[loggerName]为空，请检查！");
        }
        return "";
    }


    /**
     * 解析log4j.xml文件，获取xml文件中配置的日志信息
     * @param param
     *        filePath log4j.xml文件路径，默认获取所运行项目的文件路径
     * @return loggerAttrList
     *         	loggerName           日志名称
     *         	loggerLevel          日志级别
     *         	loggerAppenderRef    日志输出目的地
     *         monitorInterval		 失效，单位为秒
     *
     * */
    public Map<String,Object> getLoggerNameAndLevel(Map<String,Object> param){
    	Map<String, Object> resultMap = new HashMap<String, Object>();

        //节点属性集合
        List<Map<String,Object>> loggerAttrList = new ArrayList<>();
        //指定节点名为 logger 的节点
        NodeList nodeList = null;
        //指定节点名为 logger 的节点
        NodeList rootNodeList = null;
        //节点名称
        String nodeName = "";
        
        if(param.get("filePath") == null || "".equalsIgnoreCase(param.get("filePath").toString().trim()) ) {
        	param.put("filePath", getClass().getClassLoader().getResource(LOG4J_FILE_NAME).getPath());
        }
        
        initDocument(param);

        //返回指定节点名为 logger 的节点及相关配置信息（按照xml文档定义顺序）
        nodeList = document.getElementsByTagName("logger");
        this.getLoggerAttrList(nodeList,loggerAttrList);

        //返回指定节点名为 root 的节点及相关配置信息（按照xml文档定义顺序）
        rootNodeList = document.getElementsByTagName("root");
        this.getLoggerAttrList(rootNodeList,loggerAttrList);

        nodeList = document.getElementsByTagName("configuration");
        Element element = (Element)nodeList.item(0);
        String monitorInterval = element.getAttribute("monitorInterval");
        
        resultMap.put("loggerAttrList", loggerAttrList);
        resultMap.put("monitorInterval", monitorInterval);
        
        return resultMap;
    }

    /**
     * 使用xml文件初始化document
     * @param param
     * 			filePath	xml文件路径
     */
    private void initDocument(Map<String, Object> param) {
    	if(document == null) {
    		String filePath = null;
    		
    		if (null != param.get("filePath") && !"".equals(param.get("filePath").toString())) {
                filePath = param.get("filePath").toString();
            }
    		
            //将给定uri解为xml文档，返回document对象
            if (StringUtils.isNotBlank(filePath)) {
                try {
                    document = documentBuilder.parse(new File(filePath));
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    	}
    }
    
    private void getLoggerAttrList(NodeList nodeList,List<Map<String,Object>> loggerAttrList){
        //节点名称
        String nodeName = "";
        //遍历日志节点
        if (nodeList.getLength() > 0) {
            for (int i = 0,len = nodeList.getLength();i < len; i++) {
                //日志属性信息
                Map<String,Object> resultMap = new HashMap<>();

                Node loggerNode = nodeList.item(i);
                //获取日志节点的属性集合
                NamedNodeMap loggerAttrValMap = loggerNode.getAttributes();
                //日志名称
                String loggerName = "";

                //清空前一个日志属性（若存在）
                //resultMap.clear();
                //获取日志的 name 属性
                if ("root".equalsIgnoreCase(loggerNode.getNodeName())) {
                    loggerName = "root";
                    resultMap.put("loggerName",loggerName);
                } else {
                    for (int m = 0,loggerMapLen = loggerAttrValMap.getLength();m < loggerMapLen;m++) {
                        nodeName = loggerAttrValMap.item(m).getNodeName();
                        if ("name".equalsIgnoreCase(nodeName)) {
                            //属性名--对应日志名称
                            loggerName = loggerAttrValMap.item(m).getNodeValue();
                            resultMap.put("loggerName",loggerName);
                        }
                    }
                }


                //根据日志名获取当前日志级别
                if (StringUtils.isNotBlank(loggerName)) {
                    resultMap.put("loggerLevel",this.getLoggerLevelByLoggerName(loggerName));
                }

                //获取 logger 节点下的所有子节点,当level为空时，默认从xml文件取日志级别
                NodeList childNodeList = loggerNode.getChildNodes();
                //由于标签之间的所有内容都看成是子节点，每个子节点换行定义的格式，直接跳过空白节点
                for (int j = 1,childLen = childNodeList.getLength();j < childLen;j += 2) {
                    Node childNode = childNodeList.item(j);

                    //获取子节点的所有属性集合
                    NamedNodeMap childNodeAttrValMap = childNode.getAttributes();
                    for (int n = 0,loggerMapLen = childNodeAttrValMap.getLength();n < loggerMapLen;n++) {
                        //属性名称
                        nodeName = childNodeAttrValMap.item(n).getNodeName();

                        if ("value".equalsIgnoreCase(nodeName) && (null == resultMap.get("loggerLevel") || "".equals(resultMap.get("loggerLevel").toString()))) {
                            //属性名
                            resultMap.put("loggerLevel",childNodeAttrValMap.item(n).getNodeValue());
                        }

                        //日志输出地址
                        if ("ref".equalsIgnoreCase(nodeName)) {
                            //属性名
                            resultMap.put("loggerAppenderRef",childNodeAttrValMap.item(n).getNodeValue());
                        }
                    }
                }

                //同一个日志的相关属性存放到 loggerAttrList
                loggerAttrList.add(resultMap);
            }
        }
    }

}
