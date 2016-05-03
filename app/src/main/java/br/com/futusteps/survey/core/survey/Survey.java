package br.com.futusteps.survey.core.survey;

import java.io.Serializable;
import java.util.List;

public class Survey implements Serializable{

    private int id;
    private String name;
    private List<Question> questions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    @Override
    public boolean equals(Object o) {
        if(o != null){
            if(((Survey)o).getId() == this.getId()){
                return true;
            }
        }
        return false;
    }
}
