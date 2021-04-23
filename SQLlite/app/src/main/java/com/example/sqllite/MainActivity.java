package com.example.sqllite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

//untuk membuat class MainActivity dengan mewarisi AppCombatActivity
public class MainActivity extends AppCompatActivity {
    //membuat array string daftar
    String[] daftar;
    //untuk memanggil fungsi ListView
    ListView ListView01;
    //untuk memanggil fungsi Menu
    Menu menu;
    //untuk memanggil fungsi Cursor
    protected Cursor cursor;
    //untuk memanggil kelas DataHelper
    DataHelper dbcenter;
    //untuk memanggil kelas MainActivity
    public static MainActivity ma;

    @Override
    //untuk membuat fungsi onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //untuk mengatur tampilan awal aplikasi
        setContentView(R.layout.activity_main);
        //untuk memberikan fungsi pada button dengan id button2
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            //untuk membuat fungsi onClick yang berguna untuk berganti halaman atau tampilan
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent inte = new Intent(MainActivity.this, Biodata.class);
                startActivity(inte);
            }
        });
        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }

    //untuk membuat fungsi RefreshList yang digunakan untuk menampilkan data dari database kedalam list view
    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM biodata", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        ListView01 = (ListView) findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new OnItemClickListener() {

            //untuk membuat fungsi onItemClick yang digunakan untuk memberikan option ketika item pada list view di klik
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2]; //.getItemAtPosition(arg2).toString();
                final CharSequence[] dialogitem = {"View Biodata", "Update Biodata", "Delete Biodata"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Option");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            //untuk memberikan option view data
                            case 0:
                                Intent i = new Intent(getApplicationContext(), ViewBiodata.class);
                                i.putExtra("nama", selection);
                                startActivity(i);
                                break;
                            //untuk memberikan option update data
                            case 1:
                                Intent in = new Intent(getApplicationContext(), UpdateBiodata.class);
                                in.putExtra("nama", selection);
                                startActivity(in);
                                break;
                            //untuk memberikan option delete data
                            case 2:
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("delete from biodata where nama = '" + selection + "'");
                                RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();
    }
}
