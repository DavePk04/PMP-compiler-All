public enum Terminal {
    BEGIN,
    END,
    ASSIGN,
    DOTS,
    LPAREN,
    RPAREN,
    MINUS,
    PLUS,
    TIMES,
    DIVIDE,
    IF,
    THEN,
    ELSE,
    AND,
    OR,
    LBRACK,
    RBRACK,
    EQUAL,
    SMALLER,
    WHILE,
    DO,
    PRINT,
    READ,
    VARNAME,
    NUMBER,
    EOS,
    EPSILON;

    public Object getValue() {
        switch (this) {
            case BEGIN:
                return "begin";
            case END:
                return "end";
            case ASSIGN:
                return ":=";
            case DOTS:
                return "..";
            case LPAREN:
                return "(";
            case RPAREN:
                return ")";
            case MINUS:
                return "-";
            case PLUS:
                return "+";
            case TIMES:
                return "*";
            case DIVIDE:
                return "/";
            case IF:
                return "if";
            case THEN:
                return "then";
            case ELSE:
                return "else";
            case AND:
                return "and";
            case OR:
                return "or";
            case LBRACK:
                return "[";
            case RBRACK:
                return "]";
            case EQUAL:
                return "=";
            case SMALLER:
                return "<";
            case WHILE:
                return "while";
            case DO:
                return "do";
            case PRINT:
                return "print";
            case READ:
                return "read";
            case VARNAME:
                return "varname";
            case NUMBER:
                return "number";
            case EOS:
                return "$";
            case EPSILON:
                return "epsilon";
            default:
                return null;
        }
    }
}
