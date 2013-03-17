package controllers;

import json.models.TimeTableJson;
import json.models.TimeTableSearchDropDown;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.NEW;

import models.StudentClass;
import models.Teacher;
import models.TimeTable;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;

public class TimeTableManager extends Controller {
	
	@Before
	static void addDefaults() {
		if(session.get("id") == null) return;
		User obj = User.findById(Long.parseLong(session.get("id")));
	    renderArgs.put("modules", obj.modules);
	    renderArgs.put("selected", 1);
	}
	
	public static void index()
	{
		if(session.get("id") == null)	render("Login/index.html", "Session Expired");
		User obj = User.findById(Long.parseLong(session.get("id")));
		String dropDownOpts=new TimeTableSearchDropDown(obj.school).getJson();
		render(dropDownOpts);
	}
	
	public static void fetchTimeTable(long id,String type)
	{
		String json =new TimeTableJson(id,type).getJson();
		renderJSON(json);
	}
}
