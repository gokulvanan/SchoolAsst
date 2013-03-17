package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class School extends Model{
	
	public String name;

	public School(String name)
	{
		this.name=name;
	}
}
