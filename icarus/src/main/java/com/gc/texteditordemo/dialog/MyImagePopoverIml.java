package com.gc.texteditordemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.texteditordemo.R;
import com.github.mr5.icarus.Icarus;
import com.github.mr5.icarus.entity.Image;
import com.github.mr5.icarus.popover.Popover;
import com.google.gson.Gson;

/**
 * 自定义添加图片的dialog
 * Created by GongCheng on 2016/7/12.
 */
public class MyImagePopoverIml implements Popover{
    private Context context;
    private Icarus icarus;
    private TextView textView;
    private Dialog imageDialog;
    private EditText etImageSrc;
    private EditText etImageName;
    private Button btOk;
    private Button btCancel;
    private Handler mainLooperHandler;

    public MyImagePopoverIml(TextView textView,Icarus icarus){
        this.textView = textView;
        this.icarus = icarus;
        context = textView.getContext();
        mainLooperHandler = new Handler(Looper.getMainLooper());
        initDialog();
    }

    /**
     * 初始化dialog
     */
    private void initDialog() {
        imageDialog = new Dialog(context);
        imageDialog.setCanceledOnTouchOutside(false);
        //设置默认无标题
        imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = View.inflate(context, R.layout.dialog_image,null);
        imageDialog.setContentView(dialogView);
        etImageSrc = (EditText) dialogView.findViewById(R.id.image_src);
        etImageName = (EditText) dialogView.findViewById(R.id.image_name);
        btOk = (Button) dialogView.findViewById(R.id.bt_ok);
        btCancel = (Button) dialogView.findViewById(R.id.bt_cancel);
    }

    @Override
    public void show(String params, final String callbackName) {
        Gson gson = new Gson();
        final Image image = gson.fromJson(params,Image.class);
        etImageSrc.setText(image.getSrc());
        etImageName.setText(image.getAlt());
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLooperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageDialog.dismiss();
                        image.setSrc(etImageSrc.getText().toString());
                        image.setAlt(etImageName.getText().toString());
                        icarus.jsCallback(callbackName,image,Image.class);
                    }
                });
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLooperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageDialog.dismiss();
                    }
                });
            }
        });
        imageDialog.show();
    }

    @Override
    public void hide() {
        imageDialog.dismiss();
    }
}
