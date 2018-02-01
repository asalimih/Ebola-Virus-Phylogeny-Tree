package deadlyEbola;
import java.util.Arrays;

/**
 * GlobAligner.java
 * Purpose: Needleman-Wunsch Algorithm
 *
 * @author Adel Salimi
 * @version 1.0 2018-01-02
 */

public class GlobAlginer {
    
    public static int getEditDist(String str1,  String str2) {
        char[] str1Chars = str1.toCharArray();
        char[] str2Chars = str2.toCharArray();

        int x_length = str1Chars.length+1;
        int y_length = str2Chars.length+1;

        int[] costs = new int[3];
        
        int[][] s = new int[x_length][y_length];
        for(int i=0;i<x_length;i++) s[i][0] = i;
        for(int j=1;j<y_length;j++) s[0][j] = j;

        for(int j=1;j<y_length;j++) {
            for(int i=1;i<x_length;i++) {
                Arrays.fill(costs, 0);           
                costs[0] = s[i-1][j] + 1;//gap
                costs[1] = s[i][j-1] + 1;//gap
                costs[2] = s[i-1][j-1] + (str1Chars[i-1]==str2Chars[j-1]?0:1);
                s[i][j]=Math.min(costs[0], Math.min(costs[1], costs[2]));
            }
    }
        return s[x_length-1][y_length-1];
    }
}
