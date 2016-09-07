package com.gc.editor;

/**
 * 文本事件
 * Created by GongCheng on 2016/7/29.
 */
public class TextEvent {
    private String title;
    private String content;

    public TextEvent(String title,String content) {
        this.content = content;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
