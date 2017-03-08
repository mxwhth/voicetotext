package com.example.administrator.voicetotext;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.voicetotext.db.DBManager;
import com.example.administrator.voicetotext.entity.Document;
import com.example.administrator.voicetotext.util.TimeFormatUtil;

public class DocumentDetailActivity extends AppCompatActivity {

    private Document document;
    private Toolbar toolbar;
    private FloatingActionButton fabDelete;
    private FloatingActionButton fabEdit;
    private TextView documentText;
    private TextView documentTitle;
    private TextView documentTime;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_detail_activity);

        Bundle bundle = getIntent().getExtras();
        document = (Document) bundle.get("document");// 获取传递过来的文档信息

        toolbar = (Toolbar) findViewById(R.id.toolbarForDocument);
        setSupportActionBar(toolbar);

        fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);//删除文档按钮
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);//编辑文档按钮

        documentText = (TextView) findViewById(R.id.documentText);
        documentTitle = (TextView) findViewById(R.id.documentTitle);
        documentTime = (TextView) findViewById(R.id.documentTime);

        dbManager = new DBManager(this);//准备数据库

        initDocumentText();
        initDeleteButton();
        initEditButton();
    }

    public void initDeleteButton() {
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DocumentDetailActivity.this).setTitle("系统提示：").setMessage("删除后无法恢复！").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除当前文档
                        dbManager.delete(document.getTime());
                        new AlertDialog.Builder(DocumentDetailActivity.this).setTitle("系统提示：").setMessage("删除成功！").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        }).create().show();

                        //跳转至文档列表目录
                        startActivity(new Intent(DocumentDetailActivity.this,DocumentListActivity.class));
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消删除
                    }
                }).create().show();
//                Snackbar.make(view, document.getTime(), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    public void initEditButton() {
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentDetailActivity.this, MainActivity.class);
                AlertDialog.Builder dialog = new AlertDialog.Builder(DocumentDetailActivity.this);
                LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.document_edit, null);

                //获取修改文档视图里的控件
                final EditText titleEdit = (EditText) linearLayout.findViewById(R.id.documentEditTitle);
                final EditText textEdit = (EditText) linearLayout.findViewById(R.id.documentEditText);

                titleEdit.setText(document.getTitle());
                textEdit.setText(document.getText());
                dialog.setView(linearLayout).setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //保存修改后的文档
                        if (titleEdit.getText().toString().trim().equals("")) {
                            //标题为空进行提示
                            new AlertDialog.Builder(DocumentDetailActivity.this).setTitle("系统提示：").setMessage("文档标题不能为空！").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            }).create().show();
                        }else {
                            //保存文档
                            document.setTitle(titleEdit.getText().toString());
                            document.setText(textEdit.getText().toString());
                            document.setTime(TimeFormatUtil.getInstance().getFormatTime());
                            dbManager.update(document);
                            new AlertDialog.Builder(DocumentDetailActivity.this).setTitle("系统提示：").setMessage("文档修改成功！").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            }).create().show();

                            //跳转至文档列表目录
                            startActivity(new Intent(DocumentDetailActivity.this,DocumentListActivity.class));
                            finish();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消保存
                    }
                }).create().show();

//                Snackbar.make(view, document.getTime(), Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
            }
        });
    }

    public void initDocumentText() {
        documentTitle.setText(document.getTitle());
        documentText.setText(document.getText());
        documentTime.setText(document.getTime());
    }

}
