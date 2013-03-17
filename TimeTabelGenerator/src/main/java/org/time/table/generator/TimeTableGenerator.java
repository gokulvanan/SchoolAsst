package org.time.table.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.time.table.exceptions.TimeTableExceptions;

import temp.data.loader.DataLoader;

public class TimeTableGenerator {


	public static List<Classroom> classes=null;
	public static Set<Teacher> teachers= null;

	public static void loadData()
	{
		String filePath="D:/Own App Work/SchoolApp/DBScripts.xls";
//		classes= DataLoader.loadSgGroups(filePath, 0);
		teachers= DataLoader.loadTeachers(filePath);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//load startup data
		loadData();
		try {
			createTimeTableOptionsOnBaseConditions();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//1) Iterate through hardcoded conditions
//		updateTimeTableTemplateonHardCodedConditions();
		//2) Iterate through other conditions which generate options
//		createTimeTableOptionsOnBaseConditions();
		//3) Iterate teacher specific conditions

		//4) construct output out of results
		generateOutputExcelSheet();
	}
	
	private static void updateTimeTableTemplateonHardCodedConditions()
	{
		/*try
		{
			for(Classroom cls : classes)
			{
				// for saturdayFlagCondition
				Conditions.initializeStudentGroupTimeTableTemplate(cls);//Does not apply to Teacher
				Conditions.chantingPeriodCondition(cls,teachers);
				Conditions.ccaPeriodCondition(cls,teachers);
				//To add princi and vice princi conidtions here
			}
		}catch(TimeTableExceptions e) //Error case.. need to throw error in this case.
		{
			e.printStackTrace();
		}*/
	}
	
	
	private static void createTimeTableOptionsOnBaseConditions() throws CloneNotSupportedException, Exception
	{
		for(Teacher t : teachers)
		{
			Map<Classroom,Map<Subject,Integer>> classRoomSubjPeriodMap= t.getClassSubjPeriodCountMap();
			for(Classroom cls : classRoomSubjPeriodMap.keySet())
			{
				Map<Subject,Integer> subPeriodMap = classRoomSubjPeriodMap.get(cls);
				for(Subject sub : subPeriodMap.keySet())
				{
					Integer periodCount = subPeriodMap.get(sub);
					//outcome of this method is that tchr and cls have options of all possible combinations of subject mapping in timetables for each of them
					// This method also check for eariler options in tchr and cls and invalidates the options that do not generate any sub options.
					Utils.populateAllCombinationsOfTimeTables(t,cls,sub,periodCount);
				}
			}
			// need to add a method here that removes invalid options of tchr and cls list
		}
	}

	private static void generateOutputExcelSheet()
	{
		
	}
	
}

