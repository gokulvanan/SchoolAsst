package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class TestTempalte extends Model{

	public String name;
	public int maxMarks;
	public Date date;
	public static SimpleDateFormat sdf = null;
	
	static
	{
		 sdf = new SimpleDateFormat("dd-MM-yyyy");
	}
	
	public TestTempalte(String name,int maxMarks, String date) throws ParseException
	{
		this.name=name;
		this.maxMarks=maxMarks;
		this.date=sdf.parse(date);
	}
}
