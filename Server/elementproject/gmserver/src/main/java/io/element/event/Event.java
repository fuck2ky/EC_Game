package io.element.event;


public interface Event<T> {


	public T type();
	
	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public boolean apply();
	
	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public void init();
	
	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public Object GetBuffer();
}
