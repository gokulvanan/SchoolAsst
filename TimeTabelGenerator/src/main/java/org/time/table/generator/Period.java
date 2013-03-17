package org.time.table.generator;

import java.util.HashMap;
import java.util.Map;

import org.time.table.exceptions.PeriodBackUpFailedException;
import org.time.table.exceptions.PeriodNotEmptyException;



public class Period implements Cloneable{

	private Integer periodId;
	private boolean periodEmpty=true;
	private Integer slot;
	private String time;
	boolean breakTime=false;
	private String subjectId;
	private String subjectDesc;
	private String teacherId;
	private String teacherName;
	private String sgId;
	private boolean tchrOptFilledFlag=false;
	private boolean clsOptFilledFlag=false;
	private Map<String,Period> backupPeriod=null;

	
	public void backUpPeriod(String tchrOrCls) throws PeriodBackUpFailedException
	{
		if(!this.periodEmpty)
		{
			if(tchrOrCls.equals(Constants.TEACHER))
			{
				this.tchrOptFilledFlag=true;
			}else if(tchrOrCls.equals(Constants.CLASS))
			{
				this.clsOptFilledFlag=true;
			}
			else
			{
				throw new PeriodBackUpFailedException("wrong case flag");
			}

			if(backupPeriod!=null)
			{
				backupPeriod.put(tchrOrCls, this);
			}
			else
			{
				backupPeriod= new HashMap<String, Period>();
				backupPeriod.put(tchrOrCls, this);
			}

		}
	}
	
	
	public Period(Integer id,String time, boolean breakTime,Integer slot)
	{
		this.periodId=id;
		this.time=time;
		this.breakTime=breakTime;
		this.periodEmpty=!breakTime;
		this.slot=slot;
	}

	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(String subjectId)throws PeriodNotEmptyException {
		if(this.periodEmpty)
		{
			this.periodEmpty=false;
			this.subjectId = subjectId;
		}
		else
			throw new PeriodNotEmptyException("");
	}
	/**
	 * @param subjectDesc the subjectDesc to set
	 */
	public void setSubjectDesc(String subjectDesc) {
			this.subjectDesc = subjectDesc;
	}


	/**
	 * @param subjectId the subjectId to set
	 */
	public void unsetPeriod() {
		this.periodEmpty=true;
		this.subjectId = "";
		this.subjectDesc="";
		this.teacherId="";
		this.teacherName="";
		this.sgId="";
	}

	public void setPeriod(String sgId,String tchrId,String tchrName,String subjId,String subjDesc) throws PeriodNotEmptyException
	{
		if(this.periodEmpty)
		{
			this.sgId= sgId;
			this.teacherId=tchrId;
			this.teacherName=tchrName;
			this.subjectId=subjId;
			this.subjectDesc=subjDesc;
			this.periodEmpty=false;
		}
		else
			throw new PeriodNotEmptyException("");
	}
	public void extractBackup(String tchrOrCls) throws PeriodBackUpFailedException
	{
		if(this.backupPeriod!=null && !this.backupPeriod.isEmpty())
		{
			Period prd = backupPeriod.remove(tchrOrCls);
			if(prd!=null)
			{
				if(tchrOrCls.equals(Constants.TEACHER))
					this.tchrOptFilledFlag=false;
				else if(tchrOrCls.equals(Constants.CLASS))
					this.clsOptFilledFlag=false;

				//set period
				this.sgId= prd.sgId;
				this.teacherId=prd.teacherId;
				this.teacherName=prd.teacherName;
				this.subjectId=prd.subjectId;
				this.subjectDesc=prd.subjectDesc;
			}
			else if(this.tchrOptFilledFlag || this.clsOptFilledFlag)
			{
				this.unsetPeriod();
			}
			
		}
		else
			throw new PeriodBackUpFailedException("Error in extracting backups");
		
	}
	
	/**
	 * @return the tchrOptFilledFlag
	 */
	public boolean isTchrOptFilledFlag() {
		return tchrOptFilledFlag;
	}

	/**
	 * @param tchrOptFilledFlag the tchrOptFilledFlag to set
	 */
	public void setTchrOptFilledFlag(boolean tchrOptFilledFlag) {
		this.tchrOptFilledFlag = tchrOptFilledFlag;
	}

	/**
	 * @return the clsOptFilledFlag
	 */
	public boolean isClsOptFilledFlag() {
		return clsOptFilledFlag;
	}

	/**
	 * @param clsOptFilledFlag the clsOptFilledFlag to set
	 */
	public void setClsOptFilledFlag(boolean clsOptFilledFlag) {
		this.clsOptFilledFlag = clsOptFilledFlag;
	}

	/**
	 * @return the backupPeriod
	 */
	public Map<String, Period> getBackupPeriod() {
		return backupPeriod;
	}

	/**
	 * @param backupPeriod the backupPeriod to set
	 */
	public void setBackupPeriod(Map<String, Period> backupPeriod) {
		this.backupPeriod = backupPeriod;
	}

	/**
	 * @return the teacherId
	 */
	public String getTeacherId() {
		return teacherId;
	}

	/**
	 * @param teacherId the teacherId to set
	 */
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	/**
	 * @return the teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * @param teacherName the teacherName to set
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	/**
	 * @return the sgId
	 */
	public String getSgId() {
		return sgId;
	}

	/**
	 * @param sgId the sgId to set
	 */
	public void setSgId(String sgId) {
		this.sgId = sgId;
	}
	/**
	 * @return the periodEmpty
	 */
	public boolean isPeriodEmpty() {
		return periodEmpty;
	}	
	
	/**
	 * @return the subjectId
	 */
	public String getSubjectId() {
		return subjectId;
	}

	/**
	 * @return the subjectDesc
	 */
	public String getSubjectDesc() {
		return subjectDesc;
	}

	/**
	 * @return the slot
	 */
	public Integer getSlot() {
		return slot;
	}



	/**
	 * @return the breakTime
	 */
	public boolean isBreakTime() {
		return breakTime;
	}


	/**
	 * @return the periodId
	 */
	public Integer getPeriodId() {
		return periodId;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
		
}
