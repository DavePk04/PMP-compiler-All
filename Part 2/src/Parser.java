import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Parser{
    private LexicalAnalyzer scanner;
    private Token current;

    private final ArrayList<Integer> rulesNumberList = new ArrayList<Integer>();

    public Parser(FileReader source) throws IOException{
        this.scanner = new LexicalAnalyzer(source);
        this.current = scanner.nextToken();
    }

    private void consume() throws IOException{
        current = scanner.nextToken();
    }

    private ParseTree match(Terminal token) throws IOException, ParseException{
        if(!current.getType().equals(token)){
            // There is a parsing error
            throw new ParseException(current, Arrays.asList(token));
        }
        else {
            Token cur = current;
            consume();
            return new ParseTree(new TreeLabel(cur));
        }
    }
    public ParseTree parse() throws IOException, ParseException{
        // Program is the initial symbol of the grammar
        return program();
    }

    /**
     * Main method of parsing, we need to start parsing by this rule
     * Define the rule number(1) which correspond to Program     --> BEGIN Code END
     */
    private ParseTree program() throws IOException, ParseException{
        // Program     --> BEGIN Code END
        return new ParseTree(NonTerminal.Program, Arrays.asList(
                match(Terminal.BEGIN),
                code(),
                match(Terminal.END),
                match(Terminal.EOS)));
    }

    private ParseTree code() throws IOException, ParseException{
        switch(current.getType()) {
            // Code         --> EPSILON
            // follow(code) = {END}
            case END:
                return new ParseTree(NonTerminal.Code, Arrays.asList(new ParseTree(Terminal.EPSILON)));
            // Code  -->  InstList
            // first(code) = {BEGIN, IF, WHILE, PRINT, READ, VARNAME}
            case BEGIN:
            case IF:
            case WHILE:
            case PRINT:
            case READ:
            case VARNAME:
                return new ParseTree(NonTerminal.Code, Arrays.asList(instList()));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree instList() throws IOException, ParseException{
        // InstList    --> Instruction DotsInstList
        // first(Instlist) = BEGIN, IF, WHILE, PRINT, READ, VARNAME
        switch(current.getType()) {
            case BEGIN, IF, WHILE, PRINT, READ, VARNAME:
                return new ParseTree(NonTerminal.InstList, Arrays.asList(
                        instruction(),
                        dotsInstList()
                ));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree dotsInstList() throws IOException, ParseException{
        switch(current.getType()) {
            // DotsInstList --> DOTS InstList
            // first(DotsInstList) = DOTS
            case DOTS:
                return new ParseTree(NonTerminal.DotsInstList, Arrays.asList(
                        match(Terminal.DOTS),
                        instList()
                ));
            // DotsInstList --> EPSILON
            // follow(DotsInstList) = {END}
            case END:
                return new ParseTree(NonTerminal.DotsInstList, Arrays.asList(new ParseTree(Terminal.EPSILON)));
            default:
                throw new ParseException(current);
        }
    }


    private ParseTree instruction() throws IOException, ParseException{
        // first(instruction) = {BEGIN, IF, WHILE, PRINT, READ, VARNAME}
        switch(current.getType()) {
            // <Instruction>  --> <Assign>
            case VARNAME:
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(assign()));
            // <Instruction>  --> <If>
            case IF:
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(if_()));
            // <Instruction>  --> <While>
            case WHILE:
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(while_()));
            // <Instruction>  --> <Print>
            case PRINT:
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(print()));
            // <Instruction>  --> <Read>
            case READ:
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(read()));
            // <Instruction>  --> BEGIN <InstList> END
            case BEGIN:
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(
                        match(Terminal.BEGIN),
                        instList(),
                        match(Terminal.END)
                ));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree assign() throws IOException, ParseException{
        // Assign      --> VARNAME ASSIGN ExprArith
        // first(assign) = {VARNAME}
        return new ParseTree(NonTerminal.Assign, Arrays.asList(
                match(Terminal.VARNAME),
                match(Terminal.ASSIGN),
                exprArith()
        ));
    }

    private ParseTree exprArith() throws IOException, ParseException{
        // ExprArith   --> ExprArith2 ExprArithPrim
        // first(exprArith) = {LPAREN, MINUS, VARNAME, NUMBER}
        return new ParseTree(NonTerminal.ExprArith, Arrays.asList(
                exprArith2(),
                exprArithPrim()
        ));
    }

    private ParseTree exprArithPrim() throws IOException, ParseException{
        // first(ExprArithPrim) = {PLUS, MINUS}
        // follow(ExprArithPrim) = { END, DOTS, RPAREN, MINUS, PLUS, TIMES, DIVIDE, THEN, ELSE, AND, OR, RBRACK, EQUAL, SMALLER, DO}
        switch(current.getType()) {
            case PLUS, MINUS:         // ExprArithPrim --> AddOp ExprArith2 ExprArithPrim
                return new ParseTree(NonTerminal.ExprArithPrim, Arrays.asList(
                        addOp(),
                        exprArith2(),
                        exprArithPrim()
                ));
            case END, DOTS, RPAREN, TIMES, DIVIDE, THEN, ELSE, AND, OR, RBRACK, EQUAL, SMALLER, DO: // ExprArithPrim --> EPSILON
                return new ParseTree(NonTerminal.ExprArithPrim, Arrays.asList(new ParseTree(Terminal.EPSILON)));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree addOp() throws IOException, ParseException{
        // first(AddOp) = {PLUS, MINUS}
        switch(current.getType()) {
            case PLUS: // AddOp       --> PLUS
                return new ParseTree(NonTerminal.AddOp, Arrays.asList(match(Terminal.PLUS)));
            case MINUS: // AddOp       --> MINUS
                return new ParseTree(NonTerminal.AddOp, Arrays.asList(match(Terminal.MINUS)));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree exprArith2() throws IOException, ParseException{
        // ExprArith2  --> ExprArith3 ExprArith2Prim
        // first(ExprArith2) = {LPAREN, MINUS, VARNAME, NUMBER}
        return new ParseTree(NonTerminal.ExprArith2, Arrays.asList(
                exprArith3(),
                exprArith2Prim()
        ));
    }

    private ParseTree exprArith3() throws IOException, ParseException{
        // ExprArith3  --> NUMBER
        //             --> MINUS ExprArith
        //             --> LPAREN ExprArith RPAREN
        //             --> VARNAME
        // first(ExprArith3) = {LPAREN, MINUS, VARNAME, NUMBER}
        switch(current.getType()) {
            case NUMBER: // ExprArith3  --> NUMBER
                return new ParseTree(NonTerminal.ExprArith3, Arrays.asList(match(Terminal.NUMBER)));
            case MINUS: // ExprArith3  --> MINUS ExprArith
                return new ParseTree(NonTerminal.ExprArith3, Arrays.asList(
                        match(Terminal.MINUS),
                        exprArith()
                ));
            case LPAREN: // ExprArith3  --> LPAREN ExprArith RPAREN
                return new ParseTree(NonTerminal.ExprArith3, Arrays.asList(
                        match(Terminal.LPAREN),
                        exprArith(),
                        match(Terminal.RPAREN)
                ));
            case VARNAME: // ExprArith3  --> VARNAME
                return new ParseTree(NonTerminal.ExprArith3, Arrays.asList(match(Terminal.VARNAME)));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree exprArith2Prim() throws IOException, ParseException{
        // ExprArith2Prim --> MultOp ExprArith3 ExprArith2Prim
        // first(ExprArith2Prim) = {TIMES, DIVIDE}
        // follow(ExprArith2Prim) = {END, DOTS, RPAREN, MINUS, PLUS, TIMES, DIVIDE, THEN, ELSE, AND, OR, RBRACK, EQUAL, SMALLER, DO, EPSILON}
        switch(current.getType()) {
            case TIMES, DIVIDE: // ExprArith2Prim --> MultOp ExprArith3 ExprArith2Prim
                return new ParseTree(NonTerminal.ExprArith2Prim, Arrays.asList(
                        multOp(),
                        exprArith3(),
                        exprArith2Prim()
                ));
            case END, DOTS, RPAREN, MINUS, PLUS, THEN, ELSE, AND, OR, RBRACK, EQUAL, SMALLER, DO, EPSILON: // ExprArith2Prim --> EPSILON
                return new ParseTree(NonTerminal.ExprArith2Prim, Arrays.asList(new ParseTree(Terminal.EPSILON)));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree multOp() throws IOException, ParseException{
        // first(MultOp) = {TIMES, DIVIDE}
        switch(current.getType()) {
            case TIMES: // MultOp      --> TIMES
                return new ParseTree(NonTerminal.MultOp, Arrays.asList(match(Terminal.TIMES)));
            case DIVIDE: // MultOp      --> DIVIDE
                return new ParseTree(NonTerminal.MultOp, Arrays.asList(match(Terminal.DIVIDE)));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree if_() throws IOException, ParseException{
        // If           --> IF Cond THEN Instruction ELSE Statement
        // first(If) = {IF}
        return new ParseTree(NonTerminal.If, Arrays.asList(
                match(Terminal.IF),
                cond(),
                match(Terminal.THEN),
                instruction(),
                match(Terminal.ELSE),
                statement()
        ));
    }

    private ParseTree statement() throws IOException, ParseException{
        // Statement    --> Instruction
        //              --> EPSILON
        // first(Statement) = {BEGIN, IF, WHILE, PRINT, READ, VARNAME}
        // follow(Statement) = {END, DOTS, ELSE, EPSILON}

        switch(current.getType()) {
            case BEGIN, IF, WHILE, PRINT, READ, VARNAME: // Statement    --> Instruction
                return new ParseTree(NonTerminal.Statement, Arrays.asList(instruction()));
            case END, DOTS, ELSE, EPSILON: // Statement    --> EPSILON
                return new ParseTree(NonTerminal.Statement, Arrays.asList(new ParseTree(Terminal.EPSILON)));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree cond() throws IOException, ParseException{
        // Cond         -->  Cond2 CondPrim
        // first(Cond) = {LPAREN, MINUS, LBRACK, VARNAME, NUMBER}
        return new ParseTree(NonTerminal.Cond, Arrays.asList(
                cond2(),
                condPrim()
        ));
    }

    private ParseTree condPrim() throws IOException, ParseException{
        // CondPrim     --> OR Cond2 CondPrim
        //              --> EPSILON
        // first(CondPrim) = {OR, EPSILON}
        // follow(CondPrim) = {THEN, RBRACK, DO}
        switch(current.getType()) {
            case OR: // CondPrim     --> OR Cond2 CondPrim
                return new ParseTree(NonTerminal.CondPrim, Arrays.asList(
                        match(Terminal.OR),
                        cond2(),
                        condPrim()
                ));
            case THEN, RBRACK, DO: // CondPrim     --> EPSILON
                return new ParseTree(NonTerminal.CondPrim, Arrays.asList(new ParseTree(Terminal.EPSILON)));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree cond2() throws IOException, ParseException{
        // Cond2        --> Cond3 Cond2Prim
        // first(Cond2) = {LPAREN, MINUS, LBRACK, VARNAME, NUMBER}
        return new ParseTree(NonTerminal.Cond2, Arrays.asList(
                cond3(),
                cond2Prim()
        ));
    }

    private ParseTree cond2Prim() throws IOException, ParseException{
        // Cond2Prim    --> AND Cond3 Cond2Prim
        //              --> EPSILON
        // first(Cond2Prim) = {AND, EPSILON}
        // follow(Cond2Prim) = {THEN, OR, RBRACK, DO}
        switch(current.getType()) {
            case AND: // Cond2Prim    --> AND Cond3 Cond2Prim
                return new ParseTree(NonTerminal.Cond2Prim, Arrays.asList(
                        match(Terminal.AND),
                        cond3(),
                        cond2Prim()
                ));
            case THEN, OR, RBRACK, DO: // Cond2Prim    --> EPSILON
                return new ParseTree(NonTerminal.Cond2Prim, Arrays.asList(new ParseTree(Terminal.EPSILON)));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree cond3() throws IOException, ParseException{
        // Cond3        --> SimpleCond
        //              --> LBRACK Cond RBRACK
        // first(Cond3) = {LPAREN, MINUS, LBRACK, VARNAME, NUMBER}
        switch(current.getType()) {
            case LPAREN, MINUS, VARNAME, NUMBER: // Cond3        --> SimpleCond
                return new ParseTree(NonTerminal.Cond3, Arrays.asList(simpleCond()));
            case LBRACK: // Cond3        --> LBRACK Cond RBRACK
                return new ParseTree(NonTerminal.Cond3, Arrays.asList(
                        match(Terminal.LBRACK),
                        cond(),
                        match(Terminal.RBRACK)
                ));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree simpleCond() throws IOException, ParseException{
        // SimpleCond   --> ExprArith Comp ExprArith
        // first(SimpleCond) = {LPAREN, MINUS, VARNAME, NUMBER}
        return new ParseTree(NonTerminal.SimpleCond, Arrays.asList(
                exprArith(),
                comp(),
                exprArith()
        ));
    }

    private ParseTree comp() throws IOException, ParseException{
        // Comp         --> EQUAL
        //              --> SMALLER
        // first(Comp) = {EQUAL, SMALLER}
        switch(current.getType()) {
            case EQUAL: // Comp         --> EQUAL
                return new ParseTree(NonTerminal.Comp, Arrays.asList(match(Terminal.EQUAL)));
            case SMALLER: // Comp         --> SMALLER
                return new ParseTree(NonTerminal.Comp, Arrays.asList(match(Terminal.SMALLER)));
            default:
                throw new ParseException(current);
        }
    }

    private ParseTree while_() throws IOException, ParseException{
        // While        --> WHILE Cond DO Instruction
        // first(While) = {WHILE}
        return new ParseTree(NonTerminal.While, Arrays.asList(
                match(Terminal.WHILE),
                cond(),
                match(Terminal.DO),
                instruction()
        ));
    }

    private ParseTree read() throws IOException, ParseException{
        // Read         --> READ LPAREN VARNAME RPAREN
        // first(Read) = {READ}
        return new ParseTree(NonTerminal.Read, Arrays.asList(
                match(Terminal.READ),
                match(Terminal.LPAREN),
                match(Terminal.VARNAME),
                match(Terminal.RPAREN)
        ));
    }

    private ParseTree print() throws IOException, ParseException{
        // Print        --> PRINT LPAREN VARNAME RPAREN
        // first(Print) = {PRINT}
        return new ParseTree(NonTerminal.Print, Arrays.asList(
                match(Terminal.PRINT),
                match(Terminal.LPAREN),
                match(Terminal.VARNAME),
                match(Terminal.RPAREN)
        ));
    }
}