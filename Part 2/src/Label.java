import java.util.Objects;

public class Label {

    private final Terminal terminal;
    private final NonTerminal nonTerminal;
    private String valueToOut;    // Value of symbol to put in parseTree node.

    public Label(Terminal Terminal, String valueToOut) {
        this.terminal = Terminal;
        this.nonTerminal = null;
        this.valueToOut = valueToOut;
    }

    public Label(NonTerminal NonTerminal) {
        this.terminal = null;
        this.nonTerminal = NonTerminal;
        this.valueToOut = "";
    }

    /**
     * Set the value of the terminal which should be put in the node of the parseTree
     */
    private void setValueToOut(){
        // When the token and the terminal are identical, we avoid writing in the node of the parseTree both of them.
        // For example: [BEGIN BEGIN], its more clear to write only [BEGIN]
        if (terminal != null && Objects.equals(valueToOut, terminal.toString())){
            valueToOut="";
        }
        // replace ">" by "$>$" to respect latex syntax otherwise the output will be "¿"
        if (Objects.equals(valueToOut, "<")){
            valueToOut = "$<$";
        }
        else if (Objects.equals(valueToOut, ">")){
            valueToOut = "$>$";

        }
    }

    public String toTexString() {
        setValueToOut(); // set the valueToOut of the current terminal
        if (this.terminal != null) {
            return terminal + " " +valueToOut;
        }
        if (this.nonTerminal != null) {
            return nonTerminal.toString();
        }
        return "ERROR";
    }
}