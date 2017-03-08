package com.example.administrator.voicetotext;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.administrator.voicetotext.db.DBManager;
import com.example.administrator.voicetotext.entity.Document;

import java.util.List;

public class DocumentListActivity extends AppCompatActivity {

    private List<Document> documents;
    private ListView listView; //文档列表
    private ArrayAdapter<String> adapter;
    private FloatingActionButton faButton; //添加文档按钮
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_list_activity);

        listView = (ListView) findViewById(R.id.documentListView);
        faButton = (FloatingActionButton) findViewById(R.id.addDocumentButton);

        dbManager = new DBManager(this);//获取DBManager

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.document_list_item, dbManager.queryAllForCursor(),
                new String[]{"title", "text", "time"},
                new int[]{R.id.list_item_title, R.id.list_item_text, R.id.list_item_time});
        listView.setAdapter(adapter);

        initListItemClick();// 初始化列表点击事件
        initfaB();// 初始化添加按钮点击事件
    }

    public void initfaB(){
        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocumentListActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    public void initListItemClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                String saveTime = item.getString(item.getColumnIndex("time"));
                Document selectedDocument = dbManager.queryByTime(saveTime);
                if (selectedDocument!=null) {
                    //查询成功
                    Intent intent = new Intent(DocumentListActivity.this,DocumentDetailActivity.class);
                    intent.putExtra("document",selectedDocument);
                    startActivity(intent);
                    finish();
                }else {
                    //查询失败
                }
            }
        });
    }

}
