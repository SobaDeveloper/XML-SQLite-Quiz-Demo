package sobadeveloper.quizdemo;

import java.util.List;

/**
 * Question.java
 * Purpose: Contains all the Question objects
 *
 * @author Levi Hsiao
 */
public class Question {

    private int ID;
    private String text;
    private String answer;
    private List<String> choiceList;

    public Question() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<String> choiceList) {
        this.choiceList = choiceList;
    }

}
