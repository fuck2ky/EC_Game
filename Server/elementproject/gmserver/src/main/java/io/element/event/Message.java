package io.element.event;

public interface Message {
	
	enum MESSGAE_TYPE
	{
		SERVER_SET_FUTURE_TASK(0),		// 0 set the future work to room	
		SERVER_CLEAR_DELAYED_INFO(1);
		
	    private int value = 0;

	    private MESSGAE_TYPE(int value) {    //    必须是private的，否则编译错误
	        this.value = value;
	    }
	    
	    public static MESSGAE_TYPE valueOf(int value) {    //    手写的从int到enum的转换函数
	        switch (value) {
	        case 0:
	            return SERVER_SET_FUTURE_TASK;
	        case 1:
	        	return SERVER_CLEAR_DELAYED_INFO;
	        default:
	            return null;
	        }
	    }
	    
	    public int value() {
	        return this.value;
	    }
	}
	
	long GetSender();
	
	void SetSender(long id);
	
	long GetReceiver();
	
	void SetReceiver(long id);
	
	Object GetParam();
	
	void SetParam(Object obj);
	
	MESSGAE_TYPE GetType();
	
	void SetType(MESSGAE_TYPE type);

}
