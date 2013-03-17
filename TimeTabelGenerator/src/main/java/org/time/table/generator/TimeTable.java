package org.time.table.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.time.table.exceptions.PeriodBackUpFailedException;
import org.time.table.exceptions.PeriodNotEmptyException;
import org.time.table.exceptions.TimeTableExceptions;


public class TimeTable implements Constants, Cloneable {

	private Long id=null;//unique identifier for each timetable
	public static List<Long> invalidIds= new Vector<Long>();// will hold all ids of invalid timetable options
	private Map<String,Long> classTchrLinkMap=null;// list of class id and teacherId and the version No. to identify the mapping
	private Map<String,Map<Integer,Period>> periods;// day to period map
	private boolean saturdayFlag= true;// flag is false when there is not saturday place holder  in time table
	private String format=null;// label to get timetable format from DB
	List<TimeTable> recursiveOptions=null;
	
	
	
	public void generateCopyOfPeriodsToTimeTableOutput(String tchrOrCls,TimeTable output) throws PeriodBackUpFailedException
	{
		if(output.getClassTchrLinkMap()==null)
			output.setClassTchrLinkMap(classTchrLinkMap);
		else
		{
			output.getClassTchrLinkMap().putAll(classTchrLinkMap);
		}
		if(this.periods==null)
		{
			output.setPeriods(this.periods);
			output.backUPPeriods(tchrOrCls);
		}
		else
		{
			copyandBackUpPeriods(output.getPeriods(),tchrOrCls);
		}
	}
	
	
	public void copyandBackUpPeriods(Map<String,Map<Integer,Period>> input,String tchrOrCls) throws PeriodBackUpFailedException
	{
		for(String day : input.keySet())
		{
			Map<Integer,Period> prds= periods.get(day);
			Map<Integer,Period> inputPeriod=input.get(day);
			for(Integer period : inputPeriod.keySet())
			{
				Period inputP=inputPeriod.get(period);
				Period pObj= prds.get(period);
				if(!pObj.isPeriodEmpty())
				{
					if(!inputP.isPeriodEmpty())
						inputP.unsetPeriod();//unset period
					
					try {
						inputP.setSubjectId(pObj.getSubjectId());
						inputP.setSubjectDesc(pObj.getSubjectDesc());
						inputP.setTeacherId(pObj.getTeacherId());
						inputP.setTeacherName(pObj.getTeacherName());
						inputP.setSgId(pObj.getSgId());
						inputP.backUpPeriod(tchrOrCls);
					} catch (PeriodNotEmptyException e) {
						// TODO Auto-generated catch block
						System.out.println("Error case..");
					}
					
				}
			}	
		}

	}
	
	public void clearandUpdatePeriodFromBk(String tchrOrCls) throws PeriodNotEmptyException, PeriodBackUpFailedException
	{
		for(String day : periods.keySet())
		{
			Map<Integer,Period> prds= periods.get(day);
			for(Integer period : prds.keySet())
			{
				Period prd = prds.get(period);
				prd.extractBackup(tchrOrCls);
					
			}
		}

	}
	public void backUPPeriods(String tchrOrCls) throws PeriodBackUpFailedException 
	{
		if(periods==null)
		{
			throw new PeriodBackUpFailedException("periods map is empty for this timetable");
		}
		for(String day : periods.keySet())
		{
			Map<Integer,Period> prds= periods.get(day);
			for(Integer period : prds.keySet())
			{
				Period prd = prds.get(period);
				prd.backUpPeriod(tchrOrCls);
			}
		}
	}
	public void makeTimeTableInvalid()
	{
		invalidIds.add(this.id);
		if(this.classTchrLinkMap!=null)
		{
			for(String id : this.classTchrLinkMap.keySet())
			{
				invalidIds.add(this.classTchrLinkMap.get(id));
			}
		}
	}
	public void updateClsTchrLinkMap(String tchrId,String clsId)
	{
		if(this.classTchrLinkMap==null )
			this.classTchrLinkMap= new HashMap<String,Long>();
		
		this.classTchrLinkMap.put(tchrId, this.id);
		this.classTchrLinkMap.put(clsId, this.id);
	}
	public void generateOptions(Subject subj, Teacher tchr,Classroom cls)throws CloneNotSupportedException, TimeTableExceptions
	{
		if(this.recursiveOptions==null)
		{
			List<TimeTable> options = new ArrayList<TimeTable>();
			TimeTable option=null;
			for(String day : this.periods.keySet())
			{
				Map<Integer,Period> periodMapPerDay =periods.get(day);
				boolean flag=true;
				for(Integer periodLocation : periodMapPerDay.keySet())
				{
					try
					{
						if(flag)//avoid creation for failure to update. 
						{
							option=new TimeTable(cls.getTimeTableFormat(),null);//indicated to get a running sequence no
							option.updateClsTchrLinkMap(tchr.getTeacherID(),cls.getClassId());
						}
						option.updatePeriod(day, periodLocation, subj, tchr, cls);
						options.add(option);
						flag=true;
					}
					catch(PeriodNotEmptyException e)
					{
						flag=false;
					}

				}
			}
			if(options.isEmpty())
				throw new TimeTableExceptions("Error data unable to generate basic options");
		this.recursiveOptions=options;
		}else
		{
			List<TimeTable> outerBuffer = new ArrayList<TimeTable>();
			for(TimeTable existingOption : this.recursiveOptions)
			{
				if(existingOption.isInvalid())// don not consider invalid cases
					continue;
				
				List<TimeTable> options = new ArrayList<TimeTable>();
				TimeTable option= null;
				for(String day : this.periods.keySet())
				{
					Map<Integer,Period> periodMapPerDay =periods.get(day);
					for(Integer periodLocation : periodMapPerDay.keySet())
					{
						if(existingOption.isCurrentPeriodEmpty(day, periodLocation))
						{
							option= new TimeTable(cls.getTimeTableFormat(),null,existingOption.getPeriods());
							existingOption.printTimeTable(day,periodLocation);
							option.setSaturdayFlag(existingOption.isSaturdayFlag());
							option.updateClsTchrLinkMap(tchr.getTeacherID(), cls.getClassId());
							option.updatePeriod(day, periodLocation, subj, tchr, cls);
							options.add(option);
							option.printTimeTable(day,periodLocation);
							option=null;
							existingOption.printTimeTable(day,periodLocation);
						}else
						{
							System.out.println("Filled");
							existingOption.printTimeTable(day,periodLocation);
						}
					}
				}
				if(options.isEmpty())
				{
					// note same issue as in generateCopyOfperiodsToTimeTableOutput 
					// since the clsTchrLinkMap in incorrect.. when the below method is called
					// only one set of ids get marked invalid and not all. 
					// this needs to be modified when generateCopyOfperiodsToTimeTableOutput method is modified
					existingOption.makeTimeTableInvalid();
				}
				else
					outerBuffer.addAll(options);
			}
			this.recursiveOptions.clear();
			this.recursiveOptions.addAll(outerBuffer);
		}
	}
	
