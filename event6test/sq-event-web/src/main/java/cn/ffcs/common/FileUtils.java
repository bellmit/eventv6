package cn.ffcs.common;

import java.io.*;


public class FileUtils {

	public static final String ENCODE_UTF_8 = "utf-8";
	public static final String ENCODE_UNICODE = "unicode";
	public static final String ENCODE_8859_1 = "8859_1";

	/**
	 * 读取数据 字节
	 * 
	 * @param inSream
	 * @param encode
	 * @return
	 * @throws IOException
	 */
	public static String readData(InputStream inStream, String encode)
			throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return new String(data, encode);
	}

	/**
	 * 读取数据 字符 （高效率）
	 * 
	 * @param path
	 * @param encode
	 * @return data
	 * @throws IOException
	 */
	public static String readData(String path, String encode)
			throws IOException {
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader reader = new InputStreamReader(fis, encode);
		BufferedReader br = new BufferedReader(reader);

		StringBuffer sb = new StringBuffer();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str).append("\n");
		}
		br.close();
		reader.close();
		fis.close();
		return sb.toString();
	}

	/**
	 * 写数据 字符
	 * 
	 * @param path 路径
	 * @param encode 编码 FileUtils.ENCODE_UTF_8
	 * @param data 数据
	 * @param append 是否追加
	 * @throws IOException
	 */
	public static void writeData(String path, String data, String encode,
			boolean append) throws IOException {
		createFile(path);
		FileOutputStream fos = new FileOutputStream(path, append);
		OutputStreamWriter osw = new OutputStreamWriter(fos, encode);
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(data);
		bw.flush();
		bw.close();
		osw.close();
		fos.close();
	}

	/**
	 * 写数据 字符 （创建或覆盖）
	 * 
	 * @param path
	 * @param encode FileUtils.ENCODE_UTF_8
	 * @param data
	 * @throws IOException
	 */
	public static void writeData(String path, String data, String encode)
			throws IOException {
		writeData(path, data, encode, false);
	}

	/**
	 * 创建文件
	 * @param path
	 * @throws IOException
	 */
	public static void createFile(String path) throws IOException {
		File file = new File(path);

		if (!file.exists()) {
			File dir = new File(getDirPath(path));
			if(!dir.exists())
				dir.mkdirs();
			file.createNewFile();
		}
	}
	/**
	 * 删除文件夹
	 * 
	 * @param path
	 * @throws IOException
	 */
	public static void clearFolder(String folder) {
		File file = new File(folder);
		if (file.exists()) {
			deleteFile(file);
		}
	}

	private static void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}
	/**
	 * 获得文件的所属的文件夹
	 * @param path
	 * @return
	 */
	public static String getDirPath(String path){
		int index = 0;
		index = path.lastIndexOf("/");
		if(index==-1){
			index = path.lastIndexOf("\\");
		}
		return path.substring(0,index+1);
	}

	/**
	 * 获得文件后缀名
	 * @param path
	 * @return
	 */
	public static String getFileExtension(String path){
		int index = 0;
		index = path.lastIndexOf(".");
		if(index==-1){
			return null;
		}
		return path.substring(index,path.length());
	}
	
	/**
	 * 获得文件名
	 * @param path
	 * @return
	 */
	public static String getFileName(String path){
		int index = 0;
		index = path.lastIndexOf("/");
		if(index==-1){
			index = path.lastIndexOf("\\");
		}
		return path.substring(index+1,path.length());
	}
	
	
	/**
	 * 合并 文件路径
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static String combineFileName(String path1, String path2){
		path1=path1.replace('\\', '/');
		path2=path2.replace('\\', '/');
		if(!path1.endsWith("/"))
				path1+="/";		
		if(path2.startsWith("/"))
			path2=path2.substring(1);		
		return path1.concat(path2);
		
	}
	
	/**
	 * 获取当前类路径
	 * 
	 * @param 当前类
	 * @return
	 */
	public static String getClassPath(Object obj) {

		return obj.getClass().getResource("/").getPath();
	}
	
	/**
     * 复制文件
     * @param sourceFile 原文件路径
     * @param targetFile 目标文件路径
     * @throws IOException
     */
    public static void copyFile(String sourceFile, String targetFile) throws IOException {
    	FileUtils.createFile(targetFile);
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        	
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
    

	public static void main(String[] args) {
		/*
		 * TODO Auto-generated method stub try { long startTime =
		 * System.nanoTime(); readData("c:/123.txt",FileUtils.ENCODE_UTF_8);
		 * long consumingTime = System.nanoTime() - startTime; //消耗時間
		 * System.out.println(consumingTime/1000/1000.0+"微秒"); } catch
		 * (FileNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException ioe) {
		 * ioe.printStackTrace(); }
		 */

		/* 写入数据 测试
		try {
			writeData("c:/244/33/123.txt", "123fff看看看看123", FileUtils.ENCODE_UTF_8, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/* 创建文件 测试
		try {
			createFile("d:/ddd/text.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		System.out.println(combineFileName("c:/244/33","44/d.txt"));

	}


	/**
	 * 从输入流中获取字节数组
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}
}
