package io.element.reflect;

import io.element.gtserver.App;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Handler implements Cloneable   {

	protected Object object;
	
	protected Method method;
	
	protected Object[] params;
	
	public Handler( Method m)
	{
		this.method = m;
	}
	
	public Handler()
	{
		
	}
	
	public void SetObject(Object obj)
	{
		this.object = obj;
	}
	
	public void SetParam(Object... params)
	{
		this.params = new Object[params.length];
		
		for( int i=0; i< Math.min(params.length, 5); ++i )
			this.params[i] = params[i];
	}
		
	public Object invoke()
	{
		try {
			return this.method.invoke(this.object, this.params );
		} catch (IllegalAccessException e) {
			App.LOGGER.error( e.toString() );
		} catch (IllegalArgumentException e) {
			App.LOGGER.error( e.toString() );
		} catch (InvocationTargetException e) {
			App.LOGGER.error("gate-- function error in method:{}", this.method.toString());
		}
		
		return null;
	}
	
	public void invoke(Object param)
	{
		try {
			this.method.invoke(this.object, new Object[]{param} );
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			App.LOGGER.error("gate-- function error in method:{}", this.method.toString());
		}
	}
	
	public Object clone() throws CloneNotSupportedException 
	{
		Handler cloned = (Handler) super.clone(); 
		cloned.method = this.method;
		return cloned;
	}
	
	public boolean isVaild()
	{
		return method != null;
	}
	
}
