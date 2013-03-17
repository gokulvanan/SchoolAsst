package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


import play.db.jpa.Model;
@Entity
public class ReportCard extends Model{
	
	@OneToOne(fetch=FetchType.LAZY)
	public School school;
	public int accademicYear;
	
	@ElementCollection
	@MapKeyColumn(name="testTempId")
	@Column(name="marks")
	public Map<Long,Double> testMarksMap;

	
	public ReportCard(School schl, int yr)
	{
		this.school=schl;
		this.accademicYear=yr;
		this.testMarksMap=new HashMap<Long,Double>();
	}
	
	public void updateMarks(double marks,long testTemplateId)
	{
		testMarksMap.put(testTemplateId, marks);
	}
}
