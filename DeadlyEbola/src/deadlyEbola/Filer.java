
package deadlyEbola;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Filer.java
 * Purpose: Handles working with files
 *
 * @author Adel Salimi
 * @version 1.0 2018-01-02
 */

public class Filer {
    
    public static ArrayList<String> readFASTA(String fileName) {
        ArrayList<String> reads = new ArrayList<>();
        try {
            try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
                String line;
                String temp;
                while ((line = input.readLine()) != null) {
                    temp = "";
                    if (line.charAt(0) != '>') {
                        temp += line;
                        while ((line = input.readLine()) != null) {
                            if (line.compareTo("") != 0) {
                                if (line.charAt(0) != '>') {
                                    temp += line;
                                } else {
                                    break;
                                }
                            }

                        }
                        reads.add(temp);
                    }
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return reads;
    }

    public static void writeFASTA(ArrayList<String> s, String[] titles, String fileName) {
        String res = "";
        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            res += ">" + title + '\n';
            int numlines = (s.get(i).length() / 70);
            for (int j = 0; j < numlines; j++) {
                res += s.get(i).substring(j * 70, ((j + 1) * 70) > s.get(i).length() ? s.get(i).length() - 1 : (j + 1) * 70);
                res += '\n';
            }
        }

        saveFile(res, fileName);
    }

    public static void saveFile(String result, String fileName) {
        try {
            Writer writer = new BufferedWriter(new FileWriter(fileName));
            try {
                writer.write(result);
            } finally {
                writer.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void writeCSV(int[][] mat, String path) throws IOException {
        //String k ="https://www.callicoder.com/java-read-write-csv-file-opencsv/";
        
         try (
            PrintWriter br = new PrintWriter(new FileWriter(path));
        ) {
             String res = "";
             //br.append("sep=,\n");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mat.length; i++) {
                
                for (int j = 0; j < mat[0].length; j++) {
                    sb.append(mat[i][j]+((j!=mat[0].length-1)?",":""));     
                }
                if (i!= mat.length-1) sb.append("\n");
            }
            br.write(sb.toString());
            br.close();
        }
        

    }
    public static int[][] readCSV(String path) {
        int[][] mat = new int[5][5];
        ArrayList<String> reads = new ArrayList<>();
        try {
            try (BufferedReader input = new BufferedReader(new FileReader(path))) {
                String line;
                int i =0;
                int j=0;
                input.readLine();//not reading the first line
                while ((line = input.readLine()) != null) {
                    j=0;
                    for (StringTokenizer stringTokenizer = new StringTokenizer(line,","); stringTokenizer.hasMoreTokens();) {
                        
                        String token = stringTokenizer.nextToken();
                        mat[i][j] = Integer.parseInt(token);
                        j++;
                    }
                    i++;
                }
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return mat;
    }
}
