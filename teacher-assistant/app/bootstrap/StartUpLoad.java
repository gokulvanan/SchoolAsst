package bootstrap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Day;
import models.Module;
import models.Period;
import models.ReportCard;
import models.School;
import models.Student;
import models.StudentClass;
import models.SubjReportTemplate;
import models.Subject;
import models.ClassSubjMap;
import models.Teacher;
import models.TestTempalte;
import models.TimeTable;
import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class StartUpLoad extends Job{

	
	 public void doJob() throws ParseException {
	     
		 final School school = new School("Chinamaya");school.save();
		 	// create new Use
			User tchr1 = new User("mathy.velan@gmail.com","noodles","Valarmathy Velan",school);
		 	tchr1.addModules("Time Table Manager","/TimeTableManager/index/");
		 	tchr1.addModules("Report Card Manager","/ReportCardManager/index/");
		 	tchr1.addModules("Student Profiler","/StudentProfiler/index/");
		 	tchr1.save();

		
		 //Create Subjects
		 final Subject eng = new Subject("English");eng.save();
		 final Subject math = new Subject("Maths");math.save();
		 final Subject sci = new Subject("Science");sci.save();
		 final Subject soc = new Subject("Social");soc.save();
		 final Subject lang2 = new Subject("2nd Language");lang2.save();
		 final Subject yoga = new Subject("Yoga");yoga.save();
		 final Subject pe = new Subject("Physical Education");pe.save();
		 final Subject pd = new Subject("Personality Development");pd.save();
		 
		 List<Subject> subjList = new ArrayList<Subject>(){
			 {
				 add(eng);
				 add(math);
				 add(sci);
				 add(soc);
				 add(lang2);
				 add(yoga);
				 add(pe);
				 add(pd);
			 }
		 };
		 
		 //Create StudentClasses
		 final StudentClass tenA = new StudentClass("10 A",school);tenA.save();
		 final StudentClass tenB = new StudentClass("10 B",school);tenB.save();
		 final StudentClass tenC = new StudentClass("10 C",school);tenC.save();
		 final StudentClass tenD = new StudentClass("10 D",school);tenD.save();
		 
		 for(Subject s : subjList)
		 {
			 // create tempalate
			 List<TestTempalte> testtempList = new ArrayList<TestTempalte>(3);
			 TestTempalte test1 =new TestTempalte("Test 1",25, "21-07-2012"); test1.save();
			 testtempList.add(test1);
			 TestTempalte test2 =new TestTempalte("Test 2",25, "15-09-2012"); test2.save();
			 testtempList.add(test2);
			 TestTempalte finalTest =new TestTempalte("Final",100, "01-12-2012"); finalTest.save();
			 testtempList.add(finalTest);
			 SubjReportTemplate temp = new SubjReportTemplate(s, testtempList);
			 temp.save();
			 tenA.addSubjReportTemplate(temp);
		 }
		 tenA.save();
		 
		
		 // create Students
		 for(int i=0; i<40; i++)
		 {
			 // insert report from template in class tenA
			 List<SubjReportTemplate> lst =  tenA.subjReportTemplate;
			 ReportCard  rep = new ReportCard(school, 2012);
			 for(SubjReportTemplate subjTemp : lst)
			 {
				 for(TestTempalte templ : subjTemp.testTemplate)
				 {
					 rep.updateMarks((100*Math.random()), templ.getId()); 
				 }
			 }
			 rep.save();
			 Student std = new Student("sampleName"+i, i, "CHMY"+i, school, rep); std.save();
			
			 tenA.addStudents(std);
			 tenA.save();
		 }
		 List<StudentClass> stdClassList =new ArrayList<StudentClass>(){
			 {
				 add(tenA);
				 add(tenB);
				 add(tenC);
				 add(tenD);
			 }
		 };
		 
		 new ClassSubjMap(tenA,new ArrayList<Subject>(){
			 {
				 add(soc);
				 add(pd);
			 }}).save();
//		 clssSubmap for socTeacher
		 List<ClassSubjMap> clsSubMap = new ArrayList<ClassSubjMap>(){
			 {
				 add(new ClassSubjMap(tenA,new ArrayList<Subject>(){
					 {
						 add(soc);
						 add(pd);
					 }
				 }));
				 add(new ClassSubjMap(tenB,new ArrayList<Subject>(){
					 {
						 add(soc);
					 }
				 }));
			 }
		 };
		 // persist
		 for(ClassSubjMap cl : clsSubMap) cl.save();
		 
		 		 
		 //Create teachers
		 final Teacher socTeacher = new Teacher("Valarmathy",school);
		 socTeacher.classSubjList=clsSubMap;
		 socTeacher.ownClazzId=tenA.getId();
		 socTeacher.save();
		 tchr1.tchr=socTeacher;
		 tchr1.save();

		 final Teacher engTeacher = new Teacher("Teacher1",school);engTeacher.save();
		 final Teacher mathTeacher = new Teacher("Teacher2",school);mathTeacher.save();
		 final Teacher sciTeacher = new Teacher("Teacher3",school);sciTeacher.save();
		 final Teacher lang2Teacher = new Teacher("Teacher5",school);lang2Teacher.save();
		 final Teacher yogaTeacher = new Teacher("Teacher6",school);yogaTeacher.save();
		 final Teacher peTeacher = new Teacher("Teacher7",school);peTeacher.save();
		 final Teacher pdTeacher = new Teacher("Teacher8",school);pdTeacher.save();
		 
		 final String period1="(8:00 - 8:45)";
		 final String period2="(8:45 - 9:30)";
		 final String break1=" (9:30 - 9:45)";
		 final String period3="(9:45 - 10:30)";
		 final String period4="(10:30 - 11:15)";
		 final String lunch	="(11:15 - 12:15)";
		 final String period5="(12:15 - 13:00)";
		 final String period6="(13:00 - 13:45)";
		 final String break3="(13:45 - 14:00)";
		 final String period7="(14:00 - 14:45)";
		 final String period8="(14:45 - 15:30)";
		 
		 
		 
		 // create new TimeTable
		 List<Period>periods= new ArrayList<Period>(){
			 {
				 add(new Period(1,period1, eng, engTeacher, tenA));
				 add(new Period(2,period2, sci, sciTeacher, tenA));
				 add(new Period(3,"Break",break1));
				 add(new Period(4,period3, math, mathTeacher, tenA));
				 add(new Period(5,period4, math, mathTeacher, tenA));
				 add(new Period(6,"Lunch",lunch));
				 add(new Period(7,period5, lang2, lang2Teacher, tenA));
				 add(new Period(8,period6, soc, socTeacher, tenA));
				 add(new Period(9,"Break",break3));
				 add(new Period(10,period7, pd, pdTeacher, tenA));
				 add(new Period(11,period8, pe, peTeacher, tenA));
			 }
		 };
		final Day monday = new Day("Monday", periods);monday.save();
		 
		 periods= new ArrayList<Period>(){
			 {
				 
				 add(new Period(1,period1, sci, sciTeacher, tenA));
				 add(new Period(2,period2, eng, engTeacher, tenA));
				 add(new Period(3,"Break",break1));
				 add(new Period(4,period3, soc, socTeacher, tenA));
				 add(new Period(5,period4, soc, socTeacher, tenA));
				 add(new Period(6,"Lunch",lunch));
				 add(new Period(7,period5, lang2, lang2Teacher, tenA));
				 add(new Period(8,period6, math, socTeacher, tenA));
				 add(new Period(9,"Break",break3));
				 add(new Period(10,period7, yoga, yogaTeacher, tenA));
				 add(new Period(11,period8, yoga, yogaTeacher, tenA));
			 }
		 };
		 final Day tue = new Day("Tuesday",periods);tue.save();
		 periods= new ArrayList<Period>(){
			 {
				 add(new Period(1,period1, eng, engTeacher, tenA));
				 add(new Period(2,period2, sci, sciTeacher, tenA));
				 add(new Period(3,"Break",break1));
				 add(new Period(4,period3, math, mathTeacher, tenA));
				 add(new Period(5,period4, math, mathTeacher, tenA));
				 add(new Period(6,"Lunch",lunch));
				 add(new Period(7,period5, lang2, lang2Teacher, tenA));
				 add(new Period(8,period6, soc, socTeacher, tenA));
				 add(new Period(9,"Break",break3));
				 add(new Period(10,period7, pd, pdTeacher, tenA));
				 add(new Period(11,period8, pe, peTeacher, tenA));
			 }
		 };
		 
		 final Day wed = new Day("Wednesday",periods);wed.save();
		 periods= new ArrayList<Period>(){
			 {
				 add(new Period(1,period1, math, mathTeacher, tenA));
				 add(new Period(2,period2, pd, pdTeacher, tenA));
				 add(new Period(3,"Break",break1));
				 add(new Period(4,period3,sci , sciTeacher, tenA));
				 add(new Period(5,period4, sci, sciTeacher, tenA));
				 add(new Period(6,"Lunch",lunch));
				 add(new Period(7,period5, eng, engTeacher, tenA));
				 add(new Period(8,period6, soc, socTeacher, tenA));
				 add(new Period(9,"Break",break3));
				 add(new Period(10,period7, pd, pdTeacher, tenA));
				 add(new Period(11,period8, pe, peTeacher, tenA));
			 }
		 };
		 
		 final Day thu = new Day("Thrusday",periods);thu.save();
		 periods= new ArrayList<Period>(){
			 {
				 add(new Period(1,period1, soc, socTeacher, tenA));
				 add(new Period(2,period2, soc, socTeacher, tenA));
				 add(new Period(3,"Break",break1));
				 add(new Period(4,period3, eng, engTeacher, tenA));
				 add(new Period(5,period4, math, mathTeacher, tenA));
				 add(new Period(6,"Lunch",lunch));
				 add(new Period(7,period5, eng, engTeacher, tenA));
				 add(new Period(8,period6, yoga, yogaTeacher, tenA));
				 add(new Period(9,"Break",break3));
				 add(new Period(10,period7, math, mathTeacher, tenA));
				 add(new Period(11,period8, sci, sciTeacher, tenA));
			 }
		 };
		 final Day fri = new Day("Friday",periods);fri.save();
		 List<Day> days = new ArrayList<Day>(){
			 {
				 add(monday);
				 add(tue);
				 add(wed);
				 add(thu);
				 add(fri);
			 }
		 };
		 
		 TimeTable t= new TimeTable(school, days);
		 t.save();
		 
		 tenA.timetable=t;
		 tenA.save();
		 
		 engTeacher.timetable=t;engTeacher.save();
		 sciTeacher.timetable=t;sciTeacher.save();
		 socTeacher.timetable=t;socTeacher.save();
		 mathTeacher.timetable=t;mathTeacher.save();
		 yogaTeacher.timetable=t;yogaTeacher.save();
		 pdTeacher.timetable=t;pdTeacher.save();
		 peTeacher.timetable=t;peTeacher.save();
		 lang2Teacher.timetable=t;lang2Teacher.save();
		 
		 	 
	
	 }

}
