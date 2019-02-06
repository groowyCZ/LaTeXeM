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

    /**
     * @return the equationHashMap
     */
    public HashMap<String, String> getEquationHashMap() {
        HashMap<String, String> equationHashMap = new HashMap();
        equationHashMap.put("equation", this.equation);
        equationHashMap.put("result", this.result);
        equationHashMap.put("comment", this.comment);
        equationHashMap.put("category", this.category);
        equationHashMap.put("done_by", this.doneBy);
        return equationHashMap;
    }

    /**
     * Set equation from (partial) HashMap.
     * @param equationHashMap the equationHashMap to set
     */
    public void setEquationHashMap(HashMap<String, String> equationHashMap) {
        
        // FIELDS
        String equation = equationHashMap.get("equation");
        if(equation != null){this.equation = equation;}
        String result = equationHashMap.get("result");
        if(result != null){this.result = result;}
        String comment = equationHashMap.get("comment");
        if(comment != null){this.comment = comment;}
        String category = equationHashMap.get("category");
        if(category != null){this.category = category;}
        String doneBy = equationHashMap.get("doneBy");
        if(doneBy != null){this.doneBy = doneBy;}
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
