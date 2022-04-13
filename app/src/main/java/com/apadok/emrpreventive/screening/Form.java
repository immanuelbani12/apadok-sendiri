package com.apadok.emrpreventive.screening;

class Form {
    int id;
    int image;
    String question;
    String opt1;
    String opt2;
    String opt3;
    String opt4;
    String hint;

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getQuestion() {
        return question;
    }

    public String getOpt1() {
        return opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public String getOpt4() {
        return opt4;
    }

    public String getHint() {
        return hint;
    }

    public Form(int id, String question, String opt1, String opt2, String opt3, String opt4, int image, String hint) {
        this.id = id;
        this.question = question;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.image = image;
        this.hint = hint;
    }


}