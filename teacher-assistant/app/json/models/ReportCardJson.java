package json.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ReportCard;
import models.Student;
import models.StudentClass;
import models.SubjReportTemplate;
import models.TestTempalte;

import org.apache.commons.lang.builder.HashCodeBuilder;

import play.cache.Cache;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class ReportCardJson implements Serializable {

	static class StudentJson {
		private long id;
		private String name;
		private int rollNo;
		
		public  StudentJson(long id,String name, int rollNo)
		{
			this.id=id;
			this.name=name;
			this.rollNo=rollNo;
		}
	}
	
	static class TestMarksJson{
		
		private double marks;
		
		public TestMarksJson(double marks)
		{
			this.marks=marks;
		}
	}

	public Map<Long,Map<Long,Map<Long,TestMarksJson>>> subjStudentTestMarksMap;
	public Map<Long,StudentJson> studentDetailsMap;

	
	public static ReportCardJson getReportCardJson(long tchrId, StudentClass clazz)
	{
		String cacheFlag="reportcardJson-"+tchrId+"-"+clazz.id;
		ReportCardJson output = (ReportCardJson) Cache.get(cacheFlag);
		if(output != null) 		return output;
		
		output = new ReportCardJson(clazz);
		Cache.set(cacheFlag, output,"10mn");
		
		return output;
	}
	
	private ReportCardJson(StudentClass clazz)
	{
		intializeHeaderAndStudentsOfAClass(clazz);
		initializeSubjStudentMarks(clazz);
	}
	
	public void intializeHeaderAndStudentsOfAClass(StudentClass clazz)
	{
		if(clazz.students.size() == 0) 	return;
		studentDetailsMap = new HashMap<Long, StudentJson>();

		for(Student std : clazz.students)
		{
			studentDetailsMap.put(std.id,new StudentJson(std.id, std.name, std.rollNo));
		}
	}
	
	
	public void initializeSubjStudentMarks(StudentClass clazz)
	{
		subjStudentTestMarksMap = new HashMap<Long,Map<Long,Map<Long, TestMarksJson>>>();
		for(SubjReportTemplate temp : clazz.subjReportTemplate)
		{
			
			Map<Long,Map<Long,TestMarksJson>> stdTestMarksMap = new HashMap<Long, Map<Long,TestMarksJson>>();
			for(Student std : clazz.students)
			{
				Map<Long,Double> stdTestMarks = std.reportCard.testMarksMap;
				Map<Long,TestMarksJson> testMarksMap = new HashMap<Long, ReportCardJson.TestMarksJson>();
				for(TestTempalte testTemp : temp.testTemplate)
				{
					testMarksMap.put(testTemp.id, new TestMarksJson(stdTestMarks.get(testTemp.id)));
				}
				stdTestMarksMap.put(std.id, testMarksMap);
			}
			subjStudentTestMarksMap.put(temp.sub.id, stdTestMarksMap);
		}
	}
	
	


}
