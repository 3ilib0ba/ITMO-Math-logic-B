public class Compute {
    static void compute() {
        Expression.resetCurrentTreePointer();
        loadBooleans();
    }

    static void loadBooleans() {
        String[] keysArray = new String[Expression.Values.size()];
        Expression.Values.keySet().toArray(keysArray);
        for (int i = 0; i < Math.pow(2, Expression.Values.size()); i++) {
            StringBuilder converted = new StringBuilder(Integer.toBinaryString(i));
            int diff = Expression.Values.size() - converted.length();
            for (int j = 0; j < diff; j++) {
                converted.insert(0, "0");
            }
            for (int j = 0; j < Expression.Values.size(); j++) {
                if (converted.charAt(j) == '0') {
                    Expression.Values.put(keysArray[j], false);
                } else {
                    Expression.Values.put(keysArray[j], true);
                }
            }
            entryPoint();
        }
    }
    static void entryPoint() {
        if (computeTree(Expression.currentTreePointer)) {
            Expression.trueOutcomes++;
        } else {
            Expression.falseOutcomes++;
        }
    }

    static boolean computeTree(Tree tree) {
        switch (tree.getOperation()) {
            case AND:
                return tree.getPositivity() == (computeTree(tree.getLeft()) && computeTree(tree.getRight()));
            case OR:
                return tree.getPositivity() == (computeTree(tree.getLeft()) || computeTree(tree.getRight()));
            case IMPL:
                return tree.getPositivity() == ((!computeTree(tree.getLeft())) || computeTree(tree.getRight()));
            case BRACKET:
                return tree.getPositivity() == computeTree(tree.getLeft());
            case CONST:
                return tree.getPositivity() == Expression.Values.get(tree.getConstant());
            default:
                System.exit(3);
        }
        System.exit(3);
        return false;
    }
}