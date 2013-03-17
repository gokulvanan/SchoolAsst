package models;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Day extends Model{
	
	public String name;
	@OneToMany(fetch=FetchType.LAZY)
	public List<Period> periods;
	
	public Day(String name, List<Period> prds)
	{
		this.name=name;
		this.periods=prds;
		for(Period p : prds)
			p.save();
	}
	

}
