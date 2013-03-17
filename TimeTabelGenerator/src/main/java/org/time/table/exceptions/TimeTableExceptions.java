package org.time.table.exceptions;

public class TimeTableExceptions extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TimeTableExceptions(Exception e)
	{
		super(e);
	}
	
	public TimeTableExceptions(String msg)
	{
		super(msg);
	}

}
