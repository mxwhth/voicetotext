package com.example.administrator.voicetotext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsListener;
import com.alibaba.idst.nls.StageListener;
import com.alibaba.idst.nls.internal.protocol.NlsRequest;
import com.alibaba.idst.nls.internal.protocol.NlsRequestProto;
import com.example.administrator.voicetotext.db.DBManager;
import com.example.administrator.voicetotext.entity.Document;
import com.example.administrator.voicetotext.util.TimeFormatUtil;

public class MainActivity extends AppCompatActivity {
    private boolean isRecognizing = false;
    private boolean isRecording = false;
    private EditText mFullEdit;
    private EditText mResultEdit;
    private Button mCommitButton;
    private Button mCancelButton;
    private Button mRecordButton;
    private Button mSaveButton;
    private Button mDocumentButton;
    private Button mTemplateButton;
    private NlsClient mNlsClient;
    private NlsRequest mNlsRequest;
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //文档记录框
        mFullEdit = (EditText) findViewById(R.id.editText1);
        //识别结果确认框
        mResultEdit = (EditText) findViewById(R.id.editText2);

        //录音按钮
        mRecordButton = (Button) findViewById(R.id.button5);
        //确认识别结果按钮
        mCommitButton = (Button) findViewById(R.id.button4);
        //删除识别结果按钮
        mCancelButton = (Button) findViewById(R.id.button6);

        //保存文档按钮
        mSaveButton = (Button) findViewById(R.id.button1);
        //跳转文档界面按钮
        mDocumentButton = (Button) findViewById(R.id.button3);
        //跳转模板界面按钮
        mTemplateButton = (Button) findViewById(R.id.button2);

        String appkey = "nls-service"; //请设置申请到的Appkey

        mNlsRequest = initNlsRequest();

        mNlsRequest.setApp_key(appkey);    //appkey请从 "快速开始" 帮助页面的appkey列表中获取
        mNlsRequest.setAsr_sc("opu");      //设置语音格式

        /*设置热词相关属性*/
        mNlsRequest.setAsrUserId("userid");
        mNlsRequest.setAsrVocabularyId("vocabid");
        /*设置热词相关属性*/

        NlsClient.openLog(true);
        NlsClient.configure(getApplicationContext()); //全局配置
        mNlsClient = NlsClient.newInstance(this, mRecognizeListener, mStageListener, mNlsRequest);  //实例化NlsClient

        mNlsClient.setMaxRecordTime(60000);  //设置最长语音
        mNlsClient.setMaxStallTime(1000);    //设置最短语音
        mNlsClient.setMinRecordTime(500);    //设置最大录音中断时间
        mNlsClient.setRecordAutoStop(false);  //设置VAD
        mNlsClient.setMinVoiceValueInterval(200); //设置音量回调时长


        //初始化数据库
        dbManager = new DBManager(this);

