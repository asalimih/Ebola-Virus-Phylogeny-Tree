package deadlyEbola;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Filer.java Purpose: Calculating edit distance of every two Ebola Species
 * genes
 *
 * @author Adel Salimi
 * @version 1.0 2018-01-02
 */
public class FindDistMats {

    //this function calculates and saves distance matrices

    public static int[][][] findSaveDistMats() throws IOException {

        //this names are used for reading the files and also printing results
        String[] geneNames = new String[]{"NP", "VP35", "VP40", "GP", "VP30", "VP24", "L"};
        String[] genomeNames = new String[]{"Zaire", "TaiForest", "Sudan", "Reston", "Bundibugyo"};

        List<ArrayList<String>> genes = new ArrayList<>();

        //reading different ebola species genes from files
        for (String genomeName : genomeNames) {
            //genes.add(Filer.readFASTA("RealGenes\\" + genomeName + "_Genes.fasta"));
            genes.add(Filer.readFASTA("Founded_Genes\\" + genomeName + "_Genes.fasta"));
        }

        //calculating distance matrices based on globalAligner
        int[][][] distMats = new int[7][5][5];
        for (int g = 0; g < geneNames.length; g++) {
            for (int i = 0; i < 4; i++) {
                for (int j = i + 1; j < 5; j++) {
                    distMats[g][i][j] = GlobAlginer.getEditDist(genes.get(i).get(g), genes.get(j).get(g));
                    distMats[g][j][i] = distMats[g][i][j];
                }
            }
            //saving results as csv
            Filer.writeCSV(distMats[g], "RealCSVs\\" + geneNames[g] + ".csv");
        }

        return distMats;
    }

    //printing a matrix
    public static void showMat(int[][] mat) {
        for (int row = 0; row < mat.length; row++) {
            for (int col = 0; col < mat[0].length; col++) {
                System.out.printf("%5d", mat[row][col]);
            }
            System.out.println();
        }
    }
}
