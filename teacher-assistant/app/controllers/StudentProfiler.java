package controllers;

import json.models.ReportCardDropDown;
import json.models.StudentsDropDown;

import com.google.gson.Gson;

import models.Teacher;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;

public class StudentProfiler extends Controller {

	@Before
	static void addDefaults() {
		User obj = User.findById(Long.parseLong(session.get("id")));
	    renderArgs.put("modules", obj.modules);
	    renderArgs.put("selected", 3);
	}
	
	public static void index()
	{
		if(session.get("id") == null)	render("Login/index.html", "Session Expired");
		User obj = User.findById(Long.parseLong(session.get("id")));
		Teacher tchr= obj.tchr;
		String dropDownOpts=(new Gson()).toJson(tchr.classSubjList);
		String studentsList=(new Gson()).toJson(new StudentsDropDown(tchr.classSubjList).studentList);
		render(dropDownOpts,studentsList);
	}
	
	public static void fetchStudent(Long id)
	{
		System.out.println("Here" +id);
		index();
	}
}
