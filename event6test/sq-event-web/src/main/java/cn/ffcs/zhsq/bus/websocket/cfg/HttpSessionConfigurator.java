package cn.ffcs.zhsq.bus.websocket.cfg;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * 获取用户HttpSession,注,需要配置默认监听RequestListener,不然,部分浏览器取出来的httpsession为空
 * @author zkongbai
 *
 */
public class HttpSessionConfigurator extends Configurator {
	@Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession=(HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}

