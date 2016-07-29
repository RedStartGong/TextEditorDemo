package com.gc.editor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gc.markdown.editor.EditorType;
import com.gc.markdown.editor.PerformEditable;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;


public class EditorFragment extends Fragment {
    @Bind(R.id.content)
    EditText content;
    @Bind(R.id.ed_title)
    EditText title;
    private PerformEditable performEditable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor, container, false);
        ButterKnife.bind(this,view);

        performEditable = new PerformEditable(content);
        return view;
    }

    public PerformEditable getPerformEditable() {
        return performEditable;
    }

    public String getInputType(View view){
        switch (view.getId()){
            case R.id.id_shortcut_console://代码行
                return EditorType.NAME_CODE;
            case R.id.id_shortcut_format_header_1://
                return EditorType.NAME_HEADER_1;
            case R.id.id_shortcut_format_header_2://
                return EditorType.NAME_HEADER_2;
            case R.id.id_shortcut_format_header_3://
                return EditorType.NAME_HEADER_3;
            case R.id.id_shortcut_format_header_4://
                return EditorType.NAME_HEADER_4;
            case R.id.id_shortcut_format_header_5://
                return EditorType.NAME_HEADER_5;
            case R.id.id_shortcut_format_header_6://
                return EditorType.NAME_HEADER_6;
            case R.id.id_shortcut_format_italic://斜体
                return EditorType.NAME_ITALIC;
            case R.id.id_shortcut_format_bold://粗体
                return EditorType.NAME_BOLD;
            case R.id.id_shortcut_list_bulleted://列表
                return EditorType.NAME_UNORDERED_LIST;
            case R.id.id_shortcut_format_numbers://数字列表
                return EditorType.NAME_ORDERED_LIST;
            case R.id.id_shortcut_format_quote://引用
                return EditorType.NAME_QUOTE;
            case R.id.id_shortcut_format_strikethrough://字体删除线
                return EditorType.NAME_STRIKETHROUGH;
            case R.id.id_shortcut_xml:
                return EditorType.NAME_XML;
            default:
                return null;
        }

    }

}
