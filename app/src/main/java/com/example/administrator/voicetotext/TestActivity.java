package com.example.administrator.voicetotext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.voicetotext.util.ChineseSegmentationUtil;

public class TestActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button commitButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        init();

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder(editText.getText());
//                StringBuilder sb = new StringBuilder("长春市长春药店");

                textView.setText(Html.fromHtml(sb.toString()));
                textView.setText(sb.append("\n"+ChineseSegmentationUtil.getInstance().getSegmentation(sb.toString())).toString()+"\n");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                editText.setText("");
            }
        });
    }

    //初始化组件
    private void init(){
        textView = (TextView) findViewById(R.id.textViewForSegmentation);
        editText = (EditText) findViewById(R.id.editTextForSegmentation);
        commitButton = (Button) findViewById(R.id.commitForSegmentation);
        cancelButton = (Button) findViewById(R.id.cancelForSegmentation);


    }
}
