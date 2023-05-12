package com.example.todo_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static NoteMeViewModel myViewModel;
    FloatingActionButton floatingActionButton;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ImageView delete;
    NewAdapter newAdapter;
    static AlertDialog.Builder builder;
    boolean wantEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#AEAEAE"));

        actionBar.setBackgroundDrawable(colorDrawable);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingAction);
        delete = findViewById(R.id.delete);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listing);
        builder = new AlertDialog.Builder(this);

        newAdapter = new NewAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(newAdapter);

        myViewModel = new ViewModelProvider(this).get(NoteMeViewModel.class);

        myViewModel.getAllNotes().observe(this, new Observer<List<notesEn>>() {
            @Override
            public void onChanged(List<notesEn> notesEns) {
                newAdapter.setNotesData(notesEns);
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (!wantEdit) {
                    if (result.getResultCode() == RESULT_OK) {
                        String nTitle = result.getData().getStringExtra(CreateInstanceActivity.NoteMeTitle);
                        String nText = result.getData().getStringExtra(CreateInstanceActivity.NoteMeDescription);
                        notesEn insertNote = new notesEn(nTitle, nText);
                        myViewModel.insert(insertNote);
                        Toast.makeText(MainActivity.this, "Note Created", Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (result.getResultCode() == RESULT_OK) {
                        int id = result.getData().getIntExtra("NoteMeKey", -1);
                        if (id == -1) {
                            Toast.makeText(MainActivity.this, "Something went wrong while updating!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String nTitle = result.getData().getStringExtra(CreateInstanceActivity.NoteMeTitle);
                        String nText = result.getData().getStringExtra(CreateInstanceActivity.NoteMeDescription);
                        notesEn updateNote = new notesEn(nTitle, nText);
                        updateNote.setId(id);
                        myViewModel.update(updateNote);
                        Toast.makeText(MainActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                        wantEdit = false;
                    }
                }
            }
        });

        newAdapter.setOnItemClickListener(new NewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(notesEn note) {
                wantEdit = true;
                Intent intent = new Intent(MainActivity.this, CreateInstanceActivity.class);
                intent.putExtra("NoteMeKey", note.getId());
                intent.putExtra("NoteMeTitle", note.getNotesTitle());
                intent.putExtra("NoteMeDescription", note.getNotesText());
                intent.putExtra("NoteMeUpdate", "true");
                activityResultLauncher.launch(intent);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, CreateInstanceActivity.class);
                activityResultLauncher.launch(newIntent);
            }
        });
    }

    public static void deleteNote(notesEn note) {
        builder.setMessage("Are you sure ?").setTitle("Delete Note")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myViewModel.deleteNote(note);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}