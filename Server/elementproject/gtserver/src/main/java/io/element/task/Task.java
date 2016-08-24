package io.element.task;

import com.google.protobuf.ByteString;

public interface Task<T> {

	public T type();
	
	public boolean apply();
	
	public void init();
	
	public ByteString GetBuffer();
	
	public void SetBuffer(ByteString params);
}
