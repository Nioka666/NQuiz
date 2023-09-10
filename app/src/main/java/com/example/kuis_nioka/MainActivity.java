package com.example.kuis_nioka;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import java.util.Arrays;
public class MainActivity extends AppCompatActivity {
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private int totalScore = 0;
    private boolean[] radioButtonStatus;


    private final Question[] questions = {
            new Question("Apa yang diwakili oleh singkatan HTML dalam pemrograman web?", 0, new String[]{"HyperText Markup Language", "High Tech Modern Language", "Hyper Transfer and Manipulation Language", "Hyperlink and Text Markup Language"}, "HyperText Markup Language"),
            new Question("Tim sepak bola mana yang memiliki julukan 'The Red Devils'?", 3, new String[]{"Manchester City", "Liverpool FC", "Chelsea FC", "Manchester United"}, "Manchester United"),
            new Question("Apa singkatan dari CSS dalam pemrograman web?", 2, new String[]{"Cascading Style Sheet", "Computer Style Sheet", "Creative Style System", "Colorful Style Sheet"}, "Cascading Style Sheet"),
            new Question("Siapa presiden pertama Amerika Serikat?", 0, new String[]{"George Washington", "Thomas Jefferson", "Abraham Lincoln", "Benjamin Franklin"}, "George Washington"),
            new Question("Tim sepak bola yang memenangkan Piala Dunia FIFA 2018 adalah?", 1, new String[]{"Spanyol", "Prancis", "Belgia", "Kroasia"}, "Prancis"),
            new Question("Apa nama ilmuwan yang merumuskan hukum gravitasi?", 3, new String[]{"Galileo Galilei", "Albert Einstein", "Isaac Newton", "Stephen Hawking"}, "Isaac Newton"),
            new Question("Siapa pencipta bahasa pemrograman Python?", 1, new String[]{"Guido van Rossum", "Dennis Ritchie", "Linus Torvalds", "Larry Page"}, "Guido van Rossum"),
            new Question("Tim sepak bola yang bermarkas di Camp Nou adalah?", 2, new String[]{"Real Madrid", "AC Milan", "Barcelona", "Bayern Munich"}, "Barcelona"),
            new Question("Apa nama kapal Christopher Columbus untuk ke Amerika", 0, new String[]{"Santa Maria", "Nina", "Pinta", "Mayflower"}, "Santa Maria"),
            new Question("Apa itu kegiatan debugging dalam pemrograman?", 3, new String[]{"Proses memperbaiki kesalahan dalam kode", "Proses penulisan kode", "Proses memperbaiki kesalahan dalam kode", "Metode pengujian perangkat keras komputer."}, "Proses memperbaiki kesalahan dalam kode"),
    };

