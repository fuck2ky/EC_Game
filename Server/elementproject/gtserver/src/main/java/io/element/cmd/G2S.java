package io.element.cmd;


public class G2S {

	// 进入房间界面
	public static void Cmd_Req_NewRoom()			
	{
		
	}
	
	// 不进入房间界面，快速开始
	// 1. gate server -- 创建   session room， connect logic server
	// 2. gate server -- send create room msg，logic server 
	// 3. gate server -- send begin game msg
	//
	// 其中步骤1在login handler中完成
	// 当前函数完成2，从1的回调中完成
	// 当前函数返回的channel future，从2的回调完成
	public static void Cmd_Req_NewRoom_Quick()		
	{
		
	}
	
	
}
