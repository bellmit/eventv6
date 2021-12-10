package cn.ffcs.common;

public class GetFileSuffix {
	
	/**
	 * 获取文件后缀名
	 * @param path
	 */
	public static String getFileSuffixByPath(String path) {
		// 获取文件后缀名并转化为写，用于后续比较
		String fileType = path.substring(path.lastIndexOf(".") + 1,path.length()).toLowerCase();
		
		if ("bmp|jpg|jpeg|png|gif".indexOf(fileType) > -1) {
			return "image";
		} else if ("xls|xlsx|xlsm|xltx".indexOf(fileType) > -1) {
			return "excel";
		} else if("docx|docm|dotx|doc".indexOf(fileType) > -1) {
			return "word";
		} else if("pptx|pptm|ppsx|ppt".indexOf(fileType) > -1) {
			return "ppt";
		} 
		return "other";
	}
}
