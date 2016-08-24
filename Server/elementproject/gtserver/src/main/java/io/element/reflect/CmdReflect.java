package io.element.reflect;

public interface CmdReflect<T> {

	void REFLECT_REGISTER_TYPE_CMD(T type, String str_method);
	
	Handler ReflectHandler(T type);
	
	boolean HandlerMsg(T type);
}
