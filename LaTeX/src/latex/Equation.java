package latex;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<String> doneBy;
    
    private String listToString(ArrayList<String> list, String sep){
        String str = "";
        for (int i = 0; i < list.size() - 1; i++) {
            str += list.get(i) + sep;
        }if(list.size() > 0){
            str += list.get(list.size() - 1);
        }
        return str;
    }
    
    private ArrayList<String> stringToList(String str, String sep){
        ArrayList<String> list = new ArrayList(Arrays.asList(str.split(sep)));
        return list;
    }
    
    public Equation(){
        this.equation = "";
        this.result = "";
        this.comment = "";
        this.category = "";
        this.doneBy = new ArrayList();
    }
    
    public Equation(String equation, String result, String comment, String category, ArrayList<String> doneBy){
        this.equation = equation;
        this.result = result;
        this.comment = comment;
        this.category = category;
        this.doneBy = doneBy;
    }
    
    public Equation(HashMap<String, String> equationHashMap, String sep){
        
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
        this.doneBy = (doneBy == null ? new ArrayList() : stringToList(doneBy, sep));
    }

    /**
     * @param sep
     * @return the equationHashMap
     */
    public HashMap<String, String> asHashMap(String sep) {
        HashMap<String, String> equationHashMap = new HashMap();
        equationHashMap.put("body", this.equation);
        equationHashMap.put("result", this.result);
        equationHashMap.put("comment", this.comment);
        equationHashMap.put("category", this.category);
        equationHashMap.put("done_by", listToString(this.doneBy, sep));
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
    public ArrayList<String> getDoneBy() {
        return this.doneBy;
    }

    /**
     * @param doneBy the doneBy to set
     */
    public void setDoneBy(ArrayList<String> doneBy) {
        this.doneBy = doneBy;
    }   
}
