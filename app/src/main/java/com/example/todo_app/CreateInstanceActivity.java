package com.example.todo_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateInstanceActivity extends AppCompatActivity {

    public static final String NoteMeTitle = "com.example.notes.title";
    public static final String NoteMeDescription = "com.example.notes.description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#AEAEAE"));

        actionBar.setBackgroundDrawable(colorDrawable);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_instance_activity);
        EditText Title = findViewById(R.id.titleNote);
        EditText Description = findViewById(R.id.descriptionNote);
        Button CreateInstance = findViewById(R.id.button);
        Title.requestFocus();

        Intent editIntent = getIntent();
        if (editIntent.hasExtra("NoteMeUpdate")) {
            setTitle("Update");
            Title.setText(editIntent.getStringExtra("NoteMeTitle"));
            Description.setText(editIntent.getStringExtra("NoteMeDescription"));

        } else {
            setTitle("Create");
        }

        CreateInstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nTitle = Title.getText().toString().trim();
                String nText = Description.getText().toString().trim();
                Intent NewIntent = new Intent();

                if (!nText.isEmpty()) {
                    int id = getIntent().getIntExtra("NoteMeKey", -1);
                    if (id != -1) NewIntent.putExtra("NoteMeKey", id);
                    NewIntent.putExtra(NoteMeTitle, nTitle);
                    NewIntent.putExtra(NoteMeDescription, nText);
                    setResult(RESULT_OK, NewIntent);
                    finish();
                } else {
                    Toast.makeText(CreateInstanceActivity.this, "Input field's cannot be empty!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
