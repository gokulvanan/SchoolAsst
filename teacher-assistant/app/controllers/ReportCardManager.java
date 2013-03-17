package controllers;

import java.util.Arrays;

import json.models.ReportCardDropDown;
import json.models.ReportCardJson;
import json.models.TimeTableSearchDropDown;

import com.google.gson.Gson;

import models.StudentClass;
import models.Teacher;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import util.Const;

public class ReportCardManager extends Controller {
	
	@Before
	static void addDefaults() {
		if(session.get("id") == null) return;
		User obj = User.findById(Long.parseLong(session.get("id")));
	    renderArgs.put("modules", obj.modules);
	    renderArgs.put("selected", 2);
	}
	
	public static void index()
	{
		System.out.println("here");
		if(session.get("id") == null)	render("Login/index.html", "Session Expired");
		User obj = User.findById(Long.parseLong(session.get("id")));
		Teacher tchr= obj.tchr;
		session.put("tchrId", tchr.id);
		String headerAndDropDownOpts=ReportCardDropDown.getInstance(tchr).toJson();
		long selectedClass=tchr.ownClazzId;
		
		if(selectedClass==Const.NONE)
			selectedClass=tchr.getAnyClassFromList();
		
		StudentClass clazz =StudentClass.findById(selectedClass);
		ReportCardJson repJson = ReportCardJson.getReportCardJson(tchr.id,clazz);
		String students=new Gson().toJson(repJson.studentDetailsMap);
		long selectedSubj=tchr.getSubjForSelectedClass(selectedClass);
		String studentMarks= new Gson().toJson(repJson.subjStudentTestMarksMap.get(selectedSubj));
		studentMarks= (studentMarks == null) ? "{}" : studentMarks;
		render(headerAndDropDownOpts,selectedClass,selectedSubj,students,studentMarks);
	}
	
	public static void fetchStudents(long classId)
	{
		Long tchrId=Long.parseLong(session.get("tchrId"));
		if(tchrId == null)	render("Login/index.html", "Session Expired");
		Teacher tchr = Teacher.findById(tchrId);
		StudentClass clazz =StudentClass.findById(classId);
		ReportCardJson repJson = ReportCardJson.getReportCardJson(tchrId,clazz);
		String students=new Gson().toJson(repJson.studentDetailsMap);
		String studentMarks= new Gson().toJson(repJson.subjStudentTestMarksMap.get(tchr.getSubjForSelectedClass(classId)));
		String json = new StringBuilder("{ students : ").append(students)
				.append(", studentMarks:").append(studentMarks).append("}").toString();
		System.out.println(json);
		renderJSON(new Gson().toJson(json));
	}
	
	public static void fetchSubjMarksList(long classId,long subjId)
	{
		Long tchrId=Long.parseLong(session.get("tchrId"));
		StudentClass clazz =StudentClass.findById(classId);
		ReportCardJson repJson = ReportCardJson.getReportCardJson(tchrId,clazz);
		String json = new StringBuilder("{ ").append("studentMarks:").append(repJson.subjStudentTestMarksMap.get(subjId)).append("}").toString();
		renderJSON(new Gson().toJson(json));
	}

}
