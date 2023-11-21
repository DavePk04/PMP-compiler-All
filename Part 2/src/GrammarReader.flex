import java.util.regex.PatternSyntaxException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

%%// Options of the scanner

%class GrammarReader // Name
%unicode               // Use unicode
%line                  // Use line counter (yyline variable)
%column                // Use character counter by line (yycolumn variable)
%type Grammar
%function getGrammar
%yylexthrow PatternSyntaxException

%{
    private NonTerminal currentVariable;
    private ArrayList<Token> currentRHS = new ArrayList<Token>();
    private Grammar grammar = new Grammar(NonTerminal.Program);

    public NonTerminal getNonTerminal(String nonTerm) {
        return NonTerminal.valueOf(nonTerm.substring(1, nonTerm.length() - 1));
    }

    // Define aliases for terminals
    private Terminal getTerminal(String term) throws PatternSyntaxException {
        switch (term.toLowerCase()) {
            case "begin": return Terminal.BEGIN;
            case "end": return Terminal.END;
            case "assign": return Terminal.ASSIGN;
            case "dots": return Terminal.DOTS;
            case "lparen": return Terminal.LPAREN;
            case "rparen": return Terminal.RPAREN;
            case "minus": return Terminal.MINUS;
            case "plus": return Terminal.PLUS;
            case "times": return Terminal.TIMES;
            case "divide": return Terminal.DIVIDE;
            case "if": return Terminal.IF;
            case "then": return Terminal.THEN;
            case "else": return Terminal.ELSE;
            case "and": return Terminal.AND;
            case "or": return Terminal.OR;
            case "lbrack": return Terminal.LBRACK;
            case "rbrack": return Terminal.RBRACK;
            case "equal": return Terminal.EQUAL;
            case "smaller": return Terminal.SMALLER;
            case "while": return Terminal.WHILE;
            case "do": return Terminal.DO;
            case "print": return Terminal.PRINT;
            case "read": return Terminal.READ;
            case "varname": return Terminal.VARNAME;
            case "number": return Terminal.NUMBER;
            case "eos": return Terminal.EOS;
            case "epsilon": return Terminal.EPSILON;
            default: return Terminal.valueOf(term.toUpperCase());
        }
    }
%}

%eofval{
	return grammar;
%eofval}

// Extended Regular Expressions

AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]
Digit          = [0-9]
AlphaNumeric   = {AlphaUpperCase}|{AlphaLowerCase}|{Digit}

RightArrow     = "-->"
Variable       = <{AlphaNumeric}+>
Terminal       = {AlphaLowerCase}+
Epsilon        = "EPSILON"
LineFeed       = "\n"
CarriageReturn = "\r"
EndOfLine      = ({LineFeed}{CarriageReturn}?) | ({CarriageReturn}{LineFeed}?)
Space          = (\t | \f | " ")
Spaces         = {Space}+

// Declare exclusive states
%xstate RHS

%%// Identification of tokens

<YYINITIAL> {
    {Variable}         {currentVariable = getNonTerminal(yytext());}
    {RightArrow}       {yybegin(RHS);}
    {Spaces}           {}
    [^]                {throw new PatternSyntaxException("Unmatched token, out of Tokens",yytext(),yyline);} // unmatched token gives an error
}

<RHS> {
    {Variable}         {currentRHS.add(new Token(getNonTerminal(yytext())));}
    {Terminal}         {currentRHS.add(new Token(getTerminal(yytext())));}
    {Epsilon}          {currentRHS.add(new Token(Terminal.EPSILON));}
    {Spaces}           {}
    {EndOfLine}        {grammar.addRule(currentVariable, Collections.unmodifiableList(currentRHS));
                        currentRHS = new ArrayList<Token>();
                        yybegin(YYINITIAL);}
    [^]                {throw new PatternSyntaxException("Unmatched token, out of Tokens",yytext(),yyline);}
}
