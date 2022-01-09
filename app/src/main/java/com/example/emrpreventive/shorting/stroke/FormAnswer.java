package com.example.emrpreventive.shorting.stroke;

import android.os.Parcel;
import android.os.Parcelable;

class FormAnswer implements Parcelable {

    protected FormAnswer(Parcel in) {
        question = in.readString();
        answer = in.readString();
    }

    public static final Creator<FormAnswer> CREATOR = new Creator<FormAnswer>() {
        @Override
        public FormAnswer createFromParcel(Parcel in) {
            return new FormAnswer(in);
        }

        @Override
        public FormAnswer[] newArray(int size) {
            return new FormAnswer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
    }

    String question;
    String answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

//    public FormAnswer(String question) {
//        this.question = question;
//    }
}