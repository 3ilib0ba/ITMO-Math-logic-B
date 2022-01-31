import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String test = scanner.next();
        Parser.parse(test);
        Compute.compute();
        Expression.printResult();
    }
}
/*
https://miro.com/app/board/uXjVOYNbE7k=/

A&!A
Unsatisfiable
A->!B123
Satisfiable and invalid, 3 true and 1 false cases
((PPP->PPP')->PPP)->PPP
Valid
*/