	private boolean isCurrentPeriodEmpty(String day, int periodLoc)
	{
		Period p = this.periods.get(day).get(periodLoc);
		return p.isPeriodEmpty();
	}
	private void printTimeTable(String day,int period)
	{
		Period p = periods.get(day).get(period);
		if(!p.isBreakTime())
			System.out.println("Not Filled "+p.isPeriodEmpty());
		/*System.out.print("classID "+periodMap.get(periodLoc).getSgId());
				System.out.print("Subj Id "+periodMap.get(periodLoc).getSubjectId());
				System.out.print("Subj Desc "+periodMap.get(periodLoc).getSubjectDesc());
				System.out.print("Teacher Id "+periodMap.get(periodLoc).getTeacherId());
				System.out.print("Teacher Name "+periodMap.get(periodLoc).getTeacherName());*/
	}
	public void updatePeriod(String day,int period,Subject subj,Teacher tchr,Classroom cls)throws PeriodNotEmptyException
	{
		Period p = this.periods.get(day).get(period);
		if(p.isPeriodEmpty())
		{
			p.setSgId(cls.getClassId());
			p.setSubjectId(subj.getSubjectId());
			p.setSubjectDesc(subj.getSuperSubjDesc());
			p.setTeacherId(tchr.getTeacherID());
			p.setTeacherName(tchr.getTeacherName());
		}
		else
		{
			throw new PeriodNotEmptyException("Period not empty");
		}
	}
	
