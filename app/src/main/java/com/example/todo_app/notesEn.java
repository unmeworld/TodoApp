package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import android.os.Bundle;

@Entity(tableName = "NoteMeTable")
public class notesEn {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String notesTitle;
    private String notesText;

    public notesEn(String notesTitle, String notesText) {
        this.notesText = notesText;
        this.notesTitle = notesTitle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNotesText() {
        return notesText;
    }

    public String getNotesTitle() {
        return notesTitle;
    }

}