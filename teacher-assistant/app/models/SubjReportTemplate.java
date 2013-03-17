package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;
@Entity
public class SubjReportTemplate extends Model{

	@ManyToOne(fetch=FetchType.LAZY)
	public Subject sub;
	@OneToMany(fetch=FetchType.LAZY)
	public List<TestTempalte> testTemplate;
	
	public SubjReportTemplate(Subject subj, List<TestTempalte> templates )
	{
		this.sub=subj;
		this.testTemplate=templates;
	}
	
}
