package com.gc.editor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gc.markdown.editor.EditorType;
import com.gc.markdown.editor.PerformEditable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 编辑 Fragment
 */
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
                return EditorType.CODE;
            case R.id.id_shortcut_format_header_1://标题1
                return EditorType.HEADER_1;
            case R.id.id_shortcut_format_header_2://标题2
                return EditorType.HEADER_2;
            case R.id.id_shortcut_format_header_3://标题3
                return EditorType.HEADER_3;
            case R.id.id_shortcut_format_header_4://标题4
                return EditorType.HEADER_4;
            case R.id.id_shortcut_format_header_5://标题5
                return EditorType.HEADER_5;
            case R.id.id_shortcut_format_header_6://标题6
                return EditorType.HEADER_6;
            case R.id.id_shortcut_format_italic://斜体
                return EditorType.ITALIC;
            case R.id.id_shortcut_format_bold://粗体
                return EditorType.BOLD;
            case R.id.id_shortcut_minus:
                return EditorType.CUTTING_LINE;//下划线
            case R.id.id_shortcut_list_bulleted://列表
                return EditorType.UNORDERED_LIST;
            case R.id.id_shortcut_format_numbers://数字列表
                return EditorType.ORDERED_LIST;
            case R.id.id_shortcut_format_quote://引用
                return EditorType.QUOTE;
            case R.id.id_shortcut_format_strikethrough://字体删除线
                return EditorType.STRIKETHROUGH;
            case R.id.id_shortcut_xml://XML
                return EditorType.XML;
            default:
                return null;
        }

    }

}