    private int currentQuestionIndex = 0;
    private AlertDialog.Builder builder;
    private RadioGroup radioGroup;
    private int[] selectedAnswers;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.radioGroup);
        selectedAnswers = new int[questions.length];

        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);

        nextButton.setEnabled(false);
        setButtonDisabledStyle(nextButton);
        setButtonDisabledStyle(previousButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveNextQuestion(view);
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movePreviousQuestion(view);
            }
        });

        setQuestion(currentQuestionIndex);

        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        setButtonDisabledStyle(nextButton);

        resetNextButton();
    }

    private void setButtonEnabledStyle(Button button) {
        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightPrimary));
        button.setTextColor(ContextCompat.getColor(this, R.color.primary));
    }

    private void setButtonDisabledStyle(Button button) {
        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkBgPrimary));
        button.setTextColor(ContextCompat.getColor(this, R.color.darkPrimary));
    }

    private void setQuestion(int currentQuestionIndex) {
        TextView questionTextView = findViewById(R.id.textView);
        questionTextView.setText(questions[currentQuestionIndex].getQuestionText());

        String[] answerOptions = questions[currentQuestionIndex].getAnswerOptions();

        RadioButton radioButton = findViewById(R.id.radioButton);
        radioButton.setText(answerOptions[0]);
        radioButton.setBackgroundResource(R.drawable.bg_radio); // Mengatur latar belakang ke bg_radio

        RadioButton radioButton2 = findViewById(R.id.radioButton2);
        radioButton2.setText(answerOptions[1]);
        radioButton2.setBackgroundResource(R.drawable.bg_radio); // Mengatur latar belakang ke bg_radio

        RadioButton radioButton3 = findViewById(R.id.radioButton3);
        radioButton3.setText(answerOptions[2]);
        radioButton3.setBackgroundResource(R.drawable.bg_radio); // Mengatur latar belakang ke bg_radio

        RadioButton radioButton4 = findViewById(R.id.radioButton4);
        radioButton4.setText(answerOptions[3]);
        radioButton4.setBackgroundResource(R.drawable.bg_radio); // Mengatur latar belakang ke bg_radio

        int selectedAnswer = selectedAnswers[currentQuestionIndex];
        if (selectedAnswer >= 0) {
            radioGroup.check(selectedAnswer);
        } else {
            radioGroup.clearCheck();
        }

        Button nextButton = findViewById(R.id.nextButton);

        if (selectedAnswers[currentQuestionIndex] > 0) {
            nextButton.setEnabled(true);
            setButtonEnabledStyle(nextButton);
        } else {
            nextButton.setEnabled(false);
            setButtonDisabledStyle(nextButton);
        }

        resetNextButton();
        Log.d("DEBUG", "Metode setQuestion dipanggil dengan indeks pertanyaan: " + currentQuestionIndex);
    }


    public void onRadioButton(View view) {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1) {
            selectedAnswers[currentQuestionIndex] = selectedRadioButtonId;

            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedAnswer = selectedRadioButton.getText().toString();
            String correctAnswer = questions[currentQuestionIndex].getCorrectAnswer();

            if (selectedAnswer.equals(correctAnswer)) {
                tampilDialog(selectedAnswer);
            } else {
                jawabanSalah();
                Button nextButton = findViewById(R.id.nextButton);
                nextButton.setEnabled(false); // Menonaktifkan tombol "Next" ketika jawaban salah
                setButtonDisabledStyle(nextButton);
            }

            boolean isAnswerSelected = true;

            // Mengatur latar belakang tombol radio berdasarkan status (dipilih atau tidak)
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (radioButton.isChecked()) {
                    radioButton.setBackgroundResource(R.drawable.bg_radio_active);
                } else {
                    radioButton.setBackgroundResource(R.drawable.bg_radio);
                }
            }

            Button nextButton = findViewById(R.id.nextButton);
            nextButton.setEnabled(true);
            setButtonEnabledStyle(nextButton);
        }
    }

    public void tampilDialog(String selectedAnswer) {
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Selamat !!!");

        String correctAnswer = questions[currentQuestionIndex].getCorrectAnswer();
        boolean isCorrect = selectedAnswer.equals(correctAnswer);

        if (isCorrect) {
            correctAnswers++;
            totalScore += 10;
            builder.setMessage("Jawaban kamu benar: " + selectedAnswer);
        } else {
            incorrectAnswers++;
            builder.setMessage("Jawaban kamu salah. Jawaban yang benar: " + correctAnswer);
            resetNextButton();
        }

        builder.setPositiveButton("OK, Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveNextQuestion(null);
                resetNextButton();
            }
        });

        builder.create().show();
    }

    public void moveNextQuestion(View view) {
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;

            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            fadeOut.setDuration(400);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // Animasi mulai
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onAnimationEnd(Animation animation) {
                    setQuestion(currentQuestionIndex);
                    Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                    fadeIn.setDuration(400);
                    findViewById(R.id.textView).startAnimation(fadeIn);

                    // Periksa apakah sudah mencapai halaman terakhir dan sesuaikan tampilan tombol "Next" dan "Previous"
                    Button previousButton = findViewById(R.id.previousButton);
                    previousButton.setEnabled(true);
                    setButtonEnabledStyle(previousButton);

                    Button nextButton = findViewById(R.id.nextButton);
                    if (currentQuestionIndex == questions.length - 1) {
                        boolean isAnswerSelected = selectedAnswers[currentQuestionIndex] > 0;
                        if (isAnswerSelected) {
                            nextButton.setText("Finish");
                            nextButton.setEnabled(true);
                            setButtonEnabledStyle(nextButton);
                        } else {
                            nextButton.setText("Finish");
                            nextButton.setEnabled(false);
                            setButtonDisabledStyle(nextButton);
                        }
                    } else {
                        nextButton.setText("Next");
                        nextButton.setEnabled(false);
                        setButtonDisabledStyle(nextButton);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // Animasi diulang
                }
            });

            findViewById(R.id.textView).startAnimation(fadeOut);
        } else {
            showScore();
        }
    }

    public void movePreviousQuestion(View view) {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            // Kembalikan pilihan radio jika ada yang tersimpan
            int selectedAnswerIndex = selectedAnswers[currentQuestionIndex];
            if (selectedAnswerIndex >= 0) {
                radioGroup.check(selectedAnswerIndex);
            }

            setQuestion(currentQuestionIndex);

            // Periksa apakah tombol "Previous" harus dinonaktifkan
            Button previousButton = findViewById(R.id.previousButton);
            if (currentQuestionIndex == 0) {
                previousButton.setEnabled(false);
                setButtonDisabledStyle(previousButton);
            } else {
                previousButton.setEnabled(true);
                setButtonEnabledStyle(previousButton);
            }

            Button nextButton = findViewById(R.id.nextButton);
            nextButton.setEnabled(true);
            setButtonEnabledStyle(nextButton);
        }
    }

    public void showScore() {
        AlertDialog.Builder scoreDialog = new AlertDialog.Builder(this);
        scoreDialog.setCancelable(false);
        scoreDialog.setTitle("Skor Akhir\n");

        String message = "Jawaban Benar: " + correctAnswers + "\n" +
                "Jawaban Salah: " + incorrectAnswers + "\n" +
                "\nTotal Score: " + totalScore + "\n";

        scoreDialog.setMessage(message);

        scoreDialog.setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        scoreDialog.setNegativeButton("Ulangi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetQuiz();
            }
        });
        scoreDialog.create().show();
    }

    private void resetQuiz() {
        currentQuestionIndex = 0;
        correctAnswers = 0;
        incorrectAnswers = 0;
        totalScore = 0;
        Arrays.fill(selectedAnswers, -1);
        setQuestion(currentQuestionIndex);

        // Menghapus pilihan RadioButton
        radioGroup.clearCheck();

        // Mengatur ulang latar belakang RadioButton ke bg_radio
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setBackgroundResource(R.drawable.bg_radio);
        }

        Button previousButton = findViewById(R.id.previousButton);
        previousButton.setEnabled(false);
        setButtonDisabledStyle(previousButton);

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setText("Next");
        nextButton.setEnabled(false);
        setButtonDisabledStyle(nextButton);
    }

    private void resetNextButton() {
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        setButtonDisabledStyle(nextButton);
    }

    public void jawabanSalah() {
        Toast.makeText(this, "Jawaban kamu Salah", Toast.LENGTH_SHORT).show();

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        setButtonDisabledStyle(nextButton);

    }
}
