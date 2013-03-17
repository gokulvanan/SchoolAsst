package models;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;
import util.Const;

@Entity
public class Teacher extends Model{

	public String name;
	
	@OneToOne(fetch=FetchType.LAZY)
	public School school;
	
	@OneToOne(fetch=FetchType.LAZY)
	public TimeTable timetable;
	
	public long ownClazzId;
	
	@OneToMany(fetch=FetchType.LAZY)
	public List<ClassSubjMap> classSubjList;
	
	public Teacher(String name, School schl)
	{
		this.name=name;
		this.school=schl;
		this.ownClazzId=Const.NONE;
	}

	public long getAnyClassFromList() {
		
		return this.classSubjList.get(0).clazz.id;
	}
	
	public long getSubjForSelectedClass(long clazzId)
	{
		for(ClassSubjMap clsSubj : classSubjList)
		{
			if(clsSubj.clazz.id == clazzId)
				return clsSubj.subjs.get(0).getId();
		}
		return -1l;
	}
}
