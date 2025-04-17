package org.example;


import jakarta.persistence.*;

@Entity
public class Author {

@Id
    private Integer author_id;
    private String full_name;
    private String email;
    private Integer age;
    private String phone;
    private char gender;

    public Author(Integer age, Integer author_id, String email, String full_name, char gender, String phone) {
        this.age = age;
        this.author_id = author_id;
        this.email = email;
        this.full_name = full_name;
        this.gender = gender;
        this.phone = phone;
    }

    public Author() {

    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
