package deadlyEbola;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * UPGMA.java Purpose: UPGMA Algorithm
 *
 * @author Adel Salimi
 * @version 1.0 2018-01-02
 */
public class UPGMA {

    public static void findUPGMA(int[][] distMat, String[] name) {

        ArrayList<String> names = new ArrayList<>();
        names.addAll(Arrays.asList(name));

        int n = names.size();
        ArrayList<ArrayList<Float>> dMat = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            dMat.add(new ArrayList<>());
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dMat.get(i).add(new Float(distMat[i][j]));
            }
        }

        ArrayList<Integer> clsize = new ArrayList<>();//cluster sizes
        for (int i = 0; i < n; i++) {
            clsize.add(1);
        }

        //------------------------------------clustering
        for (int m = 0; m < n - 1; m++) {
            //finding minimum
            int minInd[] = new int[2];
            minInd[0] = 0;
            minInd[1] = 1;
            float min = dMat.get(0).get(1);
            for (int i = 0; i < dMat.size(); i++) {
                for (int j = i + 1; j < dMat.get(0).size(); j++) {
                    if (dMat.get(i).get(j) < min) {
                        min = dMat.get(i).get(j);
                        minInd[0] = i;
                        minInd[1] = j;
                    }
                }
            }

            //updating matrix
            float tmp = 0;
            for (int i = 0; i < dMat.size(); i++) {
                if (i != minInd[0] && i != minInd[1]) {
                    tmp = (dMat.get(minInd[0]).get(i) * clsize.get(minInd[0])
                            + dMat.get(minInd[1]).get(i) * clsize.get(minInd[1]))
                            / (clsize.get(minInd[0]) + clsize.get(minInd[1]));
                    dMat.get(i).set(minInd[0], tmp);
                    dMat.get(minInd[0]).set(i, tmp);
                }
            }

            clsize.set(minInd[0], clsize.get(minInd[0]) + clsize.get(minInd[1]));

            //removing a row
            dMat.remove(minInd[1]);
            //removing a column
            dMat.stream().forEach(row -> row.remove(minInd[1]));

            clsize.remove(minInd[1]);

            String[] sorter = new String[2];
            sorter[0] = names.get(minInd[0]);
            sorter[1] = names.get(minInd[1]);

            if (sorter[0].compareTo(sorter[1]) < 0) {
                names.set(minInd[0], "(" + names.get(minInd[0]) + "," + names.get(minInd[1]) + ")");
            } else {
                names.set(minInd[0], "(" + names.get(minInd[1]) + "," + names.get(minInd[0]) + ")");
            }
            names.remove(minInd[1]);

            System.out.println((int) min + " " + names.get(minInd[0]));
        }
    }
}
