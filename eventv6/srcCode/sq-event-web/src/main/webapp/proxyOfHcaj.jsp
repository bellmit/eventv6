<%@page session="false"%>
<%@page import="java.net.*,java.util.Properties,java.lang.System,java.io.*" %>
<%!
String[] serverUrls = {
	
  //"http://t0.tianditu.com/vec_c/wmts" ,
  
  "http://10.2.2.84:6080/arcgis/rest/services"
  //http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/MapServer

};
%>
<%

try {
  String reqUrl = request.getQueryString();
  
   //System.out.println(reqUrl.equals("http://tile0.chinaonmap.com/vec_c/wmts?request=GetCapabilities&service=WMTS&version=1.0.0"));
   //if(reqUrl.equals("http://tile0.chinaonmap.com/vec_c/wmts?request=GetCapabilities&service=WMTS&version=1.0.0")){
  	System.out.println("bbbbb:"  + reqUrl);
  	//reqUrl = "http://localhost:8888/";
  //}
  boolean allowed = false;
  String token = null;
  for(String surl : serverUrls) {
    String[] stokens = surl.split("\\s*,\\s*");
 //if(reqUrl.toLowerCase().contains(stokens[0].toLowerCase())) {
    if(true) {
      allowed = true;
      if(stokens.length >= 2 && stokens[1].length() > 0)
        token = stokens[1];
      break;
    }
  }
  if(!allowed) {
    response.setStatus(403);
    return;
  }
  if(token != null) {
    reqUrl = reqUrl + (reqUrl.indexOf("?") > -1 ? "&" : "?") + "token=" + token;
  }
  URL url = new URL(reqUrl);
  System.out.println("bbb:"  + reqUrl);
	HttpURLConnection con = (HttpURLConnection)url.openConnection();
	
	Properties prop = System.getProperties();
	/*
	prop.setProperty("http.proxyHost", "192.168.13.19");
	prop.setProperty("http.proxyPort", "7777");
	prop.setProperty("http.proxyUser", "liush");
	prop.setProperty("http.proxyPassword", "liushi805347332");
	*/
	con.setDoOutput(true);
	con.setRequestMethod(request.getMethod());
	if(request.getContentType() != null) {
	  con.setRequestProperty("Content-Type", request.getContentType());
    }
	int clength = request.getContentLength();
	if(clength > 0) {
		con.setDoInput(true);
		InputStream istream = request.getInputStream();
		OutputStream os = con.getOutputStream();
		final int length = 5000;
	  byte[] bytes = new byte[length];
	  int bytesRead = 0;
	  while ((bytesRead = istream.read(bytes, 0, length)) > 0) {
	    os.write(bytes, 0, bytesRead);
	  }
	}
	out.clear();
  out = pageContext.pushBody();
	OutputStream ostream = response.getOutputStream();
	response.setContentType(con.getContentType());
	InputStream in = con.getInputStream();
	final int length = 5000;
  byte[] bytes = new byte[length];
  int bytesRead = 0;
  while ((bytesRead = in.read(bytes, 0, length)) > 0) {
    ostream.write(bytes, 0, bytesRead);
  }
} catch(Exception e) {
    e.printStackTrace();
	response.setStatus(500);
}
%>
