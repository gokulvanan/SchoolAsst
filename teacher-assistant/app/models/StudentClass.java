package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OrderBy;

import play.db.jpa.Model;

@Entity
public class StudentClass extends Model{

	public String name;
	
	@OneToOne(fetch=FetchType.LAZY)
	public School school;
	
	@OneToOne(fetch=FetchType.LAZY)
	public TimeTable timetable;
	
	@OneToMany(fetch=FetchType.LAZY)
	public List<SubjReportTemplate> subjReportTemplate;
	
	@OneToMany(fetch=FetchType.LAZY)
	public List<Student> students;
	
	public StudentClass(String name,School school)
	{
		this.name=name;
		this.school=school;
		this.students= new ArrayList<Student>();
		this.subjReportTemplate= new ArrayList<SubjReportTemplate>();
	}
	
	
	public void addStudents(Student std)
	{
		this.students.add(std);
	}
	
	public void addSubjReportTemplate(SubjReportTemplate temp)
	{
		this.subjReportTemplate.add(temp);
	}
}
