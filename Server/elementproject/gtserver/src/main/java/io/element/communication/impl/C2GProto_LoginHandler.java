package io.element.communication.impl;

import io.element.cmd.G2C;
import io.element.communication.ProtoMsgHandler;
import io.element.gtserver.App;
import io.element.gtserver.GT_Managers;
import io.element.gtserver.GT_MatchManager;
import io.element.gtserver.GT_PlayerManager;
import io.element.player.BasePlayer;
import io.element.protobuf.LoginProto;
import io.element.protobuf.GlobalProto.MESSAGE;
import io.element.protobuf.LoginProto.C2GByteStream;
import io.element.protobuf.LoginProto.ROOM_TYPE;
import io.element.reflect.Handler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

public class C2GProto_LoginHandler extends ProtoMsgHandler<LoginProto.C2G_MSGTYPE, LoginProto.C2GByteStream> {

	private static final C2GProto_LoginHandler single = new C2GProto_LoginHandler();  
	 
	private C2GProto_LoginHandler() { m_globalType = MESSAGE.MESSAGE_LOGIN_C2G; }  
	    
	public static C2GProto_LoginHandler getInstance() {  
        return single;  
	}  
	
	{
		RegisterProtoMsgCmd();
	}
	
	@Override
	public void RegisterProtoMsgCmd() {
		super.RegisterProtoMsgCmd();
	
		PROTOMSG_REGISTER_HANDLEREX(LoginProto.C2G_MSGTYPE.C2G_REQUEST_LOGIN_ATTEMPT, 		LoginProto.C2G_Login.class, 		"CallBack_C2G_Resp_Login");
		PROTOMSG_REGISTER_HANDLEREX(LoginProto.C2G_MSGTYPE.C2G_REQUEST_QUICKMATCH_ATTEMPT, 	LoginProto.C2G_QuickMatch.class,	"CallBack_C2G_Resp_QuickMatch");
	}
	
	@Override
	public boolean HandlerMsg(ChannelHandlerContext channel, C2GByteStream param) {
		
		LoginProto.C2G_MSGTYPE type = param.getType();
		Handler handler = m_handlers.get(type);
		if( handler == null || !handler.isVaild())
			return false;
		
		// logic call -- 逻辑处理
		Handler tempHandler = null;
		Object re = null;
		try {
			 tempHandler = (Handler) handler.clone();	
			 tempHandler.SetObject(this);
			 tempHandler.SetParam(param,channel.channel());
			 re = tempHandler.invoke();
		} catch (CloneNotSupportedException e) {
			App.LOGGER.error( e.toString() );
		}
			
		// logger call -- 日志处理
		handler = m_log_handlers.get(type);
		if(handler == null || !handler.isVaild())
			return true;
			
		try {
			tempHandler = (Handler) handler; 
			tempHandler.SetObject(this);
			tempHandler.SetParam(re, channel.channel());
			tempHandler.invoke();		
		} catch (Exception e) {
			
		}
		
		return true;
	}
				
	//***********************************************
	// 
	// 协议调用函数，逻辑处理函数
	// 
	//***********************************************
	
	// call back -- C2G_REQUEST_LOGIN_ATTEMPT
	public Object CallBack_C2G_Resp_Login(C2GByteStream msg, SocketChannel channel) throws CloneNotSupportedException, Exception
	{		
		// 反序列化
		LoginProto.C2G_Login cmd = VAR_CHECK_CMD_FIX(msg.getRequestData(), msg.getType());					
		
		GT_PlayerManager mgr = GT_Managers.getPlayerManager();
		boolean logSuccess = true;
		if( mgr.GetPlayer( channel.toString() ) != null )				// 判断账号，密码匹配, 重复登陆判断
			logSuccess = false;
		
		// 创建玩家
		BasePlayer p = null;
		if( logSuccess )
			p = mgr.CreateLoginPlayer(cmd.getUser(), new ClientSession(channel));
		else 
			p = mgr.GetPlayer( channel.toString() );
			
		// 返回登陆状态
		G2C.Cmd_Resp_Login(p, logSuccess);
		
		return cmd;
	}
	
	// call back -- C2G_REQUEST_QUICKMATCH_ATTEMPT
	public Object CallBack_C2G_Resp_QuickMatch(C2GByteStream msg, SocketChannel channel) throws CloneNotSupportedException, Exception
	{
		// 反序列化
		LoginProto.C2G_QuickMatch cmd = VAR_CHECK_CMD_FIX(msg.getRequestData(), msg.getType());					

		// 获取玩家
		GT_PlayerManager pMgr = GT_Managers.getPlayerManager();
		BasePlayer p = pMgr.GetPlayer( channel.toString() );
		if( p == null )
			return null;
		
		// 放置到匹配队列中
		GT_MatchManager mMgr = GT_Managers.getMatchManager();
		mMgr.AddMarch(p, ROOM_TYPE.LOGIN_ROOMTYPE_SIMPLE_2V2);
		
		// 返回匹配状态
		G2C.Cmd_Resp_QuickMatch(p, LoginProto.QUICK_MATCH_STATUS.MATCH_RUNING);
		
		return cmd;
	}
	
	//***********************************************
	//
	// 协议调用函数，logger处理函数
	// 
	//***********************************************
	
	public void CallBack_C2G_Resp_Login_Logger(Object msg, SocketChannel channel)
	{
		LoginProto.C2G_Login cmd = (LoginProto.C2G_Login) msg;
		
		App.LOGGER.info( String.format("client-- 玩家登陆成功 --gate server，玩家名称 = %s", cmd.getUser()));
	}
	
	public void CallBack_C2G_Resp_QuickMatch_Logger(Object msg, SocketChannel channel)
	{
		// 获取玩家
		GT_PlayerManager pMgr = GT_Managers.getPlayerManager();
		BasePlayer p = pMgr.GetPlayer(channel.toString());
		
		App.LOGGER.info( String.format("client-- 玩家开始匹配 --gate server，玩家名称 = %s", p.GetPlayerName()));
	}
	
	
}
