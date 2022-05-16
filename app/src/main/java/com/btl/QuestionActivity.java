package com.btl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.btl.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCategory;
    private TextView textViewCountDown;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button buttonConfirm;

    private CountDownTimer countDownTimer;
    private ArrayList<Question> questionArrayList;
    private long timeLeftINMillis;
    private int questionCounter;
    private int questionSize;
    private Question currentQuestion;

    private int Score;
    private boolean answered;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        radiation();
//        nhan du lieu category
        Intent intent = getIntent();
        int categoryID = intent.getIntExtra("idcategories",0);
        String categoryName = intent.getStringExtra("categoriesname");
//        hien thi category
        textViewCategory.setText("Chu de: "+categoryName);
        Database database = new Database(this);
//        danh sach list chua cau hoi
        questionArrayList = database.getQuestions(categoryID);
//        lay kich co danh sach = tong so cau hoi
        questionSize = questionArrayList.size();
//        dao vi tri cau hoi
        Collections.shuffle(questionArrayList);
//        show cau hoi va dap an
        showNextQuestion();
//        button xac nhan, tiep tuc, hoan thanh
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                neu chua tra loi cau hoi
                if(!answered){
                    if(rb1.isChecked() ||rb2.isChecked() ||rb3.isChecked() ||rb4.isChecked()){
                        checkAnswer();
                    }
                    else {
                        Toast.makeText(QuestionActivity.this, "Hay chon dap an", Toast.LENGTH_SHORT).show();
                    }
                }
//                neu da tra loi cau hoi
                else{
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);
//        xoa chon
        radioGroup.clearCheck();
//        neu con cau hoi
        if(questionCounter < questionSize){
//            lay du lieu
            currentQuestion = questionArrayList.get(questionCounter);
//            hien thi cau hoi
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());
//            tang so cau hoi len sau moi lan tra loi
            questionCounter++;
//            set vi tri cau hoi hien tai
            textViewQuestionCount.setText("Cau hoi: "+questionCounter+" / "+questionSize);
//            false: dang tra loi, dang show
            answered = false;
//            gan ten cho button
            buttonConfirm.setText("Xac nhan");
//            thoi gian chay 30s
            timeLeftINMillis = 30000;
//            dem nguoc thoi gian
            startCountDown();
        }
        else {
            finishQuestion();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftINMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftINMillis = l;
//                update thoi gian
                updateCountDownText();
            }

            @Override
            public void onFinish() {
//                het gio
                timeLeftINMillis = 0;
                updateCountDownText();
//                ktra dap an
                checkAnswer();
            }
        }.start();
    }

    private void checkAnswer() {
        answered=true;
//        tra ve radiobutton trong fbGroup duoc check
        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
//        vi tri cua cau da chon
        int answer = radioGroup.indexOfChild(rbSelected)+1;
//        neu tra loi dung dap an
        if(answer == currentQuestion.getAnswer()){
            Score = Score+10;
            textViewScore.setText("Diem: "+Score);
        }
//        show dap an dung
        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
//        ktra dap an set mau va hien thi dap an len man hinh
        switch (currentQuestion.getAnswer()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Dap an dung la A");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Dap an dung la B");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Dap an dung la C");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestion.setText("Dap an dung la D");
                break;
        }
        if(questionCounter<questionSize){
            buttonConfirm.setText("Tiep tuc");
        }
        else{
            buttonConfirm.setText("Hoan thanh");
        }
    }

    private void updateCountDownText() {
//        tinh phut
        int minutes = (int) ((timeLeftINMillis/1000)/60);
//        tinh giay
        int seconds = (int) ((timeLeftINMillis/1000)%60);
//        dinh dang thoi gian
        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        textViewCountDown.setText(timeFormatted);
//        neu duoi 10s thi chuyen mau do k thi van mau den
        if(timeLeftINMillis<10000){
            textViewCountDown.setTextColor(Color.RED);
        }
        else{
            textViewCountDown.setTextColor(Color.BLACK);
        }
    }

    // thoat ve giao dien chinh
    private void finishQuestion() {
//        chua du lieu gui qua activity main
        Intent resultIntent = new Intent();
        resultIntent.putExtra("score",Score);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

//    back
    @Override
    public void onBackPressed() {
        count++;
        if(count>=1){
            finishQuestion();
        }
        count=0;
    }

    private void radiation(){
        textViewQuestion = findViewById(R.id.txtView_question);
        textViewCategory = findViewById(R.id.txtView_category);
        textViewScore = findViewById(R.id.txtView_score);
        textViewQuestionCount = findViewById(R.id.txtView_question_count);
        textViewCountDown = findViewById(R.id.txtView_countdown);
        radioGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirm = findViewById(R.id.btn_confirm);
    }
}