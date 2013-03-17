package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class ClassSubjMap extends Model{

	public ClassSubjMap (StudentClass clazz,List<Subject> subjs)
	{
		this.clazz=clazz;
		this.subjs=subjs;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	public StudentClass clazz;
	
	@ManyToMany(fetch=FetchType.LAZY)
	public List<Subject> subjs;
}
