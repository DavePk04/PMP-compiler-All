import java.util.Objects;

public class Label {

    private final Terminal terminal;
    private final NonTerminal nonTerminal;
    private String valueToOut; // Value of the symbol to put in the parseTree node.

    public Label(Terminal terminal, String valueToOut) {
        this.terminal = terminal;
        this.nonTerminal = null;
        this.valueToOut = valueToOut;
    }

    public Label(NonTerminal nonTerminal) {
        this.terminal = null;
        this.nonTerminal = nonTerminal;
        this.valueToOut = "";
    }

    /**
     * Set the value of the terminal which should be put in the node of the parseTree.
     */
    private void setValueToOut() {
        // When the token and the terminal are identical, we avoid writing both of them in the parseTree node.
        // For example: [BEGIN BEGIN], it's clearer to write only [BEGIN]
        if (terminal != null && Objects.equals(valueToOut, terminal.toString())) {
            valueToOut = "";
        }
        // Replace ">" by "$>$" to respect LaTeX syntax; otherwise, the output will be "¿"
        if (Objects.equals(valueToOut, "<")) {
            valueToOut = "$<$";
        } else if (Objects.equals(valueToOut, ">")) {
            valueToOut = "$>$";
        }
    }

    public String toTexString() {
        setValueToOut(); // Set the valueToOut of the current terminal
        if (this.terminal != null) {
            return terminal + " " + valueToOut;
        }
        if (this.nonTerminal != null) {
            return nonTerminal.toString();
        }
        return "ERROR";
    }
}