        initRecord();   //初始化录音按钮
        initCommit();  //初始化确认按钮
        initCancel();  //初始化取消按钮
        initSave();    //初始化保存按钮
        initDocument();  //初始化跳转文档界面按钮
        initTemplate();  //初始化跳转模板界面按钮
    }

    private NlsRequest initNlsRequest() {

        NlsRequestProto proto = new NlsRequestProto(getApplicationContext());
        proto.setApp_user_id("mxwhth"); //设置在应用中的用户名，可选
        return new NlsRequest(proto);

    }

    private void initRecord() {
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording == false) {

                    isRecognizing = true;
                    mResultEdit.setText("正在录音，请稍候！");
                    mNlsRequest.authorize("vuxcM9n7rgDI8k7k", "EzrVPCa62DMtqEvtGtHKaddwSeYeLf"); //请替换为用户申请到的数加认证key和密钥
                    mNlsClient.start();

                    mRecordButton.setText("结束录音");
                } else {
                    isRecognizing = false;
                    mResultEdit.setText("");
                    mNlsClient.stop();

                    mRecordButton.setText("开始录音");
                }
                isRecording = !isRecording;
            }
        });
    }

    private void initCommit() {
        mCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/2/2   将识别结果按照模板添加

                //暂时逻辑为直接添加识别结果
                if (!isRecording) {
                    //避免将提示信息存入
                    if (mFullEdit.getText().toString().trim().equals("")) {
                        mFullEdit.setText(mResultEdit.getText());
                    }else {
                        mFullEdit.setText(mFullEdit.getText() + "\n" + mResultEdit.getText());
                    }
                    mFullEdit.setSelection(mFullEdit.getText().length()); //将文档光标移至最后
                }
            }
        });
    }

    private void initCancel() {
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultEdit.setText("");
            }
        });
    }

    private void initSave(){
        //保存空文档进行提示
        mSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mFullEdit.getText().toString().trim().equals("")) {
                    Log.w("SAVE_ERROR", "保存内容为空");
                    new AlertDialog.Builder(MainActivity.this).setTitle("系统提示：").setMessage("保存文档为空！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).create().show();
                } else {
                    //保存文档
                    final Document document = new Document();
                    document.setText(mFullEdit.getText().toString());
                    String saveTime = TimeFormatUtil.getInstance().getFormatTime();
                    document.setTime(saveTime);
                    LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.document_save,null);
                    final EditText titleEditView = (EditText) linearLayout.findViewById(R.id.titleEditView);

                    //默认文档标题为保存时间
                    titleEditView.setText(saveTime);
                    titleEditView.setSelection(titleEditView.getText().length());

                    //确认保存文档提示框
                    new AlertDialog.Builder(MainActivity.this).setTitle("保存文档").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            document.setTitle(titleEditView.getText().toString());
                            //检查标题 保存文档
                            if (titleEditView.getText().toString().trim().equals("")) {
                                //标题为空进行提示
                                new AlertDialog.Builder(MainActivity.this).setTitle("系统提示：").setMessage("文档标题不能为空！").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //do nothing
                                    }
                                }).create().show();
                            } else {
                                //保存文档
                                dbManager.add(document);
                                //提示保存成功
                                new AlertDialog.Builder(MainActivity.this).setTitle("系统提示：").setMessage("文档保存成功！").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //do nothing
                                    }
                                }).create().show();
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消保存
                        }
                    }).setView(linearLayout).create().show();

                }
            }
        });

    }

    private void initDocument(){
        mDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent documentListIntent = new Intent(MainActivity.this,DocumentListActivity.class);
                startActivity(documentListIntent);
            }
        });
    }

    private void initTemplate(){
        // TODO: 2017/2/3 跳转模板界面
        mTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dbManager.cleanAllTables();
                startActivity(new Intent(MainActivity.this,TemplateListActivity.class));
            }
        });
    }

    private NlsListener mRecognizeListener = new NlsListener() {

        @Override
        public void onRecognizingResult(int status, RecognizedResult result) {
            switch (status) {
                case NlsClient.ErrorCode.SUCCESS:
                    Log.i("asr", "[demo]  callback onRecognizResult " + result.asr_out);
                    //asr_out: {"finish":1,"result":"你说什么？","status":1,"uid":"d5c6ae600701424c8fd2f34be0c3ae1f","version":"4.0"}
                    String[] resultArr = result.asr_out.split(",");
                    String text = resultArr[1].split("\"")[3];
                    mResultEdit.setText(text);
                    mResultEdit.setSelection(mResultEdit.getText().length());
                    break;
                case NlsClient.ErrorCode.RECOGNIZE_ERROR:
                    new AlertDialog.Builder(MainActivity.this).setTitle("系统提示：").setMessage("语音识别失败，请重新录音！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).create().show();
                    break;
                case NlsClient.ErrorCode.RECORDING_ERROR:
                    new AlertDialog.Builder(MainActivity.this).setTitle("系统提示：").setMessage("录音及语音识别异常，请重新录音！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).create().show();
                    break;
                case NlsClient.ErrorCode.NOTHING:
                    new AlertDialog.Builder(MainActivity.this).setTitle("系统提示：").setMessage("语音服务异常，请重新录音！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).create().show();
                    break;
            }
            isRecognizing = false;
        }

    };

    private StageListener mStageListener = new StageListener() {
        @Override
        public void onStartRecognizing(NlsClient recognizer) {
            super.onStartRecognizing(recognizer);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onStopRecognizing(NlsClient recognizer) {
            super.onStopRecognizing(recognizer);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onStartRecording(NlsClient recognizer) {
            super.onStartRecording(recognizer);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onStopRecording(NlsClient recognizer) {
            super.onStopRecording(recognizer);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onVoiceVolume(int volume) {
            super.onVoiceVolume(volume);
        }

    };

}
