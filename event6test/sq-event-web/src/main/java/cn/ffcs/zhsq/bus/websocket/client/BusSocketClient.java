package cn.ffcs.zhsq.bus.websocket.client;

import java.io.IOException;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import cn.ffcs.zhsq.bus.websocket.cfg.HttpSessionConfigurator;
import cn.ffcs.zhsq.bus.websocket.pool.BusPool;

@ServerEndpoint(value="/websocket/init/bus",configurator=HttpSessionConfigurator.class)
public class BusSocketClient {

	@OnOpen
    public void onOpen(Session session,EndpointConfig config) {
        //HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		BusPool.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        
    }

    @OnError
    public void onError(Throwable throwable) {
        
    }

    @OnClose
    public void onClose(Session session) {
    	BusPool.remove(session);
    }
}
