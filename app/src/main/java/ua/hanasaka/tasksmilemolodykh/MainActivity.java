package ua.hanasaka.tasksmilemolodykh;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ua.hanasaka.tasksmilemolodykh.adapter.CustomAdapter;
import ua.hanasaka.tasksmilemolodykh.database.DB;

public class MainActivity extends AppCompatActivity {
    private static final int STATUS_FINISHED = 2;
    private static final int STATUS_UPDATING = 3;
    private static final int STATUS_STARTING = 1;
    private ProgressDialog pd;
    private Cursor cursor;
    private RecyclerView recyclerView;
    private static int USER_ID = 1;
    Handler h;
    CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initComponents();
        getTwits();
    }

    private void setAdapter() {
        mAdapter = new CustomAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initComponents() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewTwit();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case STATUS_FINISHED:
                        setAdapter();
                        pd.dismiss();
                        break;
                    case STATUS_STARTING:
                        pd = new ProgressDialog(MainActivity.this);
                        pd.setProgressStyle(R.style.myProgressDialog);
                        pd.setMessage(MainActivity.this.getResources().getString(R.string.wait));
                        pd.setIndeterminate(true);
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();
                        break;
                    case STATUS_UPDATING:
                        mAdapter.updateRecyclerView(MainActivity.this, cursor, recyclerView);
                        pd.dismiss();
                        break;
                }
            }
        };
    }

    private void initCursor() {
        DB db = DB.getInstance(this);
        db.open();
        cursor = db.getTwits(1L);
    }

    private void getTwits() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                h.sendEmptyMessage(STATUS_STARTING);
                initCursor();
                h.sendEmptyMessage(STATUS_FINISHED);
            }
        });
        t.start();
    }

    private void startNewTwit() {
        Intent intent = new Intent(MainActivity.this, NewTwit.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        h.sendEmptyMessage(STATUS_STARTING);
                        String textTwit = data.getStringExtra("twit");
                        DB db = DB.getInstance(MainActivity.this);
                        db.open();
                        ContentValues cv = new ContentValues();
                        cv.put("body", textTwit);
                        cv.put("user_id", USER_ID);
                        long id = db.addTwit(cv);
                        if (id > 0) {
                            cursor = db.getTwits(1L);
                        }
                        h.sendEmptyMessage(STATUS_UPDATING);
                    }
                });
                t.start();
            }
        }
    }
}
