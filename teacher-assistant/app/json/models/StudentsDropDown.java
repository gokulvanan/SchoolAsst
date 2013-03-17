package json.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.security.krb5.internal.crypto.dk.ArcFourCrypto;

import models.ClassSubjMap;
import models.Student;
import models.StudentClass;
import models.Teacher;

import com.google.gson.JsonElement;

public class StudentsDropDown {

	public static class StudentMap{
		
		public StudentMap(Long id, String name){
			this.id=id;
			this.name=name;
		}
		private String name;
		private Long id;
	}
	public Map<Long,List<StudentMap>>studentList= null;
	
	public StudentsDropDown(List<ClassSubjMap> classList) {
		
		studentList= new HashMap<Long, List<StudentMap>>(classList.size());
		for(ClassSubjMap clazzMap : classList)
		{
			StudentClass clazz = clazzMap.clazz;
			List<StudentMap> stdlst= new ArrayList<StudentsDropDown.StudentMap>(clazz.students.size());
			for(final Student std : clazz.students)
			{
				stdlst.add(new StudentMap(std.id,std.name));
			}
			studentList.put(clazz.id,stdlst);
		}
	}

}
