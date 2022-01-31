import java.util.HashMap;
import java.util.Map;

public class Expression {
    static Tree globalTree;
    static Tree currentTreePointer;
    static Map<String, Boolean> Values = new HashMap<>();
    static int trueOutcomes = 0;
    static int falseOutcomes = 0;

    public static void handleConstant(Tree tree) {
        if (!Values.containsKey(tree.getConstant())) {
            Values.put(tree.getConstant(), false);
        }
        if (currentTreePointer == null) {
            globalTree = tree;
            currentTreePointer = globalTree;
        } else {
            switch (currentTreePointer.getOperation()) {
                case OR:
                case AND:
                    rebuild(true, tree);
                    break;
                case IMPL:
                    rebuild(false, tree);
                    break;
                case BRACKET:
                    currentTreePointer.setLeft(tree);
                    tree.setParent(currentTreePointer);
                    currentTreePointer = currentTreePointer.getLeft();
                    break;
                default:
                    System.exit(1);
            }
        }
    }

    static void rebuild(boolean leftPriority, Tree treeToSet) {// 1 for left, 0 for right
        if (leftPriority) {
            if (currentTreePointer.getLeft() == null) {
                currentTreePointer.setLeft(treeToSet);
                currentTreePointer.getLeft().setParent(currentTreePointer);
                currentTreePointer = currentTreePointer.getLeft();
            } else if (currentTreePointer.getRight() == null) {
                currentTreePointer.setRight(treeToSet);
                currentTreePointer.getRight().setParent(currentTreePointer);
                currentTreePointer = currentTreePointer.getRight();
            }
        } else {
            if (currentTreePointer.getRight() == null) {
                currentTreePointer.setRight(treeToSet);
                currentTreePointer.getRight().setParent(currentTreePointer);
                currentTreePointer = currentTreePointer.getRight();
            } else if (currentTreePointer.getLeft() == null) {
                currentTreePointer.setLeft(treeToSet);
                currentTreePointer.getLeft().setParent(currentTreePointer);
                currentTreePointer = currentTreePointer.getLeft();
            }
        }

    }

    static void searchOperation(Operations operation) {
        while (currentTreePointer.getParent() != null && currentTreePointer.getParent().getOperation() == operation) {
            currentTreePointer = currentTreePointer.getParent();
        }
    }

    static boolean searchRootCase(Tree tree) {
        if (currentTreePointer.getParent() == null) {
            currentTreePointer.setParent(tree);
            tree.setLeft(currentTreePointer);
            currentTreePointer = currentTreePointer.getParent();
            return true;
        }
        return false;
    }

    static boolean searchBracketCase(Tree tree) {
        if (currentTreePointer.getParent().getOperation() == Operations.BRACKET) {
            currentTreePointer.getParent().setLeft(tree);
            tree.setParent(currentTreePointer.getParent());
            currentTreePointer.setParent(tree);
            tree.setLeft(currentTreePointer);
            currentTreePointer = currentTreePointer.getParent();
            return true;
        }
        return false;
    }

    static void rightToLeftParentalSwitch(Tree tree) {
        currentTreePointer.getParent().setRight(tree);
        tree.setParent(currentTreePointer.getParent());
        tree.setLeft(currentTreePointer);
        currentTreePointer.setParent(tree);
        currentTreePointer = currentTreePointer.getParent();
    }

    static void findNewLeaf(Tree tree, Operations operation) {
        switch (operation) {
            case AND:
                searchOperation(Operations.AND);
                if (searchRootCase(tree)) break;
                if (searchBracketCase(tree)) break;
                else if (currentTreePointer.getParent().getOperation() == Operations.OR || currentTreePointer.getParent().getOperation() == Operations.IMPL) {
                    rightToLeftParentalSwitch(tree);
                }
                break;
            case OR:
            case IMPL:
                searchOperation(Operations.OR);
                searchOperation(Operations.AND);
                if (searchRootCase(tree)) break;
                if (searchBracketCase(tree)) break;
                if (currentTreePointer.getParent().getOperation() == Operations.IMPL) {
                    rightToLeftParentalSwitch(tree);
                }
                break;
        }
    }

    static void handleOperation(Tree tree) {
        if (currentTreePointer.getParent() == null) {
            currentTreePointer.setParent(tree);
            tree.setLeft(currentTreePointer);
            currentTreePointer = currentTreePointer.getParent();
            return;
        }

        switch (tree.getOperation()) {
            case AND:
                if (currentTreePointer.getParent().getOperation() == Operations.AND || currentTreePointer.getParent().getOperation() == Operations.BRACKET) {
                    findNewLeaf(tree, Operations.AND);
                } else {
                    rightToLeftParentalSwitch(tree);
                }
                break;
            case OR:
                if (currentTreePointer.getParent().getOperation() == Operations.IMPL) {
                    rightToLeftParentalSwitch(tree);
                } else {
                    findNewLeaf(tree, Operations.OR);
                }
                break;
            case IMPL:
                findNewLeaf(tree, Operations.IMPL);
                break;
        }
    }

    static void handleBracket(String value) {
        if (value.equals("(")) {
            if (currentTreePointer == null) {
                globalTree = new Tree(null, null, null, null, Operations.BRACKET, true);
                currentTreePointer = globalTree;
                if (!Parser.currentPositivity) {
                    currentTreePointer.setPositivity(false);
                    Parser.currentPositivity = true;
                }
            } else {
                Tree inBrackets = new Tree(null, null, null, null, Operations.BRACKET, true);
                inBrackets.setParent(currentTreePointer);
                if (currentTreePointer.getOperation() == Operations.BRACKET) {
                    currentTreePointer.setLeft(inBrackets);
                    currentTreePointer = currentTreePointer.getLeft();
                } else {
                    currentTreePointer.setRight(inBrackets);
                    currentTreePointer = currentTreePointer.getRight();
                }
                if (!Parser.currentPositivity) {
                    inBrackets.setPositivity(false);
                    Parser.currentPositivity = true;
                }
            }
        } else if (value.equals(")")) {
            while (currentTreePointer.getParent().getOperation() != Operations.BRACKET) {
                currentTreePointer = currentTreePointer.getParent();
            }
            currentTreePointer = currentTreePointer.getParent();
        } else {
            System.exit(2);
        }
    }

    public static void printResult() {
        if (trueOutcomes == 0) {
            System.out.println("Unsatisfiable");
        } else if (falseOutcomes == 0) {
            System.out.println("Valid");
        } else {
            System.out.println("Satisfiable and invalid, " + trueOutcomes + " true and " + falseOutcomes + " false cases");
        }
    }

    public static void resetCurrentTreePointer() {
        if (currentTreePointer != null) {
            while (currentTreePointer.getParent() != null) {
                currentTreePointer = currentTreePointer.getParent();
            }
        }
    }
}
