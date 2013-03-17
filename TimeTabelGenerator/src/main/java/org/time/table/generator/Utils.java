package org.time.table.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.time.table.exceptions.PeriodBackUpFailedException;
import org.time.table.exceptions.TimeTableExceptions;


public class Utils {
	
	////////////GENERAL UTILS////////////////////////
	private static  long count=0;
	
	
	public static Long nextCount()
	{
		count+=1;
		return count;
	}

	///////////// TEACHER SPECIFIC UTILS//////////////////
	public static Set<Teacher> getClassTeachers(Classroom cls,Set<Teacher> tchrs)
	{
		Set<Teacher> output = new HashSet<Teacher>();
		for( Teacher t : tchrs)
		{
			if(t.isClassTeacher(cls.getClassId()))
				output.add(t);
		}
		return output;
	}
	
	public static Set<Teacher> getTeachersofspecificClass(String cls, Set<Teacher> teachers)
	{
		Set<Teacher> output = new HashSet<Teacher>();
		for(Teacher t : teachers)
		{
			if(t.isTeacherAssignedToClass(cls))
				output.add(t);
		}
		return output;
	}
	
	
	public static Set<Teacher> getTeacherOfSpecificClassAndSubject(String cls,String subj,Set<Teacher> teachers)
	{
		Set<Teacher> output = new HashSet<Teacher>();
		for(Teacher t : teachers)
		{
			if(t.isTeacherTeachingThisSubjforThisClass(subj, cls))
				output.add(t);
		}
		return output;
	}
	
	public static void updateTeacherSet (Set<Teacher> oldSet, Set<Teacher> updateSet)
	{
		//as set does not add duplicates but retains old data.
		oldSet.removeAll(updateSet);
		oldSet.addAll(updateSet);
	}
//	public static Set<Teacher> updateTeacherTimeTabel
	////////////////////////////////////////
	
	public static void populateAllCombinationsOfTimeTables(Teacher tchr, Classroom cls, Subject sub, Integer periodCount) throws CloneNotSupportedException,Exception
	{
		if(tchr==null || cls==null || sub==null || periodCount==null)
			throw new Exception("Invalid inputs to populateAllCombinationsOfTimeTables");
		
		int tchrOptionsCount=0;
		int clsOptionsCount=0;
		try{
			tchrOptionsCount=tchr.getTimeTables().size();
			clsOptionsCount=cls.getTimeTables().size();	
		}catch(NullPointerException e)
		{
			// ignore case when timeTableNotIntialized
		}
		
		TimeTable option=null;
//		int periodsPerDay=(periodCount/Constants.dayPeriodsMap.size())+1;// upper limit for no periods that can be mapped each day
		//case 1 if none have options
		if(tchrOptionsCount==0 && clsOptionsCount==0)
		{
			option= new TimeTable(cls.getTimeTableFormat());
		}
		//case 2 if only tchr has any options
		else if(tchrOptionsCount>0 && clsOptionsCount==0)
		{
			option= new TimeTable(cls.getTimeTableFormat());
			List<TimeTable> existingOptions=new ArrayList<TimeTable>();
			existingOptions.addAll(tchr.getTimeTables());
			option.setRecursiveOptions(updateFlag(existingOptions,Constants.TEACHER));//set recursive options to start iterations from here
		}
		//case 3 if only cls has any options
		else if(tchrOptionsCount==0 && clsOptionsCount>0)
		{
			option= new TimeTable(cls.getTimeTableFormat());
			List<TimeTable> existingOptions=new ArrayList<TimeTable>();
			existingOptions.addAll(cls.getTimeTables());
			option.setRecursiveOptions(updateFlag(existingOptions,Constants.CLASS));//set recursive options to start iterations from here
		}
		//case 4 if both have options
		else if(tchrOptionsCount>0 && clsOptionsCount>0)
		{
			option= new TimeTable(cls.getTimeTableFormat());
			option.setRecursiveOptions(combineTchrClsOpt(tchr,cls));//set recursive options to start iterations from here
		}
		//invalid case
		else
		{
			System.out.println("In INvalid case");
		}
		
		generateCombinations(tchr, cls, sub, periodCount, option);

	}
	// to work on this
	public static List<TimeTable> clearUpdateFlag(List<TimeTable> input,String flag) throws TimeTableExceptions
	{
		if(input==null || input.isEmpty())
			throw new TimeTableExceptions("Error in clearUpdateFlag while generating options");
		for(TimeTable opt : input)
		{
			opt.clearandUpdatePeriodFromBk(flag);
		}
		return input;
	}
	
	// need this method to manipulate period so that orignal data is backedup and used to retain
	public static List<TimeTable> updateFlag(List<TimeTable> inputOpts,String flag) throws PeriodBackUpFailedException
	{
		List<TimeTable> output= new ArrayList<TimeTable>(inputOpts.size());
		for(TimeTable opt : inputOpts)
		{
			if(opt.isInvalid())
				continue;
			else
			{
				opt.backUPPeriods(flag);
				output.add(opt);
			}
		}
		inputOpts=null;
		return output;
	}
	public static void generateCombinations(Teacher tchr,Classroom cls,Subject subj,int periodCount,TimeTable buffer) throws CloneNotSupportedException, TimeTableExceptions
	{
		for(int i=0;i<periodCount;i++)
		{
			buffer.generateOptions(subj, tchr, cls);
			System.out.println(buffer.getRecursiveOptions().size());
		}
		tchr.setTimeTables(clearUpdateFlag(buffer.getRecursiveOptions(),Constants.TEACHER));
		cls.setTimeTables(clearUpdateFlag(buffer.getRecursiveOptions(),Constants.CLASS));
		buffer=null;// clear buffer
	}
	
	
	public static List<TimeTable> combineTchrClsOpt(Teacher tchr, Classroom cls)throws Exception
	{
		List<TimeTable> options = new ArrayList<TimeTable>();
		for(TimeTable tchrOpt : tchr.getTimeTables())
		{
			if(tchrOpt.isInvalid())// bypass invalid options once marked invalid
				continue;
			
			for(TimeTable clsOpt : cls.getTimeTables())
			{
				if(clsOpt.isInvalid()) // bypass invalid options once marked invalid
					continue;
				
				options.add(combineTwoOpt(tchrOpt,clsOpt));
			}
			
		}
		if(options.isEmpty())
			throw new Exception ("No combination possible between student and teacher options");
		return options;
	}
	
	public static TimeTable combineTwoOpt(TimeTable tchrOpt, TimeTable clsOpt) throws PeriodBackUpFailedException
	{
		TimeTable output= new TimeTable(clsOpt.getFormat(),null);
		tchrOpt.generateCopyOfPeriodsToTimeTableOutput(Constants.TEACHER,output);
		clsOpt.generateCopyOfPeriodsToTimeTableOutput(Constants.CLASS,output);
		return output;
	}
	///////////////// STUDENT-GROUP/CLASS SPECIFIC UTILS/////////////////////////
	
	/*
	 * 1) See if you can refractor the code on period  and timetable methods used to set flag and relase flag.- done 
	 * 2) Try to get a simple method applicable to all cases -done
	 * 3) Figure out when to add timetable Id to invalid ids and how to filter them out inorder to improve perofrmance of each iteration.- TO DO
	 * 4) Work out how to apply conditions to this code.- TODO
	 * 
	 * 
	 */
	
}
