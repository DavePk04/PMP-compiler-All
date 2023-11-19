import java.io.*;
import java.util.TreeMap;
import java.util.Map;
/**
 * @author SHEHRZAD Mohammad Amir - 000480454
 * Analyzer Class : Lexcial analyzer of the Fortress
 */
public class Analyzer {
    private final LexicalAnalyzer analyzer;
    public Analyzer(FileReader file) {
        this.analyzer = new LexicalAnalyzer(file);
    }

    /**
     * @return analyzer
     */
    public LexicalAnalyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * Output the token and its lexical unit.
     * @throws IOException
     */
    public void printAnalyze() throws IOException{
        try {
            TreeMap<String, Integer> varTreeMap = new TreeMap<>(); // TreeMap to keep "variables" and "line number
            Symbol symbol = analyzer.nextSymbol();  // Select the current token
            Terminal terminal = symbol.getTerminal();
            while (terminal != Terminal.EOS) {
                System.out.println(symbol);
                symbol = analyzer.nextSymbol();    // Select the next token
                terminal = symbol.getTerminal();
                if (terminal == Terminal.VARNAME) {
                    String varName = symbol.getValue().toString();
                    varTreeMap.putIfAbsent(varName, symbol.getLine());
                }
            }
            System.out.println("\nVariables");
            for(Map.Entry<String, Integer> variable : varTreeMap.entrySet())
                System.out.println(variable.getKey()+"\t"+variable.getValue());

        }
        catch (FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
