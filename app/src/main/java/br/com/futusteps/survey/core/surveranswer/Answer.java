package br.com.futusteps.survey.core.surveranswer;

import java.io.Serializable;
import java.util.List;

public class Answer implements Serializable {

    private int idQuestion;
    private String answerText;
    private List<Integer> alternatives;

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public List<Integer> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Integer> alternatives) {
        this.alternatives = alternatives;
    }
}
