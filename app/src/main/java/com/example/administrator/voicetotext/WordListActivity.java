package com.example.administrator.voicetotext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.voicetotext.dummy.DummyContent;
import com.example.administrator.voicetotext.entity.Directory;

import java.util.List;


public class WordListActivity extends AppCompatActivity {

    private Toolbar toolbar; //功能栏
    private FloatingActionButton fabAddTemplate; //添加词库按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list_activity);

        initToolBar();    //功能栏初始化
        initFAButton();    //FloatingActionButton初始化
        initRecyclerView();

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.frameLayoutForWord, new TemplateDetailFragment())+
//                .commit();


    }

    //功能栏初始化
    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarForWord);
        setSupportActionBar(toolbar);

        toolbar.setTitle("词库");
        //添加菜单栏点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //词典编辑按钮
                    case R.id.action_directory_edit:
                        Toast.makeText(WordListActivity.this, "directory edit!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    //FloatingActionButton初始化
    public void initFAButton() {
        fabAddTemplate = (FloatingActionButton) findViewById(R.id.fabForWord);
        fabAddTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //初始化RecyclerView
    private void initRecyclerView() {
        View recyclerView = findViewById(R.id.wordList);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(Directory.getInstance().getDirectory()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<String> mValues;

        public SimpleItemRecyclerViewAdapter(List<String> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.worditem_fragment, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
//            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position));

            // TODO: 2017/3/7  添加词典删除、编辑、查找
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, TemplateDetailActivity.class);
//                    intent.putExtra(TemplateDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//
//                    context.startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
//            public final TextView mIdView;
            public final TextView mContentView;
            public String mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
//                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.word);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

}
