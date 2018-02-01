package deadlyEbola;
import java.util.ArrayList;

/**
 * FindGenes.java
 * Purpose: Extracts EbolaVirus genes based on MarburgVirus genes
 * 
 * @author Adel Salimi
 * @version 1.0 2018-01-02
 */

public class FindGenes {
    
    static int CHECK_OFFSET = 500;//start of the range of genome to be checked has an offset
    
    public static void findAndSaveGenes() {
        //reading marburg genes
        ArrayList<String> marGenes = Filer.readFASTA("Marburg_Genes.fasta");

        //this names are used for reading the files and also printing results
        String[] geneNames = new String[]{"NP", "VP35", "VP40", "GP", "VP30", "VP24", "L"};
        String[] genomeNames = new String[]{"Zaire", "TaiForest", "Sudan", "Reston", "Bundibugyo"};

        //reading different ebola species genomes from files
        ArrayList<String> ebolaGenomes = new ArrayList<>();
        for (String genomeName : genomeNames) {
            ebolaGenomes.add(Filer.readFASTA("Genomes\\" +genomeName + "_genome.fasta").get(0));
        }

        //zairePos includes actual positions of zaire virus genes. used for testing
        ArrayList<Integer[]> zairePos = actualZaireGenePoses();

        int n = 0;//index of the current genome
        int m = 0;//index of the current gene
        int preGeneEnd = 500;//holds end index of previous gene
        int beginCheck, endCheck;//the range of genome that we align the gene
        int sumDiff = 0;//holds the total difference between results and actual data. its for testing
        int diff = 0;//holds difference of  the current gene's result data abd actual data. its for testing
        ArrayList<Integer[]> res;//results of each alignment will be assigned to this (ranges of alignment)
        ArrayList<String> geneList = new ArrayList<>();//extracted genes are collected here to be saved as a file later
        for (String ebolaGenome : ebolaGenomes) {
            System.out.println(genomeNames[n] + "'s gene positions: ");
            m = 0;
            preGeneEnd = 500;
            for (String marGene : marGenes) {
                beginCheck = (preGeneEnd - CHECK_OFFSET);
                endCheck = (preGeneEnd - CHECK_OFFSET) + marGene.length() * 2;
                if (endCheck >= ebolaGenome.length()) {
                    endCheck = ebolaGenome.length() - 1;
                }

                res = SemiGlobAligner.findBestMatch(marGene,
                        ebolaGenome.substring(beginCheck, endCheck), beginCheck);
                Integer[] range = res.get(0);
                System.out.printf("%5s:%6d %6d", geneNames[m], range[0], range[1]);//printing the range that the gene is aligned to
                geneList.add(ebolaGenome.substring(range[0], range[1]));//founded gene is collected
//                diff=(Math.abs(range[0]-zairePos.get(m)[0])+Math.abs(range[1]-zairePos.get(m)[1]));
//                sumDiff += diff;
//                System.out.printf("  Dif="+diff);
                System.out.println("");
                preGeneEnd = range[1];
                m++;
            }

            Filer.writeFASTA(geneList, geneNames, genomeNames[n] + "_Genes.fasta");
            geneList.clear();
            n++;
        }
//        System.out.println("Total Diff: " + sumDiff);

    }
    
    public static ArrayList<Integer[]> actualZaireGenePoses() {
        ArrayList<Integer[]> zairePos = new ArrayList<>();
        zairePos.add(new Integer[]{56, 3026});
        zairePos.add(new Integer[]{3032, 4407});
        zairePos.add(new Integer[]{4390, 5894});
        zairePos.add(new Integer[]{5900, 8305});
        zairePos.add(new Integer[]{8288, 9740});
        zairePos.add(new Integer[]{9885, 11518});
        zairePos.add(new Integer[]{11501, 18282});
        return zairePos;
    }
}
