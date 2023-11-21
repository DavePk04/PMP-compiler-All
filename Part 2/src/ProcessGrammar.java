import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProcessGrammar {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ProcessGrammar <-wat|-pat> <output_file> <grammar_file>");
            return;
        }

        try {
            FileReader grammarSource = new FileReader(args[args.length - 1]);
            GrammarReader grammarReader = new GrammarReader(grammarSource);
            Grammar grammar = grammarReader.getGrammar();
            StringBuilder output = new StringBuilder();
            output.append("********** The Grammar **********\n");
            output.append(grammar);
            grammar.setFirst();
            output.append("\n********** First **********\n" + grammar.stringFirst());
            grammar.setFollow();
            output.append("\n********** Follow **********\n" + grammar.stringFollow());

            grammar.setActionTable();
            output.append("\n********** Action table **********\n");

            if ("-wat".equals(args[0])) {
                output.append(grammar.stringActionTable());
                // Write action table to the specified file
                FileManager.writeFile(args[1], output);
            } else if ("-pat".equals(args[0])) {
                // Print action table to the console
                output.append(grammar.stringActionTable());
                System.out.println(output);
            } else {
                System.out.println("Invalid option. Use <-wat|-pat>.");
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

class FileManager {
    public static void writeFile(String filePath, StringBuilder content) throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        java.nio.file.Files.write(path, content.toString().getBytes());
        System.out.println("Action table written to " + filePath);
    }
}
