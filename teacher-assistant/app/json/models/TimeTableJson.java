package json.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import models.Day;
import models.Period;
import models.StudentClass;
import models.Teacher;
import models.TimeTable;

public class TimeTableJson {

	private List<Map<String,List<Period>>> timeTable;
	
	public TimeTableJson(TimeTable obj){
		init(obj);
	}
	
	public TimeTableJson(long id, String type) {
		if(type.equalsIgnoreCase("teacher")) 
		{
			Teacher tchr =Teacher.findById(id);
			init(tchr.timetable);
		}
		else
		{
			StudentClass clazz = StudentClass.findById(id);
			init(clazz.timetable);
		}
	}

	private void init(TimeTable obj)
	{
		if(obj == null)	return;
		timeTable= new ArrayList<Map<String,List<Period>>>();
		for (Day d : obj.days)
		{
			HashMap<String,List<Period>> map = new HashMap<String, List<Period>>();
			map.put(d.name,d.periods);
			timeTable.add(map);
		}
	}
	public String getJson()
	{
		return new Gson().toJson(this);
	}
}

