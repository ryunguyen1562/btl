package com.btl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.btl.model.Category;
import com.btl.model.Question;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
//    ten database
    public static final String DATABASE_NAME = "question.db";
//    vertion
    public static final int VERTION = 1;

    private SQLiteDatabase db;
    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERTION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db= sqLiteDatabase;
//      bien bang category
        final String CATEGORIES_TABLE = "CREATE TABLE " +
                Table.CategoriesTable.TABLE_NAME + "(" +
                Table.CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Table.CategoriesTable.COLUMN_NAME + " TEXT " + ")";
//        bien bang question
        final String QUESTIONS_TABLE = "CREATE TABLE " +
                Table.QuestionsTable.TABLE_NAME + "(" +
                Table.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Table.QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                Table.QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                Table.QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                Table.QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                Table.QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                Table.QuestionsTable.COLUMN_ANSWER + " INTEGER, " +
                Table.QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + Table.QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                Table.CategoriesTable.TABLE_NAME + "(" + Table.CategoriesTable._ID + ")" + "ON DELETE CASCADE" + ")";
//        tao bang
        db.execSQL(CATEGORIES_TABLE);
        db.execSQL(QUESTIONS_TABLE);

//        insert du lieu
        CreateCategories();
        CreateQuestions();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Table.CategoriesTable.COLUMN_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Table.QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

//    insert chu de vao database
    private void insertCategories(Category category){
        ContentValues values = new ContentValues();
        values.put(Table.CategoriesTable.COLUMN_NAME,category.getName());
        db.insert(Table.CategoriesTable.TABLE_NAME,null,values);
    }

//    cac gia tri insert
    private void CreateCategories(){
        Category c1= new Category("Ngu Van");
        insertCategories(c1);
        Category c2= new Category("Lich Su");
        insertCategories(c2);
        Category c3= new Category("Dia Ly");
        insertCategories(c3);
        Category c4= new Category("Toan Hoc");
        insertCategories(c4);
    }

//    insert cau hoi vao database
    private void insertQuestions(Question question){
        ContentValues values = new ContentValues();
        values.put(Table.QuestionsTable.COLUMN_QUESTION,question.getQuestion());
        values.put(Table.QuestionsTable.COLUMN_OPTION1,question.getOption1());
        values.put(Table.QuestionsTable.COLUMN_OPTION2,question.getOption2());
        values.put(Table.QuestionsTable.COLUMN_OPTION3,question.getOption3());
        values.put(Table.QuestionsTable.COLUMN_OPTION4,question.getOption4());
        values.put(Table.QuestionsTable.COLUMN_ANSWER,question.getAnswer());
        values.put(Table.QuestionsTable.COLUMN_CATEGORY_ID,question.getCategoryID());
        db.insert(Table.QuestionsTable.TABLE_NAME,null,values);
    }

//    tao bang cau hoi
    private void CreateQuestions(){
        Question q1 = new Question("(10+10)*2-30/5=",
        "A. 222",
        "B. 375",
        "C. 56",
        "D. 194", 4,4);
        insertQuestions(q1);
    }
    public List<Category> getDataCategories(){
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+Table.CategoriesTable.TABLE_NAME,null);
        if(c.moveToFirst()){
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndexOrThrow(Table.CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndexOrThrow(Table.CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            }
            while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }

//    lay du lieu cau hoi va dap an co id= id_category theo chu de da chon
    public ArrayList<Question> getQuestions(int categoryID){
        ArrayList<Question> questionArrayList = new ArrayList<>();
        db=getReadableDatabase();
        String selection = Table.QuestionsTable.COLUMN_CATEGORY_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID)};
        Cursor c= db.query(Table.QuestionsTable.TABLE_NAME,
                null,selection,selectionArgs,null,null,null);
        if (c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndexOrThrow(Table.QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndexOrThrow(Table.QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndexOrThrow(Table.QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndexOrThrow(Table.QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndexOrThrow(Table.QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndexOrThrow(Table.QuestionsTable.COLUMN_OPTION4)));
                question.setAnswer(c.getInt(c.getColumnIndexOrThrow(Table.QuestionsTable.COLUMN_ANSWER)));
                question.setCategoryID(c.getInt(c.getColumnIndexOrThrow(Table.QuestionsTable.COLUMN_CATEGORY_ID)));
                questionArrayList.add(question);
            }
            while (c.moveToNext());
        }
        c.close();
        return questionArrayList;
    }
}
