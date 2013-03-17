package unit.test.cases;

import org.junit.Test;

import json.models.ReportCardDropDown;
import json.models.ReportCardJson;

import com.google.gson.Gson;

import models.ClassSubjMap;
import models.StudentClass;
import models.Subject;
import models.Teacher;
import models.User;
import play.test.UnitTest;
import util.Const;


public class ReportCardManager extends UnitTest {

	@Test
	public  void constructDropdDownTest()
	{
		User obj = User.find("byEmail", "mathy.velan@gmail.com").first();
		assertNotNull(obj);
		Teacher tchr = obj.tchr;
		assertNotNull(tchr);
		assertNotNull(tchr.classSubjList);
		String json =new Gson().toJson(new ReportCardDropDown(tchr.classSubjList));
		assertNotNull(json);
		System.out.println(json);
	}
	@Test
	public  void getReportsForClass(){
		User obj = User.find("byEmail", "mathy.velan@gmail.com").first();
		assertNotNull(obj);
		Teacher tchr = obj.tchr;
		assertNotNull(tchr);
		assertNotNull(tchr.classSubjList);
		long selectedClass=tchr.ownClazzId;
		
		if(selectedClass==Const.NONE)
			selectedClass=tchr.getAnyClassFromList();
		
		StudentClass clazz =StudentClass.findById(selectedClass);
		assertNotNull(clazz);
		ReportCardJson repJson = ReportCardJson.getReportCardJson(tchr.id,clazz);
		assertNotNull(repJson);
		System.out.println("HERE ");
		String header=new Gson().toJson(repJson.subjTestTemplateMap);
		System.out.println(header);
		String students=new Gson().toJson(repJson.studentDetailsMap);
		System.out.println(students);
		System.out.println("Subject marks for Class :- "+clazz.name);
		for(ClassSubjMap clsSubj : tchr.classSubjList)
		{
			System.out.println(clazz.id+"- class subj map "+clsSubj.clazz.id);
			if(clsSubj.clazz.id == clazz.id)
			{
				for(Subject subj : clsSubj.subjs)
				{
					System.out.println(subj.name);
					String studentMarks= new Gson().toJson(repJson.subjStudentTestMarksMap.get(subj.id));
					System.out.println(studentMarks);
				}
			}
		}
		
		
		
	}
	
}
