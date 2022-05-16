package com.btl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.btl.model.Category;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textViewScore;
    private Spinner spinner;
    private Button button;

    private int highScore;

    private static final int REQUEST_CODE_QUESTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radiation();
        loadCategories();
        loadHighScore();
//        bat su kien click nut bat dau
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestion();
            }
        });
    }

    private void loadHighScore() {
        SharedPreferences preferences = getSharedPreferences("share",MODE_PRIVATE);
        highScore = preferences.getInt("highscore",0);
        textViewScore.setText("Diem cao: "+highScore);
    }

    private void startQuestion() {
        Category category = (Category) spinner.getSelectedItem();
        int categoryID = category.getId();
        String categoryName = category.getName();
//        chuyen qua activity question
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
//        gui du lieu id,name
        intent.putExtra("idcategories",categoryID);
        intent.putExtra("categoriesname",categoryName);
//        su dung startActivityForResult de co the nhan lai du lieu ket qua tra ve thong qua phuong thuc onActivityResult()
        startActivityForResult(intent,REQUEST_CODE_QUESTION);
    }

    private void radiation(){
        textViewScore = findViewById(R.id.diem);
        button = findViewById(R.id.btn);
        spinner = findViewById(R.id.spinner);
    }

//    load category
    private void loadCategories(){
        Database database = new Database(this);
//        lay du lieu danh sach chu de
        List<Category> categories = database.getDataCategories();
//        tao adapter
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,categories);
//        bo cuc hien thi chu de
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        gan chu de len spinner
        spinner.setAdapter(categoryArrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_QUESTION){
            if(resultCode==RESULT_OK){
                int score = data.getIntExtra("score",0);
                if(score>highScore){
                    updateHighScore(score);
                }
            }
        }
    }

    private void updateHighScore(int score) {
//        gan diem cao moi
        highScore = score;
//        hien thi
        textViewScore.setText("Diem cao: "+highScore);
//        luu tru
        SharedPreferences preferences = getSharedPreferences("share",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
//        gan gia tri cho diem cao moi vao khoa
        editor.putInt("highscore",highScore);
//        hoan thanh
        editor.apply();
    }
}