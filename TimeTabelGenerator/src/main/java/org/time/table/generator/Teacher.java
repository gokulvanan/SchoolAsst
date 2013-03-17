package org.time.table.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teacher {

	private String teacherID=null;// unique Id for each teacher
	private String teacherName=null;// name of the teacher
	private String teacherDepartment=null;// Which deparment teacher is in
	private Integer teacherLevel=null;// Upper limit of std teacher is taking
	private String ownClassId=null;//studentGroup of the class she is class teacher
	private String role=null;//designation for preference
	private Map<Classroom,Map<Subject,Integer>> classSubjPeriodCountMap=null; //maps the classes teacher would take and list of subject taken by her in each class
	private List<TimeTable> timeTables=null;
	private String timetableFormat=null;
	
	public Teacher(String id,String name,String dept,String level,String ownClass,String role,String classSubjPeriodCountMap,String timeTable)
	{
		this.teacherID=id;
		this.teacherName=name;
		this.teacherDepartment=dept;
		this.teacherLevel=Integer.parseInt(level);
		this.ownClassId=ownClass;
		this.role=role;
		initializeMap(classSubjPeriodCountMap);
		timetableFormat=timeTable;
	}
	
	private void initializeMap(String map)
	{
		String[] classes= map.split("#");
		classSubjPeriodCountMap= new HashMap<Classroom, Map<Subject,Integer>>(classes.length);
		for(String cls : classes)
		{
			String clsId=cls.split("@")[0];
			String subjId= cls.split("@")[1].split("~")[0];
			Integer periodCount=Integer.parseInt(cls.split("@")[1].split("~")[1]);
			Classroom clsObj = new Classroom(clsId);
			Map<Subject,Integer> subMapObj=classSubjPeriodCountMap.get(clsObj);
			if(subMapObj!=null)
			{
				subMapObj.put(new Subject(subjId),periodCount);
			}else
			{
				subMapObj= new HashMap<Subject, Integer>();
				subMapObj.put(new Subject(subjId),periodCount);
				classSubjPeriodCountMap.put(clsObj, subMapObj);
			}
		}
	}
	
	
	
	public boolean isTeacherAssignedToClass(String sgId)
	{
		boolean output=false;
		if(this.ownClassId!=null && this.ownClassId.equals(sgId))
			output=true;
		else if(this.classSubjPeriodCountMap!=null && !this.classSubjPeriodCountMap.isEmpty() && this.classSubjPeriodCountMap.containsKey(new Classroom(sgId)))
			output=true;
		
		return output;
	}
	
	public boolean isClassTeacher(String sgId)
	{
		boolean output=false;
		if(this.ownClassId!=null && this.ownClassId.equals(sgId))
			output=true;
	
		return output;
	}
	
	public boolean isTeacherTeachingThisSubj(String subjId)
	{
		boolean output=false;
		if(this.classSubjPeriodCountMap!=null  )
		{
			for(Classroom cls : this.classSubjPeriodCountMap.keySet())
			{
				Map<Subject,Integer> subs = this.classSubjPeriodCountMap.get(cls);
				if(subs.containsKey(new Subject(subjId)))
				{
					output=true;
					break;
				}
			}
		}
		return output;
	}
	
	public boolean isTeacherTeachingThisSubjforThisClass(String subjId, String cls)
	{
		boolean output=false;
		Classroom clas= new Classroom(cls);
		if(this.classSubjPeriodCountMap!=null  && this.classSubjPeriodCountMap.containsKey(clas))
		{
			Map<Subject,Integer> subPeriodMap = this.classSubjPeriodCountMap.get(clas);
			if(subPeriodMap.containsKey(new Subject(subjId)))
				output=true;
		}
		return output;
	}
	/**
	 * @return the teacherDepartment
	 */
	public String getTeacherDepartment() {
		return teacherDepartment;
	}

	/**
	 * @param teacherDepartment the teacherDepartment to set
	 */
	public void setTeacherDepartment(String teacherDepartment) {
		this.teacherDepartment = teacherDepartment;
	}

	/**
	 * @return the teacherLevel
	 */
	public Integer getTeacherLevel() {
		return teacherLevel;
	}

	/**
	 * @param teacherLevel the teacherLevel to set
	 */
	public void setTeacherLevel(Integer teacherLevel) {
		this.teacherLevel = teacherLevel;
	}




	/**
	 * @return the ownClassId
	 */
	public String getOwnClassId() {
		return ownClassId;
	}

	/**
	 * @param ownClassId the ownClassId to set
	 */
	public void setOwnClassId(String ownClassId) {
		this.ownClassId = ownClassId;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}


	/**
	 * @return the teacherID
	 */
	public String getTeacherID() {
		return teacherID;
	}
	/**
	 * @param teacherID the teacherID to set
	 */
	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}
	/**
	 * @return the teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}
	/**
	 * @param teacherName the teacherName to set
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
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
	 * @return the ownClass
	 */
	public String getOwnClass() {
		return ownClassId;
	}

	/**
	 * @param ownClass the ownClass to set
	 */
	public void setOwnClass(String ownClass) {
		this.ownClassId = ownClass;
	}

	/**
	 * @return the classSubjPeriodCountMap
	 */
	public Map<Classroom, Map<Subject, Integer>> getClassSubjPeriodCountMap() {
		return classSubjPeriodCountMap;
	}

	/**
	 * @param classSubjPeriodCountMap the classSubjPeriodCountMap to set
	 */
	public void setClassSubjPeriodCountMap(
			Map<Classroom, Map<Subject, Integer>> classSubjPeriodCountMap) {
		this.classSubjPeriodCountMap = classSubjPeriodCountMap;
	}

	/**
	 * @return the timetableFormat
	 */
	public String getTimetableFormat() {
		return timetableFormat;
	}

	/**
	 * @param timetableFormat the timetableFormat to set
	 */
	public void setTimetableFormat(String timetableFormat) {
		this.timetableFormat = timetableFormat;
	}

	@Override			
	   public boolean equals(Object obj) {
	       if (obj == null)
	           return false;
	       if (!(obj instanceof Teacher))
	           return false;
	       Teacher tchr = (Teacher) obj;
	       if (tchr.teacherID.equals(this.teacherID))
	    	   return true;
	       else
	    	   return false;
	   } 
	
}
