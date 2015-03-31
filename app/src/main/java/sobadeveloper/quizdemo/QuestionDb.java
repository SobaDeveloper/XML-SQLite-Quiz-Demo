package sobadeveloper.quizdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * QuestionDb.java
 * Purpose: Questions database
 *
 * @author Levi Hsiao
 */
public class QuestionDb {

    // Database name
    private static final String DB_NAME = "data.db";
    // Version number
    private static final int DB_VERSION = 1;

    // Question table
    private static final String TABLE_QUESTION = "question_table";
    // Question column values
    private static final String KEY_ID = "_id";
    private static final String KEY_TEXT = "text";
    private static final String KEY_CHOICE1 = "choice_1";
    private static final String KEY_CHOICE2 = "choice_2";
    private static final String KEY_CHOICE3 = "choice_3";
    private static final String KEY_CHOICE4 = "choice_4";
    private static final String KEY_ANSWER = "answer";

    //Question table creation statement
    private static final String TABLE_QUESTION_CREATE = "CREATE TABLE " + TABLE_QUESTION + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_TEXT + " TEXT NOT NULL, "
            + KEY_CHOICE1 + " TEXT NOT NULL, "
            + KEY_CHOICE2 + " TEXT NOT NULL, "
            + KEY_CHOICE3 + " TEXT NOT NULL, "
            + KEY_CHOICE4 + " TEXT NOT NULL, "
            + KEY_ANSWER + " TEXT NOT NULL);";

    private DbHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public QuestionDb(Context context) {
        this.context = context;
    }

    public QuestionDb open() throws SQLiteException {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void createQuestion(Question q) {
        ContentValues cv = new ContentValues();
        List<String> choiceList = new ArrayList<>(q.getChoiceList());
        cv.put(KEY_TEXT, q.getText());
        cv.put(KEY_CHOICE1, choiceList.get(0));
        cv.put(KEY_CHOICE2, choiceList.get(1));
        cv.put(KEY_CHOICE3, choiceList.get(2));
        cv.put(KEY_CHOICE4, choiceList.get(3));
        cv.put(KEY_ANSWER, q.getAnswer());
        db.insert(TABLE_QUESTION, null, cv);
    }

    // Get ten random questions
    public List<Question> getTenQuestions() {
        List<Question> questionList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTION
                + " ORDER BY Random() LIMIT 10";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Question q = new Question();
                q.setID(cursor.getInt(0));
                q.setText(cursor.getString(1));

                List<String> choiceList = new ArrayList<>();
                choiceList.add(cursor.getString(2));
                choiceList.add(cursor.getString(3));
                choiceList.add(cursor.getString(4));
                choiceList.add(cursor.getString(5));

                // Shuffle the choices
                Collections.shuffle(choiceList);
                q.setChoiceList(choiceList);

                q.setAnswer(cursor.getString(6));

                questionList.add(q);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_QUESTION_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
            onCreate(db);
        }
    }
}
