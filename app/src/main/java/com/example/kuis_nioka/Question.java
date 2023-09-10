package com.example.kuis_nioka;
// nioka666
public class Question {
    private String questionText;
    private int correctAnswerIndex;
    private String[] answerOptions;
    private String correctAnswerText;

    public Question(String questionText, int correctAnswerIndex, String[] answerOptions, String correctAnswerText) {
        this.questionText = questionText;
        this.correctAnswerIndex = correctAnswerIndex;
        this.answerOptions = answerOptions;
        this.correctAnswerText = correctAnswerText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getAnswerOptions() {
        return answerOptions;
    }

    public String getCorrectAnswer() {
        return correctAnswerText;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
    // Getter methods
}
