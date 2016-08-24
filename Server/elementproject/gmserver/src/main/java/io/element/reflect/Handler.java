package io.element.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Handler implements Cloneable {

	protected Object object;

	protected Method method;

	protected Object param;

	public Handler(Method m) {
		this.method = m;
	}

	public Handler() {

	}

	public void SetObject(Object obj) {
		this.object = obj;
	}

	public void SetParam(Object param) {
		this.param = param;
	}

	public void invoke() {
		try {
			this.method.invoke(this.object, new Object[] { this.param });
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void invoke(Object param) {
		try {
			this.method.invoke(this.object, new Object[] { param });
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		Handler cloned = (Handler) super.clone();
		cloned.method = this.method;
		return cloned;
	}

}
