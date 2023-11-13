import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) {
        try {
            FileReader grammarSource = new FileReader(args[args.length-1]);
            GrammarReader grammarReader = new GrammarReader(grammarSource);
            Grammar grammar = grammarReader.getGrammar();
            StringBuilder output = new StringBuilder();
            output.append("********** The Grammar **********\n");
            System.out.print("test" + output);
            output.append(grammar);
            // Compute first sets
            System.out.print("--------------------");
            grammar.setFirst();
            output.append("\n********** First **********\n" + grammar.stringFirst());
            // Compute follow sets
            grammar.setFollow();
            output.append("\n********** Follow **********\n" + grammar.stringFollow());
            // Compute action table
            grammar.setActionTable();

            // Print action table
            if (args.length > 0 && args[0].equals("-pat")) {
                System.out.println(grammar.stringActionTable());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}