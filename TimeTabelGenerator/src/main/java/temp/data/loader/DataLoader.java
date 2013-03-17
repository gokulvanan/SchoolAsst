package temp.data.loader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.time.table.generator.Classroom;
import org.time.table.generator.Room;
import org.time.table.generator.Subject;
import org.time.table.generator.Teacher;


public class DataLoader {

	public static List<Classroom> classes=null;
	public static List<Teacher> teachers=null;
	
	public static List<Classroom>  loadSgGroups(String filePath, int sheetNo)
	{
		try {
			ExcelParser exec=new ExcelParser(filePath);
			if(!exec.ValidateFile())
				throw new Exception("INvalid file");

			Map<Integer,List<String>> results=exec.parser(sheetNo,1);
			List<Classroom> sglist= new ArrayList<Classroom>(results.size());
			for(Integer row: results.keySet())
			{
				List<String> rowDatas= results.get(row);
				if(rowDatas!=null )
				{
					Classroom sg= new Classroom(rowDatas.get(0), rowDatas.get(1), rowDatas.get(2), rowDatas.get(3), rowDatas.get(4));
					sglist.add(sg);
				}
			}
			classes=sglist;
			return sglist;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}//exclude header
		
	}
	
	public static Set<Teacher>  loadTeachers(String filePath)
	{
		try {
			ExcelParser exe=new ExcelParser(filePath);
			if(!exe.ValidateFile())
				throw new Exception("INvalid file");
			Map<Integer,List<String>> sgMap=exe.parser(0, 1);
			Map<Integer,List<String>> results=exe.parser(1, 1);
			Map<Integer,List<String>> subjmap=exe.parser(2,1);
			Set<Teacher> teacherSet= new HashSet<Teacher>(results.size());
			for(Integer row: results.keySet())
			{
				List<String> rowDatas= results.get(row);
				if(rowDatas!=null )
				{
					Teacher teacher = new Teacher(rowDatas.get(0), rowDatas.get(1), rowDatas.get(2), rowDatas.get(3), rowDatas.get(4), rowDatas.get(5),
							rowDatas.get(6), rowDatas.get(7));
					populateClass(teacher,sgMap);
					populateSubj(teacher,subjmap);
					teacherSet.add(teacher);
				}
			}
			return teacherSet;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}//exclude header
		
	}
	
	private static void populateClass(Teacher teacher,Map<Integer,List<String>> sgMap)
	{
		Map<Classroom,Map<Subject,Integer>> sgSubPeriodMap = teacher.getClassSubjPeriodCountMap();
		for(Integer row : sgMap.keySet())
		{
			List<String> rowDatas= sgMap.get(row);
			if(rowDatas!=null )
			{
				for(Classroom sgCls : sgSubPeriodMap.keySet())
				{
					
					if(sgCls.getClassId().equals(rowDatas.get(0)))
					{
						sgCls.setClassStd(Integer.parseInt(rowDatas.get(1)));
						sgCls.setClassType(rowDatas.get(2));
						sgCls.setClassRoomLoc(new Room(Integer.parseInt(rowDatas.get(3))));
						sgCls.setTimeTableFormat(rowDatas.get(4));
					}
				}
			}
			
		}
	}
	
	private static void populateSubj(Teacher teacher,Map<Integer,List<String>> subjMap)
	{
		Map<Classroom,Map<Subject,Integer>> sgSubPeriodMap = teacher.getClassSubjPeriodCountMap();
		for(Integer row : subjMap.keySet())
		{
			List<String> rowDatas= subjMap.get(row);
			if(rowDatas!=null )
			{
				for(Classroom sgCls : sgSubPeriodMap.keySet())
				{
					Map<Subject, Integer> subMap=sgSubPeriodMap.get(sgCls);
					for(Subject sub : subMap.keySet())
					{
						if(sub.getSubjectId().equals(rowDatas.get(0)))
						{
							sub.setSubjDesc(rowDatas.get(1));
							sub.setSubSubj(Boolean.parseBoolean(rowDatas.get(2)));
							sub.setSuperSubjId(rowDatas.get(3));
							sub.setSuperSubjDesc(rowDatas.get(4));
							sub.setSubjectImportance(Integer.parseInt(rowDatas.get(5)));
						}
					}
					
				}
			}
			
		}
	}
}
