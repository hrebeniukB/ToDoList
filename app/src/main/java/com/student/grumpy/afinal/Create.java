package com.student.grumpy.afinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import data.ListContract.TaskList;
import data.ListContractHelper;

public class Create extends AppCompatActivity {
    private ListContractHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHelper = new ListContractHelper(this);

        final EditText input = findViewById(R.id.inputText);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().length() != 0) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(TaskList.COLUMN_TASK, input.getText().toString());
                    values.put(TaskList.COLUMN_CHECKED, "0");
                    db.insert(TaskList.TABLE_NAME, null, values);

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Snackbar.make(view, "Remind can't be empty", Snackbar.LENGTH_LONG)
                            .setAction("Error!", null).show();
                }
            }
        });
    }

}
