public enum NonTerminal {
    Program,
    Code,
    InstList,
    Instruction,
    DotsInstList,
    Assign,
    ExprArith,
    ExprArith2,
    ExprArith3,
    ExprArithPrim,
    ExprArith2Prim,
    If,
    Cond,
    Cond2,
    Cond3,
    CondPrim,
    Cond2Prim,
    AddOp,
    MultOp,
    SimpleCond,
    Comp,
    While,
    Print,
    Statement,
    Read;

    public Object getValue() {

        switch (this) {
            case Program:
                return "Program";
            case Code:
                return "Code";
            case InstList:
                return "InstList";
            case Instruction:
                return "Instruction";
            case Assign:
                return "Assign";
            case ExprArith:
                return "ExprArith";
            case If:
                return "If";
            case Cond:
                return "Cond";
            case SimpleCond:
                return "SimpleCond";
            case Comp:
                return "Comp";
            case While:
                return "While";
            case Print:
                return "Print";
            case Read:
                return "Read";
            case ExprArith2:
                return "ExprArith2";
            case ExprArith3:
                return "ExprArith3";
            case ExprArithPrim:
                return "ExprArithPrim";
            case ExprArith2Prim:
                return "ExprArith2Prim";
            case Cond2:
                return "Cond2";
            case Cond3:
                return "Cond3";
            case CondPrim:
                return "CondPrim";
            case Cond2Prim:
                return "Cond2Prim";
            case AddOp:
                return "AddOp";
            case MultOp:
                return "MultOp";
            case Statement:
                return "Statement";
            case DotsInstList:
                return "DotsInstList";
            default:
                return null;
        }
    }
}