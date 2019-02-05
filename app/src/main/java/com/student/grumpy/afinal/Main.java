package com.student.grumpy.afinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import data.ListContract;
import data.ListContract.TaskList;
import data.ListContractHelper;

public class Main extends AppCompatActivity {

    private ListContractHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHelper = new ListContractHelper(this);//
        UpdateList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Create.class);

                startActivityForResult(intent, 1);

            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure to delete all tasks?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    db.delete(ListContract.TaskList.TABLE_NAME, null, null);
                    UpdateList();
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


            return true;
        }
        if (id == R.id.action_test) {
            Intent intent = new Intent(Main.this, DBTest.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public static boolean CheckConv(String arg) {
        boolean val = false;
        if (arg.equals("1")) val = true;
        return val;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UpdateList();
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateList();

    }

    public void UpdateList() {
        boolean checks[];
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(Main.this, android.R.layout.simple_list_item_multiple_choice);
        final ListView lView = findViewById(R.id.ListView);
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String checkStat = "1";
                if (!lView.isItemChecked(position)) checkStat = "0";

                ListContractHelper.ItemStatusUpdate(db, lView.getAdapter().getItem(position).toString(), checkStat);
                UpdateList();

            }
        });
        lView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int fPosition = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to delete this task?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        ListContractHelper.ItemDelete(db, lView.getAdapter().getItem(fPosition).toString());
                        UpdateList();
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
                return false;
            }
        });


        String query = "SELECT " + TaskList.COLUMN_TASK + ", " + TaskList.COLUMN_CHECKED + " FROM " + TaskList.TABLE_NAME + " ORDER BY " + TaskList.COLUMN_CHECKED;
        Cursor cursor2 = db.rawQuery(query, null);
        adapter.clear();
        checks = new boolean[cursor2.getCount()];
        while (cursor2.moveToNext()) {
            String name = cursor2.getString(cursor2
                    .getColumnIndex(TaskList.COLUMN_TASK));
            String isChecked = cursor2.getString(cursor2
                    .getColumnIndex(TaskList.COLUMN_CHECKED));

            adapter.add(name);

            checks[adapter.getCount() - 1] = CheckConv(isChecked);
            Log.d("dBug", adapter.getCount() - 1 + ". UpdateList: isChecked = " + isChecked + " - " + CheckConv(isChecked) + "- real: "
                    + lView.getCheckedItemPositions());
        }
        cursor2.close();
        lView.setAdapter(adapter);
        for (int i = 0; i < checks.length; i++) {
            lView.setItemChecked(i, checks[i]);
            Log.d("dBug", "checks[" + i + "] = " + checks[i]);


        }
    }

}
