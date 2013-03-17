package org.time.table.generator;

import java.util.List;


public class Classroom {

	private String classId=null; //specific
	private String classType=null; // added for condition of different subj in case on 11 and 12th - science stream, commerce stream
	private Integer classStd=null; // standard
	private Room classRoomLoc= null;// room of student group
	private List<TimeTable> timeTables=null;//timetableOptions
	private String timeTableFormat=null;// string to get timeTableFormat
	
	public Classroom(String classId)
	{
		this.classId=classId;
	}
	public Classroom(String classId, String std,String type,String room,String timetable)
	{
		this.classId=classId;
		this.classStd=Integer.parseInt(std);
		this.classType=type;
		this.classRoomLoc=new Room(Integer.parseInt(room));
		this.timeTableFormat=timetable;
	}
	



	/**
	 * @return the timeTables
	 */
	public List<TimeTable> getTimeTables() {
		return timeTables;
	}
	/**
	 * @param timeTables the timeTables to set
	 */
	public void setTimeTables(List<TimeTable> timeTables) {
		this.timeTables = timeTables;
	}
	/**
	 * @return the timeTableFormat
	 */
	public String getTimeTableFormat() {
		return timeTableFormat;
	}
	/**
	 * @param timeTableFormat the timeTableFormat to set
	 */
	public void setTimeTableFormat(String timeTableFormat) {
		this.timeTableFormat = timeTableFormat;
	}
	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}




	/**
	 * @param classId the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}




	/**
	 * @return the classType
	 */
	public String getClassType() {
		return classType;
	}




	/**
	 * @param classType the classType to set
	 */
	public void setClassType(String classType) {
		this.classType = classType;
	}




	/**
	 * @return the classStd
	 */
	public Integer getClassStd() {
		return classStd;
	}




	/**
	 * @param classStd the classStd to set
	 */
	public void setClassStd(Integer classStd) {
		this.classStd = classStd;
	}




	/**
	 * @return the classRoomLoc
	 */
	public Room getClassRoomLoc() {
		return classRoomLoc;
	}




	/**
	 * @param classRoomLoc the classRoomLoc to set
	 */
	public void setClassRoomLoc(Room classRoomLoc) {
		this.classRoomLoc = classRoomLoc;
	}




	@Override			
	   public boolean equals(Object obj) {
	       if (obj == null)
	           return false;
	       if (!(obj instanceof Classroom))
	           return false;
	       Classroom sg = (Classroom) obj;
	       if (sg.classId.equals(this.classId))
	    	   return true;
	       else
	    	   return false;
	   } 
	
	
}
