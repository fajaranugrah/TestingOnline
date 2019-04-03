package com.example.fajar.testingonline;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by zul on 07/01/2016.
 */
public class Shareprefered {
    SharedPreferences mSharedPrefences;

    public Shareprefered(Context context) {
        mSharedPrefences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    private void editPreference(String key, String value) {
        mSharedPrefences
                .edit()
                .putString(key, value)
                .apply();
    }

    private void editPreference(String key, int value) {
        mSharedPrefences
                .edit()
                .putInt(key, value)
                .apply();
    }


    private void editPreference(String key, boolean value) {
        mSharedPrefences
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public String getEmail_mas() {
        return mSharedPrefences.getString("email_mak", "null");
    }

    public String setEmail_mas(String email_mas) {

        editPreference("email_mak", email_mas);
        return email_mas;
    }

    public String getId_quiz() {
        return mSharedPrefences.getString("id_quiz", "null");
    }

    public void setId_quiz(String id_quiz) {

        editPreference("id_quiz", id_quiz);

    }

    public String getJuduls() {
        return mSharedPrefences.getString("judul", "null");
    }

    public void setJuduls(String judul) {

        editPreference("judul", judul);

    }

    public String getWaktus() {
        return mSharedPrefences.getString("waktu", "null");
    }

    public void setWaktus(String waktu) {

        editPreference("waktu", waktu);

    }

    public String getNilais() {
        return mSharedPrefences.getString("nilai", "null");
    }

    public void setNilais(String nilai) {

        editPreference("nilai", nilai);

    }

    public String getBanyak_Soal() {
        return mSharedPrefences.getString("banyak_soal", "null");
    }

    public void setBanyak_Soal(String banyak_soal) {

        editPreference("banyak_soal", banyak_soal);

    }

    public String getSoalJudul() {
        return mSharedPrefences.getString("SoalJudul", "null");
    }

    public void setSoalJudul(String SoalJudul) {

        editPreference("SoalJudul", SoalJudul);

    }

    public String getBanyaksoal() {
        return mSharedPrefences.getString("banyaksoal", "null");
    }

    public void setBanyaksoal(String banyaksoal) {

        editPreference("banyaksoal", banyaksoal);

    }

    public String getSumber() {
        return mSharedPrefences.getString("sumber", "null");
    }

    public void setSumber(String sumber) {

        editPreference("sumber", sumber);

    }

    public String getImageRjk() {
        return mSharedPrefences.getString("imagerjk", "null");
    }

    public void setImageRjk(String imagerjk) {

        editPreference("imagerjk", imagerjk);

    }

    public String getId() {
        return mSharedPrefences.getString("id", "null");
    }

    public void setId(String id) {

        editPreference("id", id);

    }

    public String getNeedScore() {
        return mSharedPrefences.getString("needscore", "null");
    }

    public void setNeedScore(String needscore) {

        editPreference("needscore", needscore);

    }
}
