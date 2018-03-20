package com.n256coding.crazyalarm.helper;


import java.util.Random;

public class MathEquation {
    private Random random;
    private double[] equation;
    private String mathOperator;
    private String[] mathOperators;
    private double[] answers;

    public MathEquation() {
        random = new Random();
        equation = new double[3];
        answers = new double[4];
        mathOperators = new String[]{"+", "-", "*"};
        generateEquation();
    }

    public void generateEquation() {
        equation[0] = random.nextInt(1000);
        equation[1] = random.nextInt(1000);
        mathOperator = generateMathOperator();
        switch (mathOperator) {
            case "+":
                equation[2] = equation[0] + equation[1];
                break;
            case "-":
                equation[2] = equation[0] - equation[1];
                break;
            case "*":
                equation[2] = equation[0] * equation[1];
        }
        generateAnswers();
    }

    public String generateMathOperator() {
        return mathOperators[random.nextInt(3)];
    }

    public void generateAnswers() {
        int correctAnswerPosition = random.nextInt(4);
        for (int i = 0; i < answers.length; i++) {
            if (i != correctAnswerPosition) {
                answers[i] = getRelatedNumber(equation[2]);
            } else {
                answers[i] = equation[2];
            }
        }
    }

    public double getRelatedNumber(double number) {
        switch (random.nextInt(3)) {
            case 0:
                number = number + random.nextInt((int) Math.abs(number));
                break;
            case 1:
                number = number - random.nextInt((int) Math.abs(number));
                break;
            case 2:
                number = number * random.nextInt((int) Math.abs(number));
        }
        return number;
    }

    public void refreshEquation() {
        generateEquation();
    }

    public double getCorrectAnswer() {
        return equation[2];
    }

    public double getAnswerChoice(int i) {
        if (i < 4) {
            return Math.round(answers[i] * 100) / 100;
        }
        return 0;
    }

    @Override
    public String toString() {
        return equation[0] + " " + mathOperator + " " + equation[1];
    }
}
