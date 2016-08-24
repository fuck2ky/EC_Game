package io.element.communication;


import io.element.gtserver.App;
import io.element.protobuf.GlobalProto.MESSAGE;
import io.element.reflect.Handler;
import io.element.util.HandlerUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;


import com.google.protobuf.ByteString;

public abstract class ProtoMsgHandler<T,E extends Object> {
	
	// 逻辑处理函数
	protected HashMap<T, Handler> m_handlers = new HashMap<T, Handler>();
	// 反序列化函数
	protected HashMap<T, Handler> m_prases = new HashMap<T, Handler>();
	// 逻辑处理函数的log函数
	protected HashMap<T, Handler> m_log_handlers = new HashMap<T, Handler>();
	
	protected MESSAGE m_globalType;
	
	public void RegisterProtoMsgCmd() { m_handlers.clear(); }

	public void PROTOMSG_REGISTER_HANDLER(T type, String str_method )
	{ 
		Method method = null;
		try {
			method = HandlerUtil.getMethod(this.getClass().getName() , str_method);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(method == null)
			return;
		
		Handler handler = new Handler(method);
		m_handlers.put(type, handler);
	}
	
	public void PROTOMSG_REGISTER_HANDLER(T type, String str_method, Class<?> info1, Class<?> info2)
	{		
		Method method = null;
		try {
			if( info2 == null && info1 != null )
				method = HandlerUtil.getMethod(this.getClass().getName(), str_method, info1 );
			else if( ( info2 != null && info1 != null ) )
				method = HandlerUtil.getMethod(this.getClass().getName(), str_method, info1, info2);
			
		} catch (Exception e) {
			App.LOGGER.error( "注册函数失败，函数名为%s", str_method);
		}
		
		if(method == null)
			return;
		
		Handler handler = new Handler(method);
		m_handlers.put(type, handler);
	}
	
	public void PROTOMSG_REGISTER_LOG_HANDLER(T type, String str_method, Class<?> info, Class<?> info2)
	{
		Method method = null;
		try {
			method = HandlerUtil.getMethod(this.getClass().getName(), str_method, info, info2 );		
		}catch (Exception e) {

		}
		
		if(method == null)
			return;
		
		Handler handler = new Handler(method);
		m_log_handlers.put(type, handler);
	}
	
	public <V> void PROTOMSG_REGISTER_HANDLEREX(T type, Class<V> struct, String str_method)
	{	
		// get -- 真实调用函数的handler
		Class<?> entityClass = (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		PROTOMSG_REGISTER_HANDLER(type, str_method, entityClass, SocketChannel.class);
		
		// get -- 真实调用函数的log handler
		str_method = str_method + "_Logger";
		PROTOMSG_REGISTER_LOG_HANDLER(type, str_method, Object.class, SocketChannel.class);
		
		// get -- 反序列化的调用函数的handler
		str_method = "parseFrom";
		Method method = null;
		try {
			// ADD PRASE FROM METHOD 
			method = HandlerUtil.getMethod(struct.getName(), str_method, ByteString.class);
		} catch (Exception e) {
			App.LOGGER.error( "注册函数失败，函数名为%s", str_method);
		}
		if(method == null)
			return;		
		Handler handler = new Handler(method);
		m_prases.put(type, handler);
	
	}
	
	protected <X,Y> X VAR_CHECK_CMD_FIX( ByteString buffer, Y type ) throws CloneNotSupportedException, Exception
	{
		Handler handler = m_prases.get(type);
		if( handler == null || !handler.isVaild())
			return null;
		
		Handler tempHandler = null;	
		tempHandler = (Handler) handler.clone();			
	
		tempHandler.SetObject(null);
		tempHandler.SetParam(buffer);
		
		@SuppressWarnings("unchecked")
		X t = (X) tempHandler.invoke();		
		return t;
	}
	
	public abstract boolean HandlerMsg(ChannelHandlerContext channel, E param);

}
