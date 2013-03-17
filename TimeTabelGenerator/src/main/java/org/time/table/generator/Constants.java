package org.time.table.generator;

import java.util.HashMap;
import java.util.Map;

public interface Constants {

	//Days of the week in time table
	public String MON="MONDAY";
	public String TUE="TUESDAY";
	public String WED="WEDNESDAY";
	public String THU="THURSDAY";
	public String FRI="FRIDAY";
	public String SAT="SATURDAY";	
	
	//periods in a day
	public int FIRST_PERIOD =1;
	public int SECOND_PERIOD =2;
	public int THIRD_PERIOD =3;
	public int FOURTH_PERIOD =4;
	public int FIFTH_PERIOD =5;
	public int SIXTH_PERIOD =6;
	public int SEVENTH_PERIOD =7;
	public int EIGHT_PERIOD =8;
	public int LUNCH_BREAK=9;
	public int SHORT_BREAK=10;
	
	public boolean IS_TEMPLATE=true;
	public boolean NOT_TEMPLATE=false;
	
	public String TEACHER="TEACHER";
	public String CLASS="CLASS";
	
	public HashMap<String,Integer>dayPeriodsMap= new HashMap<String,Integer>()
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7782259468351076395L;

		{
			put(MON,8);
			put(TUE,8);
			put(WED,8);
			put(THU,8);
			put(FRI,8);
			put(SAT,7);
		}
	};
	
}
