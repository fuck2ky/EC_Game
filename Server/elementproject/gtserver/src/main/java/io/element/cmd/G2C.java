package io.element.cmd;

import io.element.player.BasePlayer;
import io.element.protobuf.GlobalProto;
import io.element.protobuf.LoginProto;
import io.element.protobuf.LoginProto.QUICK_MATCH_STATUS;
import io.element.util.HandlerUtil;

public class G2C {

	public static void Cmd_Resp_Login( BasePlayer p, boolean successed )
	{		
		LoginProto.G2C_Login.Builder builder = LoginProto.G2C_Login.newBuilder();
		LoginProto.G2C_Login msg = builder.setUser( p.GetPlayerName() )
										  .setResult( successed ? 1 : 0 )
										  .build();
		
	 	GlobalProto.MessageStream sendOb = (GlobalProto.MessageStream) HandlerUtil.CREATE_G2C_GLOBALMESSAGE(LoginProto.G2C_MSGTYPE.G2C_RESP_LOGIN_ATTEMPT, msg.toByteString());
	 	if( p != null )
	 		p.sendMessage( sendOb );
	}
	
	public static void Cmd_Resp_QuickMatch( BasePlayer p, QUICK_MATCH_STATUS status )
	{
		LoginProto.G2C_QuickMatch.Builder builder = LoginProto.G2C_QuickMatch.newBuilder();
		LoginProto.G2C_QuickMatch msg = builder.setResult(status).build();
		
		GlobalProto.MessageStream sendOb = (GlobalProto.MessageStream) HandlerUtil.CREATE_G2C_GLOBALMESSAGE(LoginProto.G2C_MSGTYPE.G2C_RESP_QUICK_MATCH_ATTEMPT, msg.toByteString());
		if( p != null )
			p.sendMessage( sendOb );
	}
	
}
