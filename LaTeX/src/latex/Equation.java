package latex;

import java.util.HashMap;

/**
 *
 * @author groowy
 */
public class Equation {
    private HashMap<String, String> equationHashMap;
    private String equation;
    private String result;
    private String comment;
    private String category;
    private String doneBy;
    
    public Equation(){
        this.equationHashMap = new HashMap();
        equationHashMap.put("equation", "");
        equationHashMap.put("result", "");
        equationHashMap.put("comment", "");
        equationHashMap.put("category", "");
        equationHashMap.put("done_by", "");
    }
    
    public Equation(String equation, String result, String comment, String category, String doneBy){
        this.equationHashMap = new HashMap();
        equationHashMap.put("equation", "");
        equationHashMap.put("result", "");
        equationHashMap.put("comment", "");
        equationHashMap.put("category", "");
        equationHashMap.put("done_by", "");
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
        equationHashMap.put("equation", this.equation);
        equationHashMap.put("result", this.result);
        equationHashMap.put("comment", this.comment);
        equationHashMap.put("category", this.category);
        equationHashMap.put("done_by", this.doneBy);
        return equationHashMap;
    }

    /**
     * @param equationHashMap the equationHashMap to set
     */
    public void setEquationHashMap(HashMap<String, String> equationHashMap) {
        this.equationHashMap = equationHashMap;
    }

    /**
     * @return the equation
     */
    public String getEquation() {
        equation = equationHashMap.get("equation");
        return equation;
    }

    /**
     * @param equation the equation to set
     */
    public void setEquation(String equation) {
        equationHashMap.put("equation", "xxx");
        this.equation = equation;
        equationHashMap.put("equation", this.equation);
    }

    /**
     * @return the result
     */
    public String getResult() {
        result = equationHashMap.get("result");
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
        equationHashMap.put("result", this.result);
    }

    /**
     * @return the comment
     */
    public String getComment() {
        comment = equationHashMap.get("comment");
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        equationHashMap.put("comment", this.comment);
        this.comment = comment;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        category = equationHashMap.get("category");
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
        equationHashMap.put("category", this.category);
    }

    /**
     * @return the doneBy
     */
    public String getDoneBy() {
        doneBy = equationHashMap.get("done_by");
        return doneBy;
    }

    /**
     * @param doneBy the doneBy to set
     */
    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
        equationHashMap.put("done_by", this.doneBy);
    }   
}
