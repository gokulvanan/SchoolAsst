package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Module extends Model {

	@Required
	public String name;
	
	@Required
	public String url;
	
	
	public Module(String name, String url)
	{
		this.name=name;
		this.url=url;
	}
}
