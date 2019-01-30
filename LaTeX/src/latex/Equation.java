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
        this.equation = equation;
        this.result = result;
        this.comment = comment;
        this.category = category;
        this.doneBy = doneBy;
        this.equationHashMap = new HashMap();
        equationHashMap.put("equation", "");
        equationHashMap.put("result", "");
        equationHashMap.put("comment", "");
        equationHashMap.put("category", "");
        equationHashMap.put("done_by", "");
        /** 
         * Java 9:
         * // this works for up to 10 elements:
         * Map<String, String> test1 = Map.of(
         *     "a", "b",
         *     "c", "d"
         * );
         * 
         * // this works for any number of elements:
         * Map<String, String> test2 = Map.ofEntries(
         *     entry("a", "b"),
         *     entry("c", "d")
         * );
         */
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
     * Set equation from (partial) HashMap.
     * @param equationHashMap the equationHashMap to set
     */
    public void setEquationHashMap(HashMap<String, String> equationHashMap) {
        
        // HASHMAP
        this.equationHashMap = equationHashMap;
        
        
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
        return equationHashMap.get("equation");
    }

    /**
     * @param equation the equation to set
     */
    public void setEquation(String equation) {
        this.equation = equation;
        equationHashMap.put("equation", this.equation);
    }

    /**
     * @return the result
     */
    public String getResult() {
        return equationHashMap.get("result");
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
        return equationHashMap.get("comment");
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
        equationHashMap.put("comment", this.comment);
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return equationHashMap.get("category");
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
        return equationHashMap.get("done_by");
    }

    /**
     * @param doneBy the doneBy to set
     */
    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
        equationHashMap.put("done_by", this.doneBy);
    }   
}
