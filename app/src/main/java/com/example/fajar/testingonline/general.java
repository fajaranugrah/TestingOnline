package com.example.fajar.testingonline;

/**
 * Created by fajar on 23/01/2017.
 */

public class general {
    public String email_mak;
    public String name;
    public String judul;
    public String tanggal;
    public String id_quiz;
    public String need_score;

    public general() {
    }

    public general(String email_mak, String name, String judul, String tanggal, String id_quiz, String need_score) {
        this.email_mak = email_mak;
        this.name = name;
        this.judul = judul;
        this.tanggal = tanggal;
        this.id_quiz = id_quiz;
        this.need_score = need_score;
    }
}
