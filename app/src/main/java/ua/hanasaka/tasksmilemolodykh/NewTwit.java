package ua.hanasaka.tasksmilemolodykh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * activity for creating new twit
 * <p>
 * Created by Oleksandr Molodykh on 23.03.2017.
 */
public class NewTwit extends AppCompatActivity implements View.OnClickListener {
    private TextView tvCountSymbols;
    private EditText etNewTwit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_twit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initComponents();
    }

    /**
     * setting on click actions
     * add twit or exit
     *
     * @param v clicked view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibClose:
                NewTwit.this.onBackPressed();
                break;
            case R.id.buttonTwit:
                String textTwit = etNewTwit.getText().toString();
                if (textTwit.length() == 0)
                    return;
                Intent intent = new Intent();
                intent.putExtra("twit", textTwit);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    /**
     * initializing view components
     */
    private void initComponents() {
        Button twit = (Button) findViewById(R.id.buttonTwit);
        twit.setOnClickListener(this);
        ImageButton ibClose = (ImageButton) findViewById(R.id.ibClose);
        ibClose.setOnClickListener(this);
        tvCountSymbols = (TextView) findViewById(R.id.countSymbols);
        tvCountSymbols.setText("140");
        etNewTwit = (EditText) findViewById(R.id.etNewTwit);
        etNewTwit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int filled = etNewTwit.getText().toString().length();
                tvCountSymbols.setText(140 - filled + "");
            }
        });
    }
}
