package io.element.Character;



public interface Character {
	
	public enum CHARACTER
	{
		CHARACTER_DEFAULT(0),				// 普通人
		
		CHARACTER_LI_XIAO_YAO(10);
		
		private int value = 0;
		
	    private CHARACTER(int value) {    
	        this.value = value;
	    }
	    
	    public static CHARACTER valueOf(int value) {    //    手写的从int到enum的转换函数
	        switch (value) {
	        case 1:
	            return CHARACTER_LI_XIAO_YAO;
	        default:
	            return null;
	        }
	    }
	    
	    public int value() {
	        return this.value;
	    }
		
	}
	
	CHARACTER CHARACTER();
	
}
