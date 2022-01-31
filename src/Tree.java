public class Tree {
    private Tree left;
    private Tree right;
    private Tree parent;
    private boolean positivity;
    private final String constant;
    private final Operations operation;

    public Tree(Tree left, Tree right, Tree parent, String constant, Operations operation, boolean positivity) {
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.constant = constant;
        this.operation = operation;
        this.positivity = positivity;
    }

    public Operations getOperation() {
        return operation;
    }

    public String getConstant() { return constant; }

    public Tree getParent() {
        return parent;
    }

    public void setRight(Tree right) {
        this.right = right;
    }

    public void setLeft(Tree left) {
        this.left = left;
    }

    public Tree getLeft() {
        return left;
    }

    public Tree getRight() {
        return right;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public void setPositivity(boolean positivity) {
        this.positivity = positivity;
    }

    public boolean getPositivity() {
        return this.positivity;
    }
}