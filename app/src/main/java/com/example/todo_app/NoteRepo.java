package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.app.Application;
import android.os.Bundle;

import java.util.List;

class NoteMeRepo {

    private notesDao myNotesDao;
    private LiveData<List<notesEn>> myNotes;

    NoteMeRepo(Application application) {
        NoteMeDatabase db = NoteMeDatabase.getDatabase(application);
        myNotesDao = db.notesdao();
        myNotes = myNotesDao.getAllNotes();
    }

    LiveData<List<notesEn>> getAllNotes() {
        return myNotes;
    }

    void insert(notesEn noteToBeInserted) {
        NoteMeDatabase.databaseWriteExecutor.execute(() -> {
            myNotesDao.insert(noteToBeInserted);
        });
    }

    void deleteNote(notesEn noteToBeDeleted) {
        NoteMeDatabase.databaseWriteExecutor.execute(() -> {
            myNotesDao.delete(noteToBeDeleted);
        });
    }

    void updateNote(notesEn noteToBeUpdated) {
        NoteMeDatabase.databaseWriteExecutor.execute(() -> {
            myNotesDao.update(noteToBeUpdated);
        });
    }
}


