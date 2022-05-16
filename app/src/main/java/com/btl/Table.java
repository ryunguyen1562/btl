package com.btl;

import android.provider.BaseColumns;

public final class Table {
    private Table(){}
//    du lieu bang categories
    public static class CategoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME = "name";
    }
//    du lieu bang questions
    public static class QuestionsTable implements BaseColumns{
        public static final String TABLE_NAME = "questions";
//        cau hoi
        public static final String COLUMN_QUESTION = "question";

//        4 lua chon
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";

//        dap an
        public static final String COLUMN_ANSWER = "answer";

//        id chuyen muc
        public static final String COLUMN_CATEGORY_ID = "id_categories";
    }
}
