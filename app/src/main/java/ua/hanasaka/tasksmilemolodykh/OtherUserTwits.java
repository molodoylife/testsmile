package ua.hanasaka.tasksmilemolodykh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import ua.hanasaka.tasksmilemolodykh.adapter.CustomAdapter;
import ua.hanasaka.tasksmilemolodykh.database.DB;

/**
 * activity for visualizing other user's twits
 * <p>
 * Created by Oleksandr Molodykh on 25.03.2017.
 */
public class OtherUserTwits extends AppCompatActivity {
    private Cursor cursor;
    private RecyclerView recyclerView;
    private static Handler h;
    private ProgressDialog pd;
    private static final int STATUS_FINISHED = 2;
    private static final int STATUS_STARTING = 1;
    private long user_id;

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

    /**
     * initializing cursor
     */
    private void initCursor() {
        DB db = DB.getInstance();
        db.open(this);
        cursor = db.getTwits(user_id);
    }

    /**
     * checking if user is in db
     *
     * @return true if user id in db
     */
    private boolean checkIfNameIsInDB() {
        Intent intent = getIntent();
        if (intent.hasExtra("nick")) {
            String nick = intent.getStringExtra("nick");
            DB db = DB.getInstance();
            db.open(this);
            user_id = db.ifUserIsInDB(nick);
            return user_id > 0;
        }
        return false;
    }

    /**
     * setting adapter
     */
    private void setAdapter() {
        CustomAdapter mAdapter = new CustomAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    /**
     * initializing cursor in different thread
     */
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

    /**
     * initializing view components
     */
    private void initComponents() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        h = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
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
                return true;
            }
        });
    }
}
