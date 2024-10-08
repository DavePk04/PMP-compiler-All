public class Token {
    private Terminal terminal;
    private NonTerminal nonTerminal;

    public Token(Terminal term) {
        terminal = term;
        nonTerminal = null;
    }

    public Token(NonTerminal nonTerm) {
        nonTerminal = nonTerm;
        terminal = null;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public NonTerminal getNonTerminal() {
        return nonTerminal;
    }

    public boolean isTerminal() {
        return (terminal != null && nonTerminal == null);
    }

    public boolean isNonTerminal() {
        return (nonTerminal != null && terminal == null);
    }

    public boolean isEpsilon() {
        return (nonTerminal == null && terminal.equals(Terminal.EPSILON));
    }

    @Override
    public String toString() {
        if (isTerminal()) {
            return terminal.toString();
        } else if (isNonTerminal()) {
            return nonTerminal.toString();
        } else {
            return "InvalidToken";
        }
    }
}
