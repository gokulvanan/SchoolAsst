package models;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.engine.Cascade;

import play.db.jpa.Model;

@Entity
public class TimeTable extends Model{

	@OneToOne(fetch=FetchType.LAZY)
	public School school;
	
	@OneToMany(fetch=FetchType.LAZY)
	public List<Day> days;
	
	public TimeTable(School schl, List<Day> days)
	{
		this.school=schl;
		this.days=days;
	}

	public void sortPeriods() {
		for(Day d : days)
		{
			Collections.sort(d.periods);
		}
	}
	
}
