package latex;

import java.util.HashMap;

/**
 *
 * @author groowy
 */
public class Equation {
    private String equation;
    private String result;
    private String comment;
    private String category;
    private String doneBy;
    
    public Equation(){
        this.equation = "";
        this.result = "";
        this.comment = "";
        this.category = "";
        this.doneBy = "";
    }
    
    public Equation(String equation, String result, String comment, String category, String doneBy){
        this.equation = equation;
        this.result = result;
        this.comment = comment;
        this.category = category;
        this.doneBy = doneBy;
    }
    
    public Equation(HashMap<String, String> equationHashMap){
        
        // FIELDS
        String equation = equationHashMap.get("body");
        this.equation = (equation == null ? "" : equation);
        String result = equationHashMap.get("result");
        this.result = (result == null ? "" : result);
        String comment = equationHashMap.get("comment");
        this.comment = (comment == null ? "" : comment);
        String category = equationHashMap.get("category");
        this.category = (category == null ? "" : category);
        String doneBy = equationHashMap.get("done_by");
        this.doneBy = (doneBy == null ? "" : doneBy);
    }

    /**
     * @return the equationHashMap
     */
    public HashMap<String, String> asHashMap() {
        HashMap<String, String> equationHashMap = new HashMap();
        equationHashMap.put("body", this.equation);
        equationHashMap.put("result", this.result);
        equationHashMap.put("comment", this.comment);
        equationHashMap.put("category", this.category);
        equationHashMap.put("done_by", this.doneBy);
        return equationHashMap;
    }

    /**
     * @return the equation
     */
    public String getEquation() {
        return this.equation;
    }

    /**
     * @param equation the equation to set
     */
    public void setEquation(String equation) {
        this.equation = equation;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return this.result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the doneBy
     */
    public String getDoneBy() {
        return this.doneBy;
    }

    /**
     * @param doneBy the doneBy to set
     */
    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
    }   
}