	public TimeTable(String format,Long seq)
	{
		this(format);
		if(seq==null)
		{
			this.id=Utils.nextCount();
		}
		else
		{
			this.id=seq;
		}
		this.classTchrLinkMap= new HashMap<String,Long>();
	}
	public TimeTable(String format,Long seq,Map<String,Map<Integer, Period>> periodMap)
	{
		this(format,seq);
		for(String day : this.periods.keySet())
		{
			Map<Integer, Period> localperiodMap= this.periods.get(day);
			for(int loc : localperiodMap.keySet())
			{
				localperiodMap.put(loc, periodMap.get(day).get(loc));
			}
		}
		
	}
	// Need to modify this to get this updated form DB
	public TimeTable(String format)
	{
		
		if(format.equalsIgnoreCase("Chinmaya"))
		{
			String[] day={Constants.MON,Constants.TUE,Constants.WED,Constants.THU,Constants.FRI};
			periods= new LinkedHashMap<String,Map<Integer,Period>>(6);
			Map<Integer,Period> perDayPeriods=null;
			for(int i=0;i<5;i++)
			{
				perDayPeriods=new LinkedHashMap<Integer,Period>(10);
				Period p= new Period(FIRST_PERIOD,"9.00 to 9.40",false,1);
				perDayPeriods.put(FIRST_PERIOD,p);
				p= new Period(SECOND_PERIOD,"9.40 to 10.20",false,1);
				perDayPeriods.put(SECOND_PERIOD,p);
				p= new Period(SHORT_BREAK,"10.20 to 10.30",true,0);
				perDayPeriods.put(SHORT_BREAK,p);
				p= new Period(THIRD_PERIOD,"10.30 to 11.00",false,2);
				perDayPeriods.put(THIRD_PERIOD,p);
				p= new Period(FOURTH_PERIOD,"11.10 to 11.50",false,2);
				perDayPeriods.put(FOURTH_PERIOD,p);
				p= new Period(FIFTH_PERIOD,"11.50 to 12,30",false,2);
				perDayPeriods.put(FIFTH_PERIOD,p);
				p= new Period(LUNCH_BREAK,"12.30 to 1.00",true,0);
				perDayPeriods.put(LUNCH_BREAK,p);
				p= new Period(SIXTH_PERIOD,"1.00 to 1.40",false,3);
				perDayPeriods.put(SIXTH_PERIOD,p);
				p= new Period(SEVENTH_PERIOD,"1.40 to 2.20",false,3);
				perDayPeriods.put(SEVENTH_PERIOD,p);
				p= new Period(EIGHT_PERIOD,"2.20 to 3.15",false,3);
				perDayPeriods.put(EIGHT_PERIOD,p);
				periods.put(day[i], perDayPeriods);
			}
			perDayPeriods=new LinkedHashMap<Integer,Period>();
			Period p= new Period(FIRST_PERIOD,"8.45 to 9.20",false,1);
			perDayPeriods.put(FIRST_PERIOD,p);
			p= new Period(SECOND_PERIOD,"9.20 to 9.55",false,1);
			perDayPeriods.put(SECOND_PERIOD,p);
			p= new Period(THIRD_PERIOD,"9.55 to 10.30",false,1);
			perDayPeriods.put(THIRD_PERIOD,p);
			p= new Period(SHORT_BREAK,"10.30 to 10.40",true,0);
			perDayPeriods.put(SHORT_BREAK,p);
			p= new Period(FOURTH_PERIOD,"10.40 to 11.15",false,2);
			perDayPeriods.put(FOURTH_PERIOD,p);
			p= new Period(FIFTH_PERIOD,"11.15 to 11.50",false,2);
			perDayPeriods.put(FIFTH_PERIOD,p);
			p= new Period(SIXTH_PERIOD,"11.50 to 12.25",false,3);
			perDayPeriods.put(SIXTH_PERIOD,p);
			p= new Period(SEVENTH_PERIOD,"12.25 to 1.00",false,3);
			perDayPeriods.put(SEVENTH_PERIOD,p);
			periods.put(Constants.SAT,perDayPeriods);
		}
	}
	
	
	public boolean isInvalid()
	{
		if(TimeTable.invalidIds.contains(id))
			return true;
		else return false;
	}
	
	public Integer getNoOfPeriodsEmpty(String day)
	{
		int count=0;
		if(this.periods!=null)
		{
			if(day.equals(SAT) && this.saturdayFlag)
			{
				Map<Integer,Period> periodMap= this.periods.get(day);
				for(Integer p : periodMap.keySet())
				{
					if(periodMap.get(p).isPeriodEmpty())
						count++;
				}
			}
		}
		return count;
	}
	

	/**
	 * @return the recursiveOptions
	 */
	public List<TimeTable> getRecursiveOptions() {
		return recursiveOptions;
	}

	/**
	 * @param recursiveOptions the recursiveOptions to set
	 */
	public void setRecursiveOptions(List<TimeTable> recursiveOptions) {
		this.recursiveOptions = recursiveOptions;
	}

	/**
	 * @return the classTchrLinkMap
	 */
	public Map<String, Long> getClassTchrLinkMap() {
		return classTchrLinkMap;
	}

	/**
	 * @param classTchrLinkMap the classTchrLinkMap to set
	 */
	public void setClassTchrLinkMap(Map<String, Long> classTchrLinkMap) {
		this.classTchrLinkMap = classTchrLinkMap;
	}

	/**
	 * @return the invalidId
	 */
	public static List<Long> getInvalidIds() {
		return invalidIds;
	}



	/**
	 * @param invalidId the invalidId to set
	 */
	public static void setInvalidIds(List<Long> invalidIds) {
		TimeTable.invalidIds = invalidIds;
	}



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the periods
	 */
	public Map<String, Map<Integer, Period>> getPeriods() {
		return periods;
	}

	/**
	 * @param periods the periods to set
	 */
	public void setPeriods(Map<String, Map<Integer, Period>> periods) {
		this.periods = periods;
	}

	/**
	 * @return the saturdayFlag
	 */
	public boolean isSaturdayFlag() {
		return saturdayFlag;
	}

	/**
	 * @param saturdayFlag the saturdayFlag to set
	 */
	public void setSaturdayFlag(boolean saturdayFlag) {
		this.saturdayFlag = saturdayFlag;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	

	@Override			
	   public boolean equals(Object obj) {
	       if (obj == null)
	           return false;
	       if (!(obj instanceof TimeTable))
	           return false;
	       TimeTable timetable = (TimeTable) obj;
	       if (timetable.id.equals(this.id))
	    	   return true;
	       else
	    	   return false;
	   } 
}
