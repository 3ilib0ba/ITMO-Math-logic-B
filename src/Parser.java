import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Parser {
    static int position = 0;
    static List<String> data = new LinkedList<>();
    static boolean currentPositivity = true;

    static List<String> prettier(String input) {
        List<String> data = Arrays.asList(input.split(""));

        data = data.stream()
                .filter(i -> !Objects.equals(i, " ") && !Objects.equals(i, ">") && !Objects.equals(i, "\r") && !Objects.equals(i, "\n"))
                .collect(Collectors.toList());

        for (int i = 0; i < data.size(); i++) {
            if (isConstant(data.get(i)) && i != data.size() - 1 && isConstant(data.get(i + 1))) {
                data.set(i, data.get(i) + data.get(i + 1));
                data.remove(1 + i--);
            } if (Objects.equals(data.get(i), "-")){
                data.set(i, "->");
            }
        }
        return data;
    }

    static boolean isConstant(String string) {
        return string.matches("[A-Z]?([0-9A-Z']*)");
    }

    static boolean isOperation(String string) {
        return string.equals("&") || string.equals("|") || string.equals("->");
    }

    static void entryPoint() {
        try {
            String nowValue = Parser.data.get(Parser.position++);

            if (isOperation(nowValue)) {
                Expression.handleOperation(Parser.operationParser(nowValue));
            } else if (nowValue.equals("!")) {
                Parser.currentPositivity = !Parser.currentPositivity;
            } else {
                if (nowValue.equals("(") || nowValue.equals(")")) {
                    Expression.handleBracket(nowValue);
                } else {
                    Expression.handleConstant(Parser.constParser(nowValue));
                }
            }
            entryPoint();
        } catch (Exception ignored) {
        }
    }

    static Tree constParser(String constant) {
        if (currentPositivity) {
            return new Tree(null, null, null, constant, Operations.CONST, true);
        } else {
            currentPositivity = true;
            return new Tree(null, null, null, constant, Operations.CONST, false);
        }
    }

    static Tree operationParser(String operation) {
        switch (operation) {
            case "&":
                return new Tree(null, null, null, operation, Operations.AND, true);
            case "->":
                return new Tree(null, null, null, operation, Operations.IMPL, true);
            case "|":
                return new Tree(null, null, null, operation, Operations.OR, true);
        }
        return new Tree(null, null, null, operation, null, true);
    }

    static void parse(String data) {
        Parser.data = prettier(data);
        entryPoint();
    }
}
