package deadlyEbola;

import java.io.IOException;

/**
 * Main.java
 * Purpose: Running and Testing Algorithms
 *
 * @author Adel Salimi
 * @version 1.0 2018-01-02
 */

public class Main {
    
    public static void main(String[] args) throws IOException{
        //find genes of different ebola species
        FindGenes.findAndSaveGenes();
        //this function calculates and saves distance matrices of genes
        FindDistMats.findSaveDistMats();
        //implemented UPGMA for testing
        UPGMA.findUPGMA(Filer.readCSV("CSVs\\L.csv")
                        ,new String[]{"Zaire", "TaiForest", "Sudan", "Reston", "Bundibugyo"});
    } 
}
