package io.element.gtserver;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import com.google.protobuf.CodedOutputStream;


import junit.framework.TestCase;

public class EncodeTest extends TestCase {

	
	
	
	
	
	public void testEncode() throws InterruptedException, IOException
	{
		
		ByteBuf out = Unpooled.buffer(1);  
        CodedOutputStream headerOut =
                CodedOutputStream.newInstance(new ByteBufOutputStream(out));
        
        headerOut.writeRawVarint32(16);
        headerOut.flush();
        
        int headerLen = CodedOutputStream.computeRawVarint32Size(16);
        byte[] buffer = out.array();
        
		if( headerLen > 0 && buffer != null )
		{
			
		}
        

        
	}
	
}
