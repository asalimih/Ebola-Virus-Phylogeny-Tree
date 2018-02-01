package deadlyEbola;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * SemiGlobAligner.java 
 * Purpose: Global-Local Aligner
 *
 * @author Adel Salimi
 * @version 1.0 2018-01-02
 */

public class SemiGlobAligner {

    public static ArrayList<Integer[]> findBestMatch(String str1, String str2, int offset) {

        ArrayList<Integer[]> results = new ArrayList<>();
        char[] str1Chars = str1.toCharArray();
        char[] str2Chars = str2.toCharArray();

        int x_length = str1Chars.length + 1;
        int y_length = str2Chars.length + 1;

        int[] costs = new int[3];

        int[][] s = new int[x_length][y_length];
        int[][] path = new int[x_length][y_length];
        
        for (int i = 0; i < x_length; i++) {
            s[i][0] = i;
        }
        for (int j = 1; j < y_length; j++) {
            s[0][j] = 0;
        }

        int bestCost = Integer.MAX_VALUE;
        int[] bestMatch = new int[2];
        int[] leftEdits = new int[x_length];//0=gap 1=mutate/match
        Arrays.fill(leftEdits, 1);

        int upEdit = 1;
        for (int j = 1; j < y_length; j++) {
            for (int i = 1; i < x_length; i++) {
                Arrays.fill(costs, 0);
                costs[0] = s[i - 1][j] + 1;//gap
                costs[1] = s[i][j - 1] + 1;//gap
                costs[2] = s[i - 1][j - 1] + (str1Chars[i - 1] == str2Chars[j - 1] ? 0 : 1);

                //finding minimum
                int minInd = -1;
                int minCost = 0;
                for (int m = 0; m < 3; m++) {
                    if (minInd < 0) {
                        minInd = m;
                        minCost = costs[m];
                    } else if (costs[m] < minCost) {
                        minInd = m;
                        minCost = costs[m];
                    }
                }
                s[i][j] = costs[minInd];
                upEdit = (minInd == 2) ? 1 : 0;
                leftEdits[i] = upEdit;
                if (minInd == 0) {
                    path[i][j] = path[i - 1][j];
                } else if (minInd == 1) {
                    path[i][j] = path[i][j - 1] + 1;
                } else if (minInd == 2) {
                    path[i][j] = path[i - 1][j - 1] + 1;
                }
            }
            //System.out.println(s[x_length-1][j]);

            if (bestCost > s[x_length - 1][j]) {
                bestMatch[0] = j - path[x_length - 1][j];
                bestMatch[1] = j;
                bestCost = s[x_length - 1][j];
                results.clear();
                results.add(new Integer[]{bestMatch[0] + offset, bestMatch[1] + offset});
            } else if (bestCost == s[x_length - 1][j]) {
                bestMatch[0] = j - path[x_length - 1][j];
                bestMatch[1] = j;
                results.add(new Integer[]{bestMatch[0] + offset, bestMatch[1] + offset});
            }
            upEdit = 1;
        }
        return results;
    }
}
