package json.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import models.School;
import models.StudentClass;
import models.Teacher;

public class TimeTableSearchDropDown {

	private Map<Long,String> teachers= null;
	private Map<Long,String> classes= null;
	
	public TimeTableSearchDropDown(School school)
	{
		// Initialise default select options
		teachers= new HashMap<Long, String>(){	{	put(0l,"Select");	}	};
		classes= new HashMap<Long, String>() {	{	put(0l,"Select");	}	};
		//fetch All Data
		List<Teacher> tchrs			 =  Teacher.find("bySchool", school).fetch();
		List<StudentClass> clazzes 	 =  StudentClass.find("bySchool", school).fetch();
		// Populate Dropdowns
		for(Teacher tchr : tchrs)			teachers.put(tchr.getId(), tchr.name);
		for(StudentClass clazz : clazzes)	classes.put(clazz.getId(), clazz.name);
	}
	
	public String getJson()
	{
		return new Gson().toJson(this);
	}
	
}
