import java.io.FileReader;
import java.io.FileWriter;
import java.util.Objects;

public class Main {
    /**
     * Main
     * @param argv
     * @throws Exception
     */
    public static void main(String[] argv) throws Exception {
        if (argv.length == 1 ) { // Print the rules
            FileReader file;
            try {
                file = new FileReader(argv[0]);
            }catch (Exception e){
                throw new Exception(argv[0] + " file not found");

            }
            Parser parser = new Parser(file);
            parser.program(); // parse the program
            Integer[] rules = parser.getRulesNumberList().toArray(new Integer[0]);
            for (Integer rule : rules) {
                System.out.print(rule + " ");
            }
            System.out.println();
        }
        else if(argv.length == 3){  // Make the Parse Tree
            if (Objects.equals(argv[0], "-wt")){
                FileReader file;
                try {
                    file = new FileReader(argv[2]);
                }catch (Exception e){
                    throw new Exception(argv[2] + " file not found");

                }
                Parser parser = new Parser(file);
                ParseTree parseTree = parser.program();
                FileWriter TreeToTex=null;
                try {
                    TreeToTex = new FileWriter(argv[1]);

                }catch (Exception e){
                    throw new Exception("Cannot create the file : " + argv[1]);

                }
                TreeToTex.write(parseTree.toLaTeX());
                TreeToTex.close();
            }
            else{
                help();
            }
        }
        else{
            help();
            return;
        }
    }

    /**
     * Help function to guide the users
     */
    private static void help(){
        System.out.println("Usage : java -jar part2.jar [OPTION] [FILE]");
        System.out.println("For print the parse tree (rules), the command for running your\n" +
                "executable must be as follows:");
        System.out.println("java -jar part2.jar sourceFile.fs");
        System.out.println("For write the parse tree on tex file, the command for running your\n" +
                "executable must be as follows: ");
        System.out.println("java -jar part2.jar -wt sourceFile.tex sourceFile.fs");
    }
}