package io.element.state;



public interface StateMachine<T,S> {

	/**
	 * @return Returns the unique id associated with this game object.
	 */
	public void Update();
	
	public S PopCurrentState();
	
	public void ChangeState(S newState);
	
	public void ChangeState();
	
	public void SetOwner(T owner);
	
	public T GetOwner();
	
	
}
