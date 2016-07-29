package com.gc.editor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.gc.markdown.widget.TabIconView;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabIconView)
    TabIconView tabIconView;

    private EditorFragment editorFragment;
    private PreviewFragment previewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        editorFragment = new EditorFragment();
        previewFragment = new PreviewFragment();

        initTabIcon();
        initViewPager();
    }

    /**
     * 初始化工具栏
     */
    private void initTabIcon() {
        tabIconView.addTab(R.drawable.ic_shortcut_format_list_bulleted, R.id.id_shortcut_list_bulleted, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_list_numbers, R.id.id_shortcut_format_numbers, this);
        tabIconView.addTab(R.drawable.ic_shortcut_insert_link, R.id.id_shortcut_insert_link, this);
        tabIconView.addTab(R.drawable.ic_shortcut_insert_photo, R.id.id_shortcut_insert_photo, this);
        tabIconView.addTab(R.drawable.ic_shortcut_console, R.id.id_shortcut_console, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_bold, R.id.id_shortcut_format_bold, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_italic, R.id.id_shortcut_format_italic, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_1, R.id.id_shortcut_format_header_1, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_2, R.id.id_shortcut_format_header_2, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_3, R.id.id_shortcut_format_header_3, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_quote, R.id.id_shortcut_format_quote, this);
        tabIconView.addTab(R.drawable.ic_shortcut_xml, R.id.id_shortcut_xml, this);
        tabIconView.addTab(R.drawable.ic_shortcut_minus, R.id.id_shortcut_minus, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_strikethrough, R.id.id_shortcut_format_strikethrough, this);
        tabIconView.addTab(R.drawable.ic_shortcut_grid, R.id.id_shortcut_grid, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_4, R.id.id_shortcut_format_header_4, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_5, R.id.id_shortcut_format_header_5, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_6, R.id.id_shortcut_format_header_6, this);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return editorFragment;
                    case 1:
                        return previewFragment;
                    default:
                        return editorFragment;
                }

            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return getString(R.string.editor);
                    case 1:
                        return getString(R.string.preview);
                    default:
                        return getString(R.string.editor);
                }
            }
        });
        //设置切换Fragment显示/隐藏 TabIconView
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        tabIconView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        //发送Event
                        EditText title = (EditText) editorFragment.getActivity().findViewById(R.id.ed_title);
                        EditText content = (EditText) editorFragment.getActivity().findViewById(R.id.content);
                        TextEvent event = new TextEvent(title.getText().toString(),content.getText().toString());
                        EventBus.getDefault().post(event);
                        tabIconView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 点击事件分发
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_shortcut_insert_link://链接
                return;
            case R.id.id_shortcut_insert_photo://图片
                return;
            case R.id.id_shortcut_grid://表格
                return;
            default:
                String type = editorFragment.getInputType(v);
                editorFragment.getPerformEditable().perform(type);
        }
    }
}
