package cn.edu.hfut.xc.hfut;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MarksLin on 2015/11/6 0006.
 */
public class IntroduceActivity extends BaseActivity {
    private FrameLayout actionBar;
    private EditText editText;
    private TextView editTextNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        findViews();
        setThemeColor(Color.argb(0xff, COLOR_R, COLOR_G, COLOR_B));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextNum.setText(editText.getText().length() + "/12");
            }

            @Override
            public void afterTextChanged(Editable s) {
                int start = editText.getSelectionStart();
                int end = editText.getSelectionEnd();
                if (editText.getText().length() > 12) {
                    Toast.makeText(IntroduceActivity.this, "输入字数超出限制!", Toast.LENGTH_SHORT).show();
                    s.delete(start - 1, end);
                    editText.setText(s);
                    editText.setSelection(editText.getText().length());
                }
            }
        });
    }

    private void findViews() {
        actionBar = (FrameLayout) findViewById(R.id.activity_introduce_action_bar);
        editText = (EditText) findViewById(R.id.activity_introduce_edit_text);
        editTextNum = (TextView) findViewById(R.id.activity_introduce_edit_text_num);
    }

    @Override
    public void setThemeColor(int color) {
        actionBar.setBackgroundColor(color);
        super.setThemeColor(color);
    }

    public void onIntroduceOkClicked(View view) {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("introduce", editText.getText().toString());
        editor.commit();
        finish();
    }
}
