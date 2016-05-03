package br.com.futusteps.survey.core.surveranswer;

import java.io.Serializable;
import java.util.List;

public class SurveyAnswer implements Serializable {

    private int surveyId;
    private List<Answer> answers;
    private UserData userData;

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
