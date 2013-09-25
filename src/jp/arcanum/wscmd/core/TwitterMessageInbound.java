package jp.arcanum.wscmd.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import javax.servlet.http.HttpServletRequest;

import jp.arcanum.wscmd.util.Util;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

public class TwitterMessageInbound extends MessageInbound{

	private WsOutbound _outbound;

	public TwitterMessageInbound(String subprotocol, HttpServletRequest request){
	}

	@Override
	public void onOpen(WsOutbound outbound) {
		_outbound = outbound;
	}

    @Override
    public void onClose(int status) {
    	System.out.println("onClosed : " + status);
    	// 補足：この時点ではもう、_outbound.writeXXXXX(...)はできないYO!
    }

	@Override
	protected void onBinaryMessage(ByteBuffer bytebuff) throws IOException {

	}

	// 実行中のプロセス
	private Process _process;

	@Override
	protected void onTextMessage(CharBuffer charbuff) throws IOException {

		// 前のプロセスが生きていたら殺す
		if(_process!=null){
			_process.destroy();
		}

		// 入力されたコマンドを画面に表示するために送る
		String command = charbuff.toString().trim();
		String sendcmd = command;
		if(sendcmd.equals("")){
			sendcmd = "<br>";
		}
		_outbound.writeTextMessage(CharBuffer.wrap(sendcmd));

		try {


			// windowsの場合はコマンドラインから実行
			// TODO Unixの場合とか後で対応
			command = "cmd /c " + command;

			// コマンドを実行
			ProcessBuilder r = new ProcessBuilder(command.split(" "));
			r.redirectErrorStream(true);
			_process = r.start();

		} catch (Exception e) {
			String exmessage = Util.getExceptionMess(e);
			CharBuffer mess = CharBuffer.wrap(exmessage);
			_outbound.writeTextMessage(mess);
			return;
		}

		BufferedReader reader = null;
		try{

			// TODO OSにより、コマンドの文字コードが異なる。以下はWindowsの場合
			reader = new BufferedReader(new InputStreamReader(_process.getInputStream(),"Shift_JIS"));
			String line = null;
			while ((line = reader.readLine()) != null) {

				CharBuffer mess = CharBuffer.wrap(line);
				_outbound.writeTextMessage(mess);
			}

		}
		catch(IOException ioe) {
			String exmessage = Util.getExceptionMess(ioe);
			CharBuffer mess = CharBuffer.wrap(exmessage);
			_outbound.writeTextMessage(mess);
			return;
		}
		finally{
			reader.close();
		}

		if(_process!=null){
			_process.destroy();
		}

	}

}
