package jp.arcanum.wscmd.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

public class TailSocketServlet extends WebSocketServlet{

	@Override
	protected StreamInbound createWebSocketInbound(String subprotocol, HttpServletRequest request) {
		TwitterMessageInbound tmi = new TwitterMessageInbound(subprotocol, request);
		return tmi;
	}

}
