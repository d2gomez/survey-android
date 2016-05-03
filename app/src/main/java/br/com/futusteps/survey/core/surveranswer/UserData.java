package br.com.futusteps.survey.core.surveranswer;

import java.io.Serializable;

public class UserData implements Serializable{

    private String cpf;
    private String birthDate;
    private String phone;
    private int phoneType;
    private Float salary;
    private int professionalClass;
    private int profession;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public int getProfessionalClass() {
        return professionalClass;
    }

    public void setProfessionalClass(int professionalClass) {
        this.professionalClass = professionalClass;
    }

    public int getProfession() {
        return profession;
    }

    public void setProfession(int profession) {
        this.profession = profession;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }
}
