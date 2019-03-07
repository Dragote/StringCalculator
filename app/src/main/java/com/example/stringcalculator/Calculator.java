package com.example.stringcalculator;

import java.io.IOException;
import java.util.Stack;


public class Calculator {
    public static String getResult(String expression) throws IOException {
        return Double.toString(rpnToAnswer(expressionToRPN(expression)));
    }


    public static String expressionToRPN(String expr) throws IOException {
        StringBuilder current = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        expr = expr.replaceAll("\\s+",""); //Удаляем все пробелы и табы из выражения
        int priority;

        for(int i=0;i<expr.length();i++){
            priority = getPriority(expr.charAt(i));

            switch (priority){
                case (0):
                    current.append(expr.charAt(i));
                    break;
                case (1):
                    stack.push(expr.charAt(i));
                    break;
                case(2):
                     //Блок проверки на унарный минус
                     if(expr.charAt(i)=='-' && i==0){
                         current.append(expr.charAt(i));
                         continue;
                     }
                     else if(expr.charAt(i)=='-' && getPriority(expr.charAt(i-1))>0&& getPriority(expr.charAt(i+1))==0){
                         current.append(expr.charAt(i));
                         continue;
                     }
                     //__________________
                     current.append(' ');
                     while (!stack.empty()){
                         if(getPriority(stack.peek())>=priority) {
                             current.append(stack.pop());
                             current.append(' ');

                         }
                         else break;
                     }
                     stack.push(expr.charAt(i));
                     break;
                case (3):
                    current.append(' ');
                    while (!stack.empty()){
                        if(getPriority(stack.peek())>=priority) {
                            current.append(stack.pop());
                            current.append(' ');
                        }
                        else break;
                    }
                    stack.push(expr.charAt(i));
                    break;
                case(-1):
                    current.append(' ');
                    while (getPriority(stack.peek())!=1) current.append(stack.pop());
                    stack.pop();
                    break;
                default:
                    throw new IOException();
            }
        }
        current.append(' ');
        while (!stack.empty()) current.append(stack.pop());
        current.append(' ');
        return current.toString();
    }

    public static double rpnToAnswer(String rpn) throws IOException {
        StringBuilder operand= new StringBuilder();
        Stack<Double> stack = new Stack<>();

        for(int i=0;i<rpn.length();i++){
            if(rpn.charAt(i)==' ')continue;
            if(rpn.charAt(i)=='-' && getPriority(rpn.charAt(i+1))==0) operand.append(rpn.charAt(i++));
            if(getPriority(rpn.charAt(i))==0){
                while (rpn.charAt(i)!=' ') {
                    operand.append(rpn.charAt(i++));
                    if (i == rpn.length()) break;
                }
                stack.push(Double.parseDouble(operand.toString()));
                operand = new StringBuilder();
            }
            if(getPriority(rpn.charAt(i))>=2){
                double a = stack.pop();
                double b = stack.pop();

                switch (rpn.charAt(i)){
                    case ('+'):
                        stack.push(b+a);
                        break;
                    case ('-'):
                        stack.push(b-a);
                        break;
                    case ('*'):
                        stack.push(b*a);
                        break;
                    case ('/'):
                        stack.push(b/a);
                        break;
                }
            }
        }
        Double result = stack.pop();
        if(stack.isEmpty()) return result;
        else throw new IOException();
    }

    private static int getPriority(char token){
        if(token =='*'||token=='/') return 3;
        else if(token =='+'||token=='-')return 2;
        else if(token =='(') return 1;
        else if(".0123456789".indexOf(token)!=-1) return 0;
        else if(token==')') return -1;
        else return -2;
    }
}
