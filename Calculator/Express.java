import java.util.Stack;

public class Express {
    public static void main(String[] args) {
        // 10
        String expression = "2*(4-2)+(2*3)";
        // String expression = "-3*2";

        double result = calculate(expression);
        System.out.println("Result: " + result);
    }

    public static double calculate(String expression) {
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        
        // 記錄前一個charator
        char c_0 = ' ';

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            // System.out.println(c);;

            if (c == ' ') {
                continue;
            } 
            else if (c == '(') {
                operatorStack.push(c);
            } 
            else if (Character.isDigit(c)) {
                // 可動態地修改字串
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--; 
                double operand = Double.parseDouble(sb.toString());
                operandStack.push(operand);
            } 
            else if (c == '+' || c == '-') {
                // 1-12, -12, (1)-(10)
                if (!Character.isDigit(c_0) && Character.isDigit(expression.charAt(i+1))){
                    StringBuilder sb = new StringBuilder();
                    sb.append(expression.charAt(i++));
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        sb.append(expression.charAt(i++));
                    }
                    i--; 
                    double operand = Double.parseDouble(sb.toString());
                    operandStack.push(operand);
                }
                else{
                    while (!operatorStack.isEmpty() && (operatorStack.peek() == '*' || operatorStack.peek() == '/')) {
                        processOperator(operandStack, operatorStack);
                    }
                    operatorStack.push(c);
                }

            } 
            else if (c == '*' || c == '/') {
                operatorStack.push(c);
            } 
            else if (c == ')') {
                while (operatorStack.peek() != '(') {
                    processOperator(operandStack, operatorStack);
                }
                operatorStack.pop(); // 移地 '('

                if (!operatorStack.isEmpty() && (operatorStack.peek() == '*' || operatorStack.peek() == '/')) {
                    processOperator(operandStack, operatorStack);
                }
            }
            c_0 = expression.charAt(i);
        }

        while (!operatorStack.isEmpty()) {
            processOperator(operandStack, operatorStack);
        }

        return operandStack.pop();
    }

    private static void processOperator(Stack<Double> operandStack, Stack<Character> operatorStack) {
        char operator = operatorStack.pop();
        double operand2 = operandStack.pop();
        double operand1 = operandStack.pop();

        switch (operator) {
            case '+':
                operandStack.push(operand1 + operand2);
                break;
            case '-':
                operandStack.push(operand1 - operand2);
                break;
            case '*':
                operandStack.push(operand1 * operand2);
                break;
            case '/':
                operandStack.push(operand1 / operand2);
                break;
        }
    }
}
