import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.util.Arrays;

public class Parser {
    private Analyzer analyzer;
    private Symbol currentSymbol;
    private final ArrayList<Integer> rulesNumberList = new ArrayList<Integer>();

    public Parser(FileReader file) throws Exception {
        analyzer = new Analyzer(file);
        this.currentSymbol = analyzer.getAnalyzer().nextSymbol();
    }

    /**
     * @return rulesNumberList
     */
    public ArrayList<Integer> getRulesNumberList() {
        return this.rulesNumberList;
    }

    /**
     * Match the terminal by create its node on parseTree or throw an exception.
     */
    private ParseTree match(Terminal terminal) throws Exception {
        if (currentSymbol.getTerminal() == terminal) {
            String valueOfSymbol = "";
            Terminal prevTerminal = currentSymbol.getTerminal();
            if (terminal != Terminal.EOS) valueOfSymbol = currentSymbol.getValue().toString();
            currentSymbol = analyzer.getAnalyzer().nextSymbol();
            return new ParseTree(new Label(prevTerminal, valueOfSymbol));
        } else {
            throw new Exception(getExceptionString());
        }
    }

    /**
     * @return the string of Exception to display with the current token information which caused the exception.
     */
    private String getExceptionString() {
        return "An error occurred while parsing the token : " + currentSymbol.getValue() + " at line " +
                currentSymbol.getLine() + " and at column " + currentSymbol.getColumn();
    }


//    public ParseTree parse() throws Exception{
//        // Program is the initial symbol of the grammar
//        return program();
//    }

    /**
     * Main method of parsing, we need to start parsing by this rule
     * Define the rule number(1) which correspond to Program     --> BEGIN Code END
     */
    public ParseTree program() throws Exception {
        this.rulesNumberList.add(1); // Program     --> BEGIN Code END
        return new ParseTree(new Label(NonTerminal.Program), Arrays.asList(
                match(Terminal.BEGIN),
                code(),
                match(Terminal.END),
                match(Terminal.EOS)
        ));
    }

