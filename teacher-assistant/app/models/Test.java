package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class Test extends Model{

	public long templateId;
	public double marks;
	
	public Test(TestTempalte testTemplate, double mark) {
		this.templateId=testTemplate.getId();
		this.marks=mark;
	}

	
}
