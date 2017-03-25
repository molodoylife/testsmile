package ua.hanasaka.tasksmilemolodykh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ua.hanasaka.tasksmilemolodykh.adapter.CustomAdapter;
import ua.hanasaka.tasksmilemolodykh.database.DB;

/**
 * Created by Oleksandr Molodykh on 25.03.2017.
 */

public class OtherUserTwits extends AppCompatActivity {
    private Cursor cursor;
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private Handler h;
    ProgressDialog pd;
    private static final int STATUS_FINISHED = 2;
    private static final int STATUS_STARTING = 1;
    private String TAG = "myLogs";
    private  long user_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkIfNameIsInDB()) {
            setContentView(R.layout.content_main);
            initComponents();
            getTwits();
        } else {
            Toast.makeText(this, getResources().getString(R.string.userIsAbsent), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initCursor() {
        DB db = DB.getInstance(this);
        db.open();
        cursor = db.getTwits(user_id);
    }

    private boolean checkIfNameIsInDB() {
        Intent intent = getIntent();
        if (intent.hasExtra("nick")) {
            String nick = intent.getStringExtra("nick");
            DB db = DB.getInstance(this);
            db.open();
            user_id = db.ifUserIsInDB(nick);
            Log.i(TAG, "id="+user_id);
            if (user_id > 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    private void setAdapter() {
        mAdapter = new CustomAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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

    private void initComponents() {
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
                        pd = new ProgressDialog(OtherUserTwits.this);
                        pd.setProgressStyle(R.style.myProgressDialog);
                        pd.setMessage(OtherUserTwits.this.getResources().getString(R.string.wait));
                        pd.setIndeterminate(true);
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();
                        break;
                }
            }
        };
    }
}
