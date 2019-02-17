
package lib;


import java.util.Comparator;


/*
 * License
 */

/**
 *
 * @author patrik
 */
public class StringComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        int diff = o1.length() - o2.length();
        if(diff == 0){
            return o1.compareTo(o2);
        }
        else{
            return diff / Math.abs(diff);
        }
    }
}
