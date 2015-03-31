package sobadeveloper.quizdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * QuizActivity.java
 * Purpose: Simple implementation of a 10-question geography quiz
 *
 * @author Levi Hsiao
 */
public class QuizActivity extends ActionBarActivity implements View.OnClickListener {

    private final Handler handler = new Handler();
    private TextView tvNumber, tvQuestion, tvResponse;
    private Button bChoice1, bChoice2, bChoice3, bChoice4, b_restart;
    private Toolbar toolbar;
    private Animation shakeAnimation;
    private QuestionDb db;
    private List<Question> qList;
    private Question currentQ;
    private LinearLayout buttonContainer;
    private int index;
    private int score;
    private int qPoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        setupUI();
        startQuiz();
    }

    // Retrieve fresh batch of questions and reset UI / quiz related values
    private void startQuiz() {

        getQuestions();
        index = 0;
        score = 0;
        qPoints = 10;
        b_restart.setVisibility(View.INVISIBLE);
        tvResponse.setVisibility(View.INVISIBLE);
        setQuestionView();
    }

    // Set the view for each question
    private void setQuestionView() {

        if (index < qList.size()) {
            qPoints = 10;
            currentQ = qList.get(index);
            int qId = index + 1;

            tvResponse.setVisibility(View.INVISIBLE);
            enableButtons();

            tvNumber.setText("Question " + qId + "/" + qList.size());
            tvQuestion.setText(currentQ.getText());
            bChoice1.setText(currentQ.getChoiceList().get(0));
            bChoice2.setText(currentQ.getChoiceList().get(1));
            bChoice3.setText(currentQ.getChoiceList().get(2));
            bChoice4.setText(currentQ.getChoiceList().get(3));
            index++;
        } else {
            b_restart.setVisibility(View.VISIBLE);
            tvResponse.setText("YOUR FINAL SCORE IS: " + score + " out of 100");
            b_restart.setText("Restart");
        }
    }

    // Disable all buttons so that they cannot be clicked
    public void disableButtons() {

        for (int i = 0; i < buttonContainer.getChildCount(); i++) {
            Button button = (Button) buttonContainer.getChildAt(i);
            button.setClickable(false);
        }
    }

    // Enable all buttons
    public void enableButtons() {

        for (int i = 0; i < buttonContainer.getChildCount(); i++) {
            Button button = (Button) buttonContainer.getChildAt(i);
            button.setEnabled(true);
            button.setClickable(true);
            button.setTextColor(getResources().getColor(R.color.black));
        }
    }

    // Button functions
    @Override
    public void onClick(View v) {

        Button button = (Button) findViewById(v.getId());

        if (button.getText().equals("Restart")) {
            startQuiz();
        } else if (currentQ.getAnswer().equals(button.getText())) {
            score = score + qPoints;
            tvResponse.setVisibility(View.VISIBLE);
            tvResponse.setText("CORRECT!");
            button.setTextColor(getResources().getColor(R.color.green));
            disableButtons();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setQuestionView();
                }
            }, 1000);

        } else {
            qPoints = qPoints - 3;
            tvResponse.setVisibility(View.VISIBLE);
            tvResponse.setText("INCORRECT!");
            button.startAnimation(shakeAnimation);
            button.setTextColor(getResources().getColor(R.color.red));
            button.setClickable(false);
        }
    }

    // Retrieve 10 random questions from database
    private void getQuestions() {

        db.open();
        qList = new ArrayList<>(db.getTenQuestions());
        db.close();
    }

    public void setupUI() {

        db = new QuestionDb(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvResponse = (TextView) findViewById(R.id.tv_response);
        buttonContainer = (LinearLayout) findViewById(R.id.button_container);
        b_restart = (Button) findViewById(R.id.b_restart);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3);

        bChoice1 = (Button) findViewById(R.id.button1);
        bChoice2 = (Button) findViewById(R.id.button2);
        bChoice3 = (Button) findViewById(R.id.button3);
        bChoice4 = (Button) findViewById(R.id.button4);

        bChoice1.setOnClickListener(this);
        bChoice2.setOnClickListener(this);
        bChoice3.setOnClickListener(this);
        bChoice4.setOnClickListener(this);
        b_restart.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
