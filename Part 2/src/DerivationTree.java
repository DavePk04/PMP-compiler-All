/* TODO:
 * build a derivation tree which from startSymbol
 * inputs: terminal enum, nonTerminal enum, startSymbol, derivationRules
 * output: derivation tree
 * this is a kind of derivationRules: {Program=[[BEGIN, Code, END]], Code=[[InstList], [EPSILON]], InstList=[[Instruction], [Instruction, DOTS, InstList]], Instruction=[[If], [Assign], [While], [For], [Print], [Read], [BEGIN, InstList, END]], Assign=[[VARNAME, ASSIGN, ExprArith]], ExprArith=[[UMINUS, ExprArith], [VARNAME], [NUMBER], [LPAREN, ExprArith, RPAREN], [ExprArith, Op, ExprArith]], Op=[[PLUS], [TIMES], [BMINUS], [DIVIDE]], If=[[IF, Cond, THEN, Instruction, ELSE, Instruction], [IF, Cond, THEN, Instruction]], Cond=[[SimpleCond], [Cond, AND, Cond], [LBRACK, Cond, RBRACK], [Cond, OR, Cond]], SimpleCond=[[ExprArith, Comp, ExprArith]], Comp=[[EQUAL], [SMALLER]], While=[[WHILE, Cond, DO, Instruction]], Print=[[PRINT, LPAREN, VARNAME, RPAREN]], Read=[[READ, LPAREN, VARNAME, RPAREN]]}
 * */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DerivationTree {
    private Symbol startSymbol;
    private TreeNode rootNode;

    public DerivationTree(Symbol startSymbol, Map<NonTerminal, Set<List<Symbol>>> derivationRules) {
        this.startSymbol = startSymbol;
        this.rootNode = buildDerivationTree(startSymbol, derivationRules);
    }

//    private TreeNode buildDerivationTree(Symbol symbol, Map<NonTerminal, Set<List<Symbol>>> derivationRules) {
//        System.out.println("symbol: " + symbol);
//        TreeNode node = new TreeNode(symbol);
//
//        if (symbol.isTerminal()) {
//            node.addChild(new TreeNode(symbol));
//        } else{
//            for (List<Symbol> rule : derivationRules.get(symbol.getNonTerminal())) {
//                for (Symbol s : rule) {
//                    node.addChild(buildDerivationTree(s, derivationRules));
//                }
//            }
//        }
//
//        return node;
//    }

    private TreeNode buildDerivationTree(Symbol symbol, Map<NonTerminal, Set<List<Symbol>>> derivationRules) {
        TreeNode node = new TreeNode(symbol);

        if (!symbol.isTerminal()) {
            if (derivationRules.containsKey(symbol.getNonTerminal())) {
                for (List<Symbol> rule : derivationRules.get(symbol.getNonTerminal())) {
                    TreeNode ruleNode = new TreeNode(null); // Placeholder for non-terminal rule
                    for (Symbol s : rule) {
                        TreeNode childNode = buildDerivationTree(s, derivationRules);
                        ruleNode.addChild(childNode);
                    }
                    node.addChild(ruleNode);
                }
            }
        }

        return node;
    }

    public String toString() {
        return rootNode.toString();
    }

    // Additional methods for tree traversal, printing, etc.
}

class TreeNode {
    private Symbol symbol;
    private List<TreeNode> children;

    public TreeNode(Symbol symbol) {
        this.symbol = symbol;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        this.children.add(child);
    }

    // Additional methods as needed

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(symbol);
        if (children.size() > 0) {
            string.append("(");
            for (TreeNode child : children) {
                string.append(child.toString());
            }
            string.append(")");
        }
        return string.toString();
    }
}

