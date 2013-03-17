package json.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.cache.Cache;

import models.School;
import models.StudentClass;
import models.ClassSubjMap;
import models.SubjReportTemplate;
import models.Subject;
import models.Teacher;
import models.TestTempalte;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ReportCardDropDown implements Serializable{

	private Map<Long,String> classes= null;
	private Map<Long,Map<Long,String>>classSubjMap=null;
	public Map<Long,List<TestTempalte>> subjTestTemplateMap;// Added as this is one time load and will never change
	
	
	public static ReportCardDropDown getInstance(Teacher tch)
	{
		String cacheFlag="reportCardDropDown-"+tch.id;
		ReportCardDropDown obj = (ReportCardDropDown) Cache.get(cacheFlag);
		if(obj != null)		return obj;
		obj= new ReportCardDropDown(tch.classSubjList);
		Cache.set(cacheFlag, obj,"5mn");
		return obj;
	}
	private ReportCardDropDown(List<ClassSubjMap> classSubjList)
	{
		classes=new HashMap<Long, String>();
		classSubjMap=new HashMap<Long, Map<Long,String>>();
		subjTestTemplateMap = new HashMap<Long, List<TestTempalte>>();
		for(ClassSubjMap clsmap : classSubjList)
		{
			StudentClass c = clsmap.clazz;
			classes.put(c.id, c.name);
			HashMap<Long, String> subs = new HashMap<Long, String>();
			for(Subject s : clsmap.subjs)
			{
				subs.put(s.id, s.name);
			}
			classSubjMap.put(c.id,subs);
			populateSubjReportTemplate(clsmap.clazz.subjReportTemplate);
			
		}
	}

	private void populateSubjReportTemplate(List<SubjReportTemplate> data)
	{
		for(SubjReportTemplate temp : data)
		{
			subjTestTemplateMap.put(temp.sub.id,temp.testTemplate);
		}
	}
	
	public String toJson() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}
	

}
