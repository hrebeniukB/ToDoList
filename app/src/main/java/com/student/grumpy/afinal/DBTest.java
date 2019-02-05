package com.student.grumpy.afinal;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import data.ListContract;
import data.ListContractHelper;

public class DBTest extends AppCompatActivity {
    private ListContractHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbtest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHelper = new ListContractHelper(this);
        RefreshList();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DBTest.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to delete all tasks?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        db.delete(ListContract.TaskList.TABLE_NAME, null, null);
                        RefreshList();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void RefreshList() {
        ListView lView = findViewById(R.id.ListViewTest);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1);
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String query = "SELECT " + ListContract.TaskList.COLUMN_TASK + ", " + ListContract.TaskList.COLUMN_CHECKED + " FROM "
                + ListContract.TaskList.TABLE_NAME;

        Cursor cursor2 = db.rawQuery(query, null);
        adapter.clear();

        adapter.add("TASK : CHECKED");
        while (cursor2.moveToNext()) {
            String result = cursor2.getString(cursor2
                    .getColumnIndex(ListContract.TaskList.COLUMN_TASK)) + " : " + cursor2.getString(cursor2
                    .getColumnIndex(ListContract.TaskList.COLUMN_CHECKED));

            adapter.add(result);

        }
        cursor2.close();
        lView.setAdapter(adapter);
    }
}


