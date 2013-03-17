package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Subject extends Model{

	public String name;
	
	public Subject(String name)
	{
		this.name=name;
	}
	
}
