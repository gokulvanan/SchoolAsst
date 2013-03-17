package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class Period extends Model implements Comparable<Period>{

	public int periodId;
	public String durationLabel;
	public String name;
	public long subjId;
	public String subj;
	public long tchrId;
	public String tchr;
	public long clazzId;
	public String clazz;
	
	public boolean breakPeriod;
	public Period(int id, String durationLabel,Subject subj, Teacher tchr, StudentClass clazz)
	{
		this.periodId=id;
		this.name=id+"";
		this.durationLabel=durationLabel;
		this.subjId=subj.id;
		this.subj=subj.name;
		this.tchrId=tchr.id;
		this.tchr=tchr.name;
		this.clazzId=clazz.id;
		this.clazz=clazz.name;
		this.breakPeriod=false;
	}
	
	public Period(int id, String name,String durationLabel){
		this.periodId=id;
		this.name=name;
		this.durationLabel=durationLabel;
		this.breakPeriod=true;
	}

	@Override
	public int compareTo(Period o) {
		return this.periodId=o.periodId;
	}
}
