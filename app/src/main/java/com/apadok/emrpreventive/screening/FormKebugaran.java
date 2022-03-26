package com.apadok.emrpreventive.screening;

public class FormKebugaran {
    int id;
    String question;

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }


    public FormKebugaran(int id, String question) {
        this.id = id;
        this.question = question;
    }
}
