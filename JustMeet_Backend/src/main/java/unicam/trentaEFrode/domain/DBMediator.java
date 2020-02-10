package unicam.trentaEFrode.domain;

import java.sql.SQLException;

public class DBMediator {
	
	private Object obj;
	private static DBMediator instance;
	
	private DBMediator() {
	}
	
	
	public static DBMediator getInstance() throws SQLException {
		if(instance==null) instance=new DBMediator();
		return instance;
	}
	
	public synchronized Object getObject() throws InterruptedException {
		while(obj==null) {
			wait();
		}
		this.notifyAll();
		return this.obj;
		
	}
	
	public synchronized void setObject(Object obj) throws InterruptedException {
		while(this.obj!=null)
			wait();
		this.notifyAll();
		this.obj=obj;
	}
	
	public void resetObject() {
		this.obj=null;
	}
}
