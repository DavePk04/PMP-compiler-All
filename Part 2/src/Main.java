import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Main {
    /**
     * Main method
     * @param argv Command line arguments
     */
    public static void main(String[] argv) {
        try {
            if (argv.length == 1) {
                printRules(argv);
            } else if (argv.length == 3 && Objects.equals(argv[0], "-wt")) {
                makeParseTree(argv);
            } else {
                help();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void printRules(String[] argv) throws Exception {
        try (FileReader file = new FileReader(argv[0])) {
            Parser parser = new Parser(file);
            parser.program(); // parse the program
            Integer[] rules = parser.getRulesNumberList().toArray(new Integer[0]);
            for (Integer rule : rules) {
                System.out.print(rule + " ");
            }
            System.out.println();
        } catch (IOException e) {
            throw new Exception(argv[0] + " file not found");
        }
    }

    private static void makeParseTree(String[] argv) throws Exception {
        try (FileReader file = new FileReader(argv[2]);
             FileWriter treeToTex = new FileWriter(argv[1])) {

            Parser parser = new Parser(file);
            ParseTree parseTree = parser.program();

            treeToTex.write(parseTree.toLaTeX());
            treeToTex.write(parseTree.toTikZPicture());

        } catch (IOException e) {
            throw new Exception("Cannot create the file: " + argv[1]);
        }
    }

    /**
     * Help function to guide the users
     */
    private static void help() {
        System.out.println("Usage: java -jar part2.jar [OPTION] [FILE]");
        System.out.println("For printing the parse tree (rules), the command for running your\n" +
                "executable must be as follows:");
        System.out.println("java -jar part2.jar sourceFile.pmp");
        System.out.println("For writing the parse tree to a tex file, the command for running your\n" +
                "executable must be as follows: ");
        System.out.println("java -jar part2.jar -wt sourceFile.tex sourceFile.pmp");
    }
}
