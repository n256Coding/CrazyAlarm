package com.n256coding.crazyalarm.model;


import java.util.Random;

/**
 * This class represents the mathematical equation which given at the alarm time.
 * Simply this will create a random mathematical equation and 1 answer with 3 wrong answers at the runtime.
 */
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

    /**
     * generates a new mathematical equation
     */
    public void generateEquation() {
        mathOperator = generateMathOperator();
        if(mathOperator.equals("*")){
            equation[0] = random.nextInt(50);
            equation[1] = random.nextInt(50);
        }else{
            equation[0] = random.nextInt(1000);
            equation[1] = random.nextInt(1000);
        }
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

    /**
     * Randomly select a math operator from +, - and *
     * @return selected operator
     */
    public String generateMathOperator() {
        return mathOperators[random.nextInt(mathOperators.length)];
    }

    /**
     * Generate 1 correct answer and 3 wrong answers from generated equation
     */
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

    /**
     * generate mostly similar but wrong answers to given correct answer
     * @param number the correct answer
     * @return likely similar answer
     */
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


    /**
     * returns the correct answer for the current math equation
     * @return correct answer
     */
    public double getCorrectAnswer() {
        return equation[2];
    }

    /**
     * returns the generated answer value of the given index
     * @param i index of the answer
     * @return answer
     */
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
