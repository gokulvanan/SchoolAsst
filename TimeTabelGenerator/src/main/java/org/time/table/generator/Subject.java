package org.time.table.generator;


public class Subject {
	
	private String subjectId=null;// Subject code given for each subject may be common across classes
	private String subjDesc=null; // Description for subj
	private boolean subSubj=false; // true for hindi as they come under 2nd lang etc. 
	private String superSubjId=null;// super subjId incase for hindi 2nd lang its 2nd Lang
	private String superSubjDesc=null;// super subj desc
	private Integer subjectImportance=0; // importance for slot priority in timetable
	
	
	public Subject (String subId)
	{
		this.subjectId=subId;
	}
	
	public Subject (String subId,String subjDesc,boolean commonSub,String superSubId, String superSubDesc,Integer subjImp)
	{
		this.subjectId=subId;
		this.subjDesc=subjDesc;
		this.subSubj=commonSub;
		this.superSubjId=superSubId;
		this.superSubjDesc=superSubDesc;
		this.subjectImportance=subjImp;
	}
	
	
	/**
	 * @return the subjectId
	 */
	public String getSubjectId() {
		return subjectId;
	}

	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	/**
	 * @return the subjDesc
	 */
	public String getSubjDesc() {
		return subjDesc;
	}

	/**
	 * @param subjDesc the subjDesc to set
	 */
	public void setSubjDesc(String subjDesc) {
		this.subjDesc = subjDesc;
	}

	/**
	 * @return the subSubj
	 */
	public boolean isSubSubj() {
		return subSubj;
	}

	/**
	 * @param subSubj the subSubj to set
	 */
	public void setSubSubj(boolean subSubj) {
		this.subSubj = subSubj;
	}

	/**
	 * @return the superSubjId
	 */
	public String getSuperSubjId() {
		return superSubjId;
	}

	/**
	 * @param superSubjId the superSubjId to set
	 */
	public void setSuperSubjId(String superSubjId) {
		this.superSubjId = superSubjId;
	}

	/**
	 * @return the superSubjDesc
	 */
	public String getSuperSubjDesc() {
		return superSubjDesc;
	}

	/**
	 * @param superSubjDesc the superSubjDesc to set
	 */
	public void setSuperSubjDesc(String superSubjDesc) {
		this.superSubjDesc = superSubjDesc;
	}

	/**
	 * @return the subjectImportance
	 */
	public Integer getSubjectImportance() {
		return subjectImportance;
	}

	/**
	 * @param subjectImportance the subjectImportance to set
	 */
	public void setSubjectImportance(Integer subjectImportance) {
		this.subjectImportance = subjectImportance;
	}

	@Override			
	   public boolean equals(Object obj) {
	       if (obj == null)
	           return false;
	       if (!(obj instanceof Subject))
	           return false;
	       Subject sub = (Subject) obj;
	       if (sub.subjectId.equals(this.subjectId))
	    	   return true;
	       else
	    	   return false;
	   } 
	 
	  /* (non-Javadoc)
	    * @see java.lang.Object#hashCode()
	    */
	   @Override
	   public int hashCode() {
	       final int PRIME = 31;
	       int result = 1;
	       result = PRIME * result*((Double)Math.random()).intValue();
	       return result;
	   }

	   

}
