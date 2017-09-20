package com.xionger.qcb.model;

public class StockConcept extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

    private String conceptName;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName == null ? null : conceptName.trim();
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }

}