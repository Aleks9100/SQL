package com.example.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLog";
    Button AddButton, ClearButton, ReadButton;
    EditText etName, etEmail;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
        dbHelper = new DBHelper(this);
    }

    public void OnClick(View view) {
        ContentValues cv=new ContentValues();
        String name =etName.getText().toString();
        String email = etEmail.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (view.getId())
        {
            case R.id.btnAdd:
                Log.d(LOG_TAG,"--- Insert in mytable: ---");
                cv.put("name",name);
                cv.put("email",email);
                long rowID=db.insert("mytable",null,cv);
                Log.d(LOG_TAG,"row inserted, ID = " + rowID);
                break;
            case R.id.btnRead:
                Cursor c = db.query("mytable",null,null,null,null,null,null);
                if(c.moveToFirst())
                {
                    int idColIndex=c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int emailColIndex = c.getColumnIndex("email");
                    do {
                        Log.d(LOG_TAG,"ID =" +c.getInt(idColIndex)+ ", name = "+ c.getInt(nameColIndex) + ",email = "+c.getInt(emailColIndex));

                    }
                    while (c.moveToNext());
                }
                else Log.d(LOG_TAG,"0 rows");
                c.close();
                break;
            case R.id.btnClear:
                Log.d(LOG_TAG,"--- Clear mytable: ---");
                int clear = db.delete("mytable",null,null);
                Log.d(LOG_TAG,"deleted rows count = " + clear);
                break;
        }
        dbHelper.close();
    }
    class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper(Context context)
        {
            super(context,"myDB",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG,"--- onCreate database ---");
            db.execSQL("create table mytable{" +
                    "id integer primary key autoincrement" +
                    "name text" +
                    "email text"+"};");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}