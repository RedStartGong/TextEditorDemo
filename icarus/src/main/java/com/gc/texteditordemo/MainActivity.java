package com.gc.texteditordemo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.gc.texteditordemo.dialog.MyImagePopoverIml;
import com.github.mr5.icarus.Icarus;
import com.github.mr5.icarus.TextViewToolbar;
import com.github.mr5.icarus.Toolbar;
import com.github.mr5.icarus.button.Button;
import com.github.mr5.icarus.button.TextViewButton;
import com.github.mr5.icarus.entity.Options;
import com.github.mr5.icarus.popover.FontScalePopoverImpl;
import com.github.mr5.icarus.popover.HtmlPopoverImpl;
import com.github.mr5.icarus.popover.LinkPopoverImpl;

import java.util.HashMap;

/**
 * Created by GongCheng on 2016/7/11.
 */
public class MainActivity extends AppCompatActivity {
    protected WebView webView;
    protected Icarus icarus;
    private TextViewToolbar textViewToolbar;
    private Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化editor
        init();

    }

    /**
     * 初始化editor
     */
    private void init(){
        webView = (WebView) findViewById(R.id.editor);
        textViewToolbar = new TextViewToolbar();
        options = new Options();
        options.setDefaultImage("file:///android_asset/images/eefung.png");
        options.setPlaceholder("请输入内容...");
        icarus = new Icarus(textViewToolbar,options,webView);
        prepareToolbar(textViewToolbar, icarus);
        icarus.render();
    }

    /**
     * 初始化TextViewToolbar
     * @param textViewToolbar
     * @param icarus
     */
    private Toolbar prepareToolbar(TextViewToolbar textViewToolbar, Icarus icarus) {
        Typeface iconFont = Typeface.createFromAsset(getAssets(),"Simditor.ttf");
        //添加新的字体
        Typeface newIconFont = Typeface.createFromAsset(getAssets(),"iconfont.ttf");
        //初始化普通Button
        HashMap<String,Integer> generalButtons = new HashMap<>();
        generalButtons.put(Button.NAME_BOLD,R.id.button_bold);
        generalButtons.put(Button.NAME_OL, R.id.button_list_ol);
        generalButtons.put(Button.NAME_BLOCKQUOTE, R.id.button_blockquote);
        generalButtons.put(Button.NAME_HR, R.id.button_hr);
        generalButtons.put(Button.NAME_UL, R.id.button_list_ul);
        generalButtons.put(Button.NAME_ALIGN_LEFT, R.id.button_align_left);
        generalButtons.put(Button.NAME_ALIGN_CENTER, R.id.button_align_center);
        generalButtons.put(Button.NAME_ALIGN_RIGHT, R.id.button_align_right);
        generalButtons.put(Button.NAME_ITALIC, R.id.button_italic);
        generalButtons.put(Button.NAME_INDENT, R.id.button_indent);
        generalButtons.put(Button.NAME_OUTDENT, R.id.button_outdent);
        generalButtons.put(Button.NAME_CODE, R.id.button_math);
        generalButtons.put(Button.NAME_UNDERLINE, R.id.button_underline);
        generalButtons.put(Button.NAME_STRIKETHROUGH, R.id.button_strike_through);

        for (String name : generalButtons.keySet()) {
            TextView textView = (TextView) findViewById(generalButtons.get(name));
            if (textView == null){
                continue;
            }
            textView.setTypeface(iconFont);
            TextViewButton button = new TextViewButton(textView,icarus);
            button.setName(name);
            textViewToolbar.addButton(button);
        }
        //添加链接
        TextView linkButtonTextView = (TextView) findViewById(R.id.button_link);
        linkButtonTextView.setTypeface(newIconFont);
        TextViewButton linkButton = new TextViewButton(linkButtonTextView, icarus);
        linkButton.setName(Button.NAME_LINK);
        linkButton.setPopover(new LinkPopoverImpl(linkButtonTextView, icarus));
        textViewToolbar.addButton(linkButton);
        //添加图片
        TextView imageButtonTextView = (TextView) findViewById(R.id.button_image);
        imageButtonTextView.setTypeface(newIconFont);
        TextViewButton imageButton = new TextViewButton(imageButtonTextView,icarus);
        imageButton.setName(Button.NAME_IMAGE);
        imageButton.setPopover(new MyImagePopoverIml(imageButtonTextView, icarus));
        textViewToolbar.addButton(imageButton);
        //添加HTML
        TextView htmlButtonTextView = (TextView) findViewById(R.id.button_html5);
        htmlButtonTextView.setTypeface(iconFont);
        TextViewButton htmlButton = new TextViewButton(htmlButtonTextView,icarus);
        htmlButton.setName(Button.NAME_HTML);
        htmlButton.setPopover(new HtmlPopoverImpl(htmlButtonTextView,icarus));
        textViewToolbar.addButton(htmlButton);
        //修改文字大小
        TextView fontScaleTextView = (TextView) findViewById(R.id.button_font_scale);
        fontScaleTextView.setTypeface(iconFont);
        TextViewButton fontScaleButton = new TextViewButton(fontScaleTextView,icarus);
        fontScaleButton.setName(Button.NAME_FONT_SCALE);
        fontScaleButton.setPopover(new FontScalePopoverImpl(fontScaleTextView,icarus));
        textViewToolbar.addButton(fontScaleButton);

        return textViewToolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            icarus.render();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();

    }
}
