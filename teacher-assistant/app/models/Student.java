package models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class Student extends Model{

	public String name;
	public int rollNo;
	public String idCardNo;
	@OneToOne(fetch=FetchType.LAZY)
	public School school;
	@OneToOne(fetch=FetchType.LAZY)
	public ReportCard reportCard;


	public Student(String name, int i, String id, School school, ReportCard report )
	{
		this.name=name;
		this.rollNo=i;
		this.idCardNo=id;
		this.school=school;
		this.reportCard=report;
	}
}