    private ParseTree code() throws Exception {
        switch (currentSymbol.getTerminal()) {
            // Code         --> EPSILON
            // follow(code) = {END}
            case END:
                this.rulesNumberList.add(2); // Code         --> EPSILON
                return new ParseTree(new Label(NonTerminal.Code),
                        Arrays.asList(new ParseTree(new Label(Terminal.EPSILON, ""))));
            // Code  -->  InstList
            // first(code) = {BEGIN, IF, WHILE, PRINT, READ, VARNAME, EPSILON}
            case BEGIN:
            case IF:
            case WHILE:
            case PRINT:
            case READ:
            case VARNAME:
                this.rulesNumberList.add(3); // Code  -->  InstList
                return new ParseTree(new Label(NonTerminal.Code)
                        , Arrays.asList(instList()));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree instList() throws Exception {
        // InstList    --> Instruction DotsInstList
        // first(Instlist) = BEGIN, IF, WHILE, PRINT, READ, VARNAME
        switch (currentSymbol.getTerminal()) {
            case BEGIN:
            case IF:
            case WHILE:
            case PRINT:
            case READ:
            case VARNAME:
                this.rulesNumberList.add(4); // InstList    --> Instruction DotsInstList
                return new ParseTree(new Label(NonTerminal.InstList),
                        Arrays.asList(
                                instruction(),
                                dotsInstList()
                        ));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree dotsInstList() throws Exception {
        switch (currentSymbol.getTerminal()) {
            // DotsInstList --> DOTS InstList
            // first(DotsInstList) = {DOTS, EPSILON}
            case DOTS:
                this.rulesNumberList.add(6); // DotsInstList --> DOTS InstList
                return new ParseTree(new Label(NonTerminal.DotsInstList),
                        Arrays.asList(
                                match(Terminal.DOTS),
                                instList()
                        ));
            // DotsInstList --> EPSILON
            // follow(DotsInstList) = {END}
            case END:
                this.rulesNumberList.add(5); // DotsInstList --> EPSILON
                return new ParseTree(new Label(NonTerminal.DotsInstList),
                        Arrays.asList(new ParseTree(new Label(Terminal.EPSILON, ""))));
            default:
                throw new Exception(getExceptionString());
        }
    }


    private ParseTree instruction() throws Exception {
        // first(instruction) = {BEGIN, IF, WHILE, PRINT, READ, VARNAME}
        switch (currentSymbol.getTerminal()) {
            // <Instruction>  --> <Assign>
            case VARNAME:
                this.rulesNumberList.add(7); // <Instruction>  --> <Assign>
                return new ParseTree(new Label(NonTerminal.Instruction), Arrays.asList(assign()));
            // <Instruction>  --> <If>
            case IF:
                this.rulesNumberList.add(8); // <Instruction>  --> <If>
                return new ParseTree(new Label(NonTerminal.Instruction), Arrays.asList(if_()));
            // <Instruction>  --> <While>
            case WHILE:
                this.rulesNumberList.add(9); // <Instruction>  --> <While>
                return new ParseTree(new Label(NonTerminal.Instruction), Arrays.asList(while_()));
            // <Instruction>  --> <Print>
            case PRINT:
                this.rulesNumberList.add(10); // <Instruction>  --> <Print>
                return new ParseTree(new Label(NonTerminal.Instruction), Arrays.asList(print()));
            // <Instruction>  --> <Read>
            case READ:
                this.rulesNumberList.add(11); // <Instruction>  --> <Read>
                return new ParseTree(new Label(NonTerminal.Instruction), Arrays.asList(read()));
            // <Instruction>  --> BEGIN <InstList> END
            case BEGIN:
                this.rulesNumberList.add(12); // <Instruction>  --> BEGIN <InstList> END
                return new ParseTree(new Label(NonTerminal.Instruction), Arrays.asList(
                        match(Terminal.BEGIN),
                        instList(),
                        match(Terminal.END)
                ));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree assign() throws Exception {
        // Assign      --> VARNAME ASSIGN ExprArith
        // first(assign) = {VARNAME}
        this.rulesNumberList.add(13); // Assign      --> VARNAME ASSIGN ExprArith
        return new ParseTree(new Label(NonTerminal.Assign), Arrays.asList(
                match(Terminal.VARNAME),
                match(Terminal.ASSIGN),
                exprArith()
        ));
    }

    private ParseTree exprArith() throws Exception {
        // ExprArith   --> ExprArith2 ExprArithPrim
        // first(exprArith) = {LPAREN, MINUS, VARNAME, NUMBER}
        this.rulesNumberList.add(14); // ExprArith   --> ExprArith2 ExprArithPrim
        return new ParseTree(new Label(NonTerminal.ExprArith), Arrays.asList(
                exprArith2(),
                exprArithPrim()
        ));
    }

    private ParseTree exprArithPrim() throws Exception {
        // first(ExprArithPrim) = {PLUS, MINUS, EPSILON}
        // follow(ExprArithPrim) = { END, DOTS, RPAREN, THEN, ELSE, AND, OR, RBRACK, EQUAL, SMALLER, DO}
        switch (currentSymbol.getTerminal()) {
            case PLUS, MINUS: // ExprArithPrim --> AddOp ExprArith2 ExprArithPrim
                this.rulesNumberList.add(15); // ExprArithPrim --> AddOp ExprArith2 ExprArithPrim
                return new ParseTree(new Label(NonTerminal.ExprArithPrim), Arrays.asList(
                        addOp(),
                        exprArith2(),
                        exprArithPrim()
                ));
            case END, DOTS, RPAREN, THEN, ELSE, AND, OR, RBRACK, EQUAL, SMALLER, DO: // ExprArithPrim --> EPSILON
                this.rulesNumberList.add(16); // ExprArithPrim --> EPSILON
                return new ParseTree(new Label(NonTerminal.ExprArithPrim),
                        Arrays.asList(new ParseTree(new Label(Terminal.EPSILON, ""))));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree addOp() throws Exception {
        // first(AddOp) = {PLUS, MINUS}
        switch (currentSymbol.getTerminal()) {
            case PLUS: // AddOp       --> PLUS
                this.rulesNumberList.add(17); // AddOp       --> PLUS
                return new ParseTree(new Label(NonTerminal.AddOp), Arrays.asList(match(Terminal.PLUS)));
            case MINUS: // AddOp       --> MINUS
                this.rulesNumberList.add(18); // AddOp       --> MINUS
                return new ParseTree(new Label(NonTerminal.AddOp), Arrays.asList(match(Terminal.MINUS)));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree exprArith2() throws Exception {
        // ExprArith2  --> ExprArith3 ExprArith2Prim
        // first(ExprArith2) = {LPAREN, MINUS, VARNAME, NUMBER}
        this.rulesNumberList.add(19); // ExprArith2  --> ExprArith3 ExprArith2Prim
        return new ParseTree(new Label(NonTerminal.ExprArith2), Arrays.asList(
                exprArith3(),
                exprArith2Prim()
        ));
    }

    private ParseTree exprArith3() throws Exception {
        // ExprArith3  --> NUMBER
        //             --> MINUS ExprArith3
        //             --> LPAREN ExprArith RPAREN
        //             --> VARNAME
        // first(ExprArith3) = {LPAREN, MINUS, VARNAME, NUMBER}
        switch (currentSymbol.getTerminal()) {
            case NUMBER: // ExprArith3  --> NUMBER
                this.rulesNumberList.add(27); // ExprArith3  --> NUMBER
                return new ParseTree(new Label(NonTerminal.ExprArith3), Arrays.asList(match(Terminal.NUMBER)));
            case MINUS: // ExprArith3  --> MINUS ExprArith
                this.rulesNumberList.add(25); // ExprArith3  --> MINUS ExprArith3
                return new ParseTree(new Label(NonTerminal.ExprArith3), Arrays.asList(
                        match(Terminal.MINUS),
                        exprArith3()
                ));
            case LPAREN: // ExprArith3  --> LPAREN ExprArith RPAREN
                this.rulesNumberList.add(24); // ExprArith3  --> LPAREN ExprArith RPAREN
                return new ParseTree(new Label(NonTerminal.ExprArith3), Arrays.asList(
                        match(Terminal.LPAREN),
                        exprArith(),
                        match(Terminal.RPAREN)
                ));
            case VARNAME: // ExprArith3  --> VARNAME
                this.rulesNumberList.add(26); // ExprArith3  --> VARNAME
                return new ParseTree(new Label(NonTerminal.ExprArith3), Arrays.asList(match(Terminal.VARNAME)));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree exprArith2Prim() throws Exception {
        // ExprArith2Prim --> MultOp ExprArith3 ExprArith2Prim
        // first(ExprArith2Prim) = {TIMES, DIVIDE, EPSILON}
        // follow(ExprArith2Prim) = {END, DOTS, RPAREN, MINUS, PLUS, THEN, ELSE, AND, OR, RBRACK, EQUAL, SMALLER, DO}
        switch (currentSymbol.getTerminal()) {
            case TIMES, DIVIDE: // ExprArith2Prim --> MultOp ExprArith3 ExprArith2Prim
                this.rulesNumberList.add(20); // ExprArith2Prim --> MultOp ExprArith3 ExprArith2Prim
                return new ParseTree(new Label(NonTerminal.ExprArith2Prim), Arrays.asList(
                        multOp(),
                        exprArith3(),
                        exprArith2Prim()
                ));
            case END, DOTS, RPAREN, MINUS, PLUS, THEN, ELSE, AND, OR, RBRACK, EQUAL, SMALLER, DO: // ExprArith2Prim --> EPSILON
                this.rulesNumberList.add(21); // ExprArith2Prim --> EPSILON
                return new ParseTree(new Label(NonTerminal.ExprArith2Prim),
                        Arrays.asList(new ParseTree(new Label(Terminal.EPSILON, ""))));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree multOp() throws Exception {
        // first(MultOp) = {TIMES, DIVIDE}
        switch (currentSymbol.getTerminal()) {
            case TIMES: // MultOp      --> TIMES
                this.rulesNumberList.add(22); // MultOp      --> TIMES
                return new ParseTree(new Label(NonTerminal.MultOp), Arrays.asList(match(Terminal.TIMES)));
            case DIVIDE: // MultOp      --> DIVIDE
                this.rulesNumberList.add(23); // MultOp      --> DIVIDE
                return new ParseTree(new Label(NonTerminal.MultOp), Arrays.asList(match(Terminal.DIVIDE)));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree if_() throws Exception {
        // If           --> IF Cond THEN Instruction ELSE Statement
        // first(If) = {IF}
        this.rulesNumberList.add(28); // If           --> IF Cond THEN Instruction ELSE Statement
        return new ParseTree(new Label(NonTerminal.If), Arrays.asList(
                match(Terminal.IF),
                cond(),
                match(Terminal.THEN),
                instruction(),
                match(Terminal.ELSE),
                statement()
        ));
    }

    private ParseTree statement() throws Exception {
        // Statement    --> Instruction
        //              --> EPSILON
        // first(Statement) = {BEGIN, IF, WHILE, PRINT, READ, VARNAME, EPSILON}
        // follow(Statement) = {END, DOTS, ELSE}

        switch (currentSymbol.getTerminal()) {
            case BEGIN, IF, WHILE, PRINT, READ, VARNAME: // Statement    --> Instruction
                this.rulesNumberList.add(29); // Statement    --> Instruction
                return new ParseTree(new Label(NonTerminal.Statement), Arrays.asList(instruction()));
            case END, DOTS, ELSE, EPSILON: // Statement    --> EPSILON
                this.rulesNumberList.add(30); // Statement    --> EPSILON
                return new ParseTree(new Label(NonTerminal.Statement),
                        Arrays.asList(new ParseTree(new Label(Terminal.EPSILON, ""))));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree cond() throws Exception {
        // Cond         -->  Cond2 CondPrim
        // first(Cond) = {LPAREN, MINUS, LBRACK, VARNAME, NUMBER}
        this.rulesNumberList.add(31); // Cond         -->  Cond2 CondPrim
        return new ParseTree(new Label(NonTerminal.Cond), Arrays.asList(
                cond2(),
                condPrim()
        ));
    }

    private ParseTree condPrim() throws Exception {
        // CondPrim     --> OR Cond2 CondPrim
        //              --> EPSILON
        // first(CondPrim) = {OR, EPSILON}
        // follow(CondPrim) = {THEN, RBRACK, DO}
        switch (currentSymbol.getTerminal()) {
            case OR: // CondPrim     --> OR Cond2 CondPrim
                this.rulesNumberList.add(32); // CondPrim     --> OR Cond2 CondPrim
                return new ParseTree(new Label(NonTerminal.CondPrim), Arrays.asList(
                        match(Terminal.OR),
                        cond2(),
                        condPrim()
                ));
            case THEN, RBRACK, DO: // CondPrim     --> EPSILON
                this.rulesNumberList.add(33); // CondPrim     --> EPSILON
                return new ParseTree(new Label(NonTerminal.CondPrim),
                        Arrays.asList(new ParseTree(new Label(Terminal.EPSILON, ""))));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree cond2() throws Exception {
        // Cond2        --> Cond3 Cond2Prim
        // first(Cond2) = {LPAREN, MINUS, LBRACK, VARNAME, NUMBER}
        this.rulesNumberList.add(34); // Cond2        --> Cond3 Cond2Prim
        return new ParseTree(new Label(NonTerminal.Cond2), Arrays.asList(
                cond3(),
                cond2Prim()
        ));
    }

    private ParseTree cond2Prim() throws Exception {
        // Cond2Prim    --> AND Cond3 Cond2Prim
        //              --> EPSILON
        // first(Cond2Prim) = {AND, EPSILON}
        // follow(Cond2Prim) = {THEN, OR, RBRACK, DO}
        switch (currentSymbol.getTerminal()) {
            case AND: // Cond2Prim    --> AND Cond3 Cond2Prim
                this.rulesNumberList.add(35); // Cond2Prim    --> AND Cond3 Cond2Prim
                return new ParseTree(new Label(NonTerminal.Cond2Prim), Arrays.asList(
                        match(Terminal.AND),
                        cond3(),
                        cond2Prim()
                ));
            case THEN, OR, RBRACK, DO: // Cond2Prim    --> EPSILON
                this.rulesNumberList.add(36); // Cond2Prim    --> EPSILON
                return new ParseTree(new Label(NonTerminal.Cond2Prim),
                        Arrays.asList(new ParseTree(new Label(Terminal.EPSILON, ""))));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree cond3() throws Exception {
        // Cond3        --> SimpleCond
        //              --> LBRACK Cond RBRACK
        // first(Cond3) = {LPAREN, MINUS, LBRACK, VARNAME, NUMBER}
        switch (currentSymbol.getTerminal()) {
            case LPAREN, MINUS, VARNAME, NUMBER: // Cond3        --> SimpleCond
                this.rulesNumberList.add(38); // Cond3        --> SimpleCond
                return new ParseTree(new Label(NonTerminal.Cond3), Arrays.asList(simpleCond()));
            case LBRACK: // Cond3        --> LBRACK Cond RBRACK
                this.rulesNumberList.add(37); // Cond3        --> LBRACK Cond RBRACK
                return new ParseTree(new Label(NonTerminal.Cond3), Arrays.asList(
                        match(Terminal.LBRACK),
                        cond(),
                        match(Terminal.RBRACK)
                ));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree simpleCond() throws Exception {
        // SimpleCond   --> ExprArith Comp ExprArith
        // first(SimpleCond) = {LPAREN, MINUS, VARNAME, NUMBER}
        this.rulesNumberList.add(39); // SimpleCond   --> ExprArith Comp ExprArith
        return new ParseTree(new Label(NonTerminal.SimpleCond), Arrays.asList(
                exprArith(),
                comp(),
                exprArith()
        ));
    }

    private ParseTree comp() throws Exception {
        // Comp         --> EQUAL
        //              --> SMALLER
        // first(Comp) = {EQUAL, SMALLER}
        switch (currentSymbol.getTerminal()) {
            case EQUAL: // Comp         --> EQUAL
                this.rulesNumberList.add(40); // Comp         --> EQUAL
                return new ParseTree(new Label(NonTerminal.Comp), Arrays.asList(match(Terminal.EQUAL)));
            case SMALLER: // Comp         --> SMALLER
                this.rulesNumberList.add(41); // Comp         --> SMALLER
                return new ParseTree(new Label(NonTerminal.Comp), Arrays.asList(match(Terminal.SMALLER)));
            default:
                throw new Exception(getExceptionString());
        }
    }

    private ParseTree while_() throws Exception {
        // While        --> WHILE Cond DO Instruction
        // first(While) = {WHILE}
        this.rulesNumberList.add(42); // While        --> WHILE Cond DO Instruction
        return new ParseTree(new Label(NonTerminal.While), Arrays.asList(
                match(Terminal.WHILE),
                cond(),
                match(Terminal.DO),
                instruction()
        ));
    }

    private ParseTree read() throws Exception {
        // Read         --> READ LPAREN VARNAME RPAREN
        // first(Read) = {READ}
        this.rulesNumberList.add(43); // Read         --> READ LPAREN VARNAME RPAREN
        return new ParseTree(new Label(NonTerminal.Read), Arrays.asList(
                match(Terminal.READ),
                match(Terminal.LPAREN),
                match(Terminal.VARNAME),
                match(Terminal.RPAREN)
        ));
    }

    private ParseTree print() throws Exception {
        // Print        --> PRINT LPAREN VARNAME RPAREN
        // first(Print) = {PRINT}
        this.rulesNumberList.add(44); // Print        --> PRINT LPAREN VARNAME RPAREN
        return new ParseTree(new Label(NonTerminal.Print), Arrays.asList(
                match(Terminal.PRINT),
                match(Terminal.LPAREN),
                match(Terminal.VARNAME),
                match(Terminal.RPAREN)
        ));
    }
}