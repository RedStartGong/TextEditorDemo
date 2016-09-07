package com.gc.editor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.markdown.widget.MarkdownPreviewView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 预览 Fragment
 */
public class PreviewFragment extends Fragment {

    @Bind(R.id.markdownTitle)
    TextView markdownTitle;
    @Bind(R.id.markdownView)
    MarkdownPreviewView markdownPreviewView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        ButterKnife.bind(this, view);
        //注册EventBus
        EventBus.getDefault().register(this);
        return view;
    }

    /**
     * 接收标题跟内容的文本
     * 通过MarkdownPreviewView预览
     *
     * @param textEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getText(TextEvent textEvent) {
        String title = textEvent.getTitle();
        String content = textEvent.getContent();
        markdownTitle.setText(title);
        //设置自定义样式
//        markdownPreviewView.setHtml();
        //格式转换
        markdownPreviewView.parseMarkdown(content, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
