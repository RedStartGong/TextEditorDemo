/*
 * Copyright 2016. SHENQINCI(沈钦赐)<946736079@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gc.markdown.editor;

import android.widget.EditText;

import com.gc.markdown.utils.Check;


/**
 * 格式相关操作
 * Created by 沈钦赐 on 16/6/22.
 */
public class PerformEditable  {
    private EditText editText;

    public PerformEditable(EditText editText) {
        Check.CheckNull(editText, "EditText不能为空");
        this.editText = editText;

    }

    public void perform(String type, Object... param) {
        if (type == null) {
            return;
        }
        switch (type) {
            case EditorType.CODE:            //代码行
                performCode();
                break;
            case EditorType.HEADER_1://
                H(1);
                break;
            case EditorType.HEADER_2://
                H(2);
                break;
            case EditorType.HEADER_3://
                H(3);
                break;
            case EditorType.HEADER_4://
                H(4);
                break;
            case EditorType.HEADER_5://
                H(5);
                break;
            case EditorType.HEADER_6://
                H(6);
                break;
            case EditorType.ITALIC://斜体
                performItalic();
                break;
            case EditorType.BOLD://粗体
                performBold();
                break;
            case EditorType.UNORDERED_LIST://无序列表
                performList("* ");
                break;
            case EditorType.ORDERED_LIST://数字列表
                performList("1. ");
                break;
            case EditorType.QUOTE://引用
                performQuote();
                break;
            case EditorType.STRIKETHROUGH://字体删除线
                performStrikeThrough();
                break;
            case EditorType.TABLE://插入表格
                performInsertTable(param);
                break;
            case EditorType.LINK://插入链接
                performInsertLink(param);
                break;
            case EditorType.IMAGE://插入图片
                performInsertImage(param);
                break;
            case EditorType.IMAGE_LINK:
                performInsertImageLink(param);
                break;
            case EditorType.CUTTING_LINE://插入分割线
                performMinus();
                break;
            case EditorType.XML://xml
                performXML();
                break;
        }
    }

    private void performList(String tag) {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(0, selectionStart);
        int line = substring.lastIndexOf(10);

        if (line != -1) {
            selectionStart = line + 1;
        } else {
            selectionStart = 0;
        }
        substring = source.substring(selectionStart, selectionEnd);

        String[] split = substring.split("\n");
        StringBuffer stringBuffer = new StringBuffer();

        if (split != null && split.length > 0)
            for (String s : split) {
                if (s.length() == 0 && stringBuffer.length() != 0) {
                    stringBuffer.append("\n");
                    continue;
                }
                if (!s.trim().startsWith(tag)) {
                    //不是 空行或者已经是序号开头
                    if (stringBuffer.length() > 0) stringBuffer.append("\n");
                    stringBuffer.append(tag).append(s);
                } else {
                    if (stringBuffer.length() > 0) stringBuffer.append("\n");
                    stringBuffer.append(s);
                }
            }

        if (stringBuffer.length() == 0) {
            stringBuffer.append(tag);
        }
        editText.getText().replace(selectionStart, selectionEnd, stringBuffer.toString());
        editText.setSelection(stringBuffer.length() + selectionStart);
        completed();
    }


    /**
     * H.
     *
     * @param level the level
     */
    public final void H(int level) {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        StringBuilder result = new StringBuilder();
        String substring = source.substring(selectionStart, selectionEnd);
        if (!hasNewLine(source, selectionStart))
            result.append("\n");
        for (int i = 0; i < level; i++) {
            result.append("#");
        }
        result.append(" ").append(substring);

        editText.getText().replace(selectionStart, selectionEnd, result.toString());
        editText.setSelection(selectionStart + result.length());
        completed();
    }

    /**
     * 插入表格
     * @param param
     */
    private void performInsertTable(Object... param) {
        int row;
        int column;
        int i;
        if (param == null || param.length < 2) {
            row = 3;
            column = 3;
        } else {
            try {
                row = Integer.parseInt(param[0].toString());
                column = Integer.parseInt(param[1].toString());
            } catch (NumberFormatException e) {
                row = 3;
                column = 3;
            }
        }
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        StringBuilder stringBuilder = new StringBuilder();

        if (!hasNewTwoLine(source, selectionStart)) {
            if (hasNewLine(source, selectionStart))
                stringBuilder.append("\n");
            else
                stringBuilder.append("\n\n");
        }
        stringBuilder.append("|");
        for (i = 0; i < column; i++) {
            stringBuilder.append(" Header |");
        }
        stringBuilder.append("\n|");
        for (i = 0; i < column; i++) {
            stringBuilder.append(":----------:|");
        }
        stringBuilder.append("\n");
        for (int i2 = 0; i2 < row; i2++) {
            stringBuilder.append("|");
            for (i = 0; i < column; i++) {
                stringBuilder.append("            |");
            }
            stringBuilder.append("\n");
        }
        String result = stringBuilder.toString();
        editText.getText().insert(selectionStart, result);
        editText.setSelection(selectionStart + result.length());
        completed();
    }

    /**
     * 插入图片链接
     * 示范格式[![WoWoViewPager](https://github.com/Nightonke/WoWoVie=true)](https://www.github.com)
     * @param param
     */
    private void performInsertImageLink(Object[] param) {
        Object[] temp = param;
        if (param == null){
            param = new Object[]{""};
        }
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();

        String result;
        if (hasNewLine(source,selectionStart)) {
            result = "[![imageLink]("+param[0]+")]"+"("+param[1]+")";
        }
    }
    /**
     * 插入图片
     * @param param
     */
    private void performInsertImage(Object[] param) {
        Object[] temp = param;
        if (param == null){
            param = new Object[]{""};
        }
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();

        String result;
        if (hasNewLine(source, selectionStart)) {
            result = "![image](" + param[0] + ")";
        } else {
            result = "\n" + "![image](" + param[0] + ")";
        }
        editText.getText().insert(selectionStart, result);
        if (temp == null || temp[0].toString().length() == 0)
            editText.setSelection(result.length() + selectionStart - 1);
        else
            editText.setSelection(result.length() + selectionStart);
        completed();
    }

    /**
     * 插入链接
     * Perform insert link.
     * @param param the param
     */
    private void performInsertLink(Object[] param) {
        int selectionStart = editText.getSelectionStart();
        String result;
        if (param == null || param.length == 0) {
            result = "[]()\n";
            editText.getText().insert(selectionStart, result);
            editText.setSelection(selectionStart + 1);
        } else if (param.length == 1) {
            result = "[" + param[0] + "](" + param[0] + ")\n";
            editText.getText().insert(selectionStart, result);
            editText.setSelection(selectionStart + result.length());
        } else {
            result = "[" + param[0] + "](" + param[1] + ")\n";
            editText.getText().insert(selectionStart, result);
            editText.setSelection(selectionStart + result.length());
        }

        completed();
    }

    /**
     * 插入斜体
     */
    private void performItalic() {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result = " *" + substring + "* ";
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart - 2);
        completed();
    }

    /**
     * 插入粗体
     */
    private void performBold() {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result = " **" + substring + "** ";
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart - 3);
        completed();
    }

    /**
     * 插入代码
     */
    private void performCode() {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();

        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);

        String result;
        if (hasNewLine(source, selectionStart))
            result = "```\n" + substring + "\n```\n";
        else
            result = "\n```\n" + substring + "\n```\n";

        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart - 5);
        completed();
    }

    /**
     * 插入XML
     */
    private void performXML() {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);
        String result = " `" + substring + "` ";
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart - 2);
        completed();
    }

    /**
     * 插入分割线
     */
    private void performStrikeThrough() {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);

        String result = " ~~" + substring + "~~ ";
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart - 3);
        completed();
    }

    /**
     * 插入引用
     */
    public void performQuote() {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        String substring = source.substring(selectionStart, selectionEnd);

        String result;
        if (hasNewLine(source, selectionStart)) {
            result = "> " + substring;
        } else {
            result = "\n> " + substring;

        }
        editText.getText().replace(selectionStart, selectionEnd, result);
        editText.setSelection(result.length() + selectionStart);
        completed();
    }

    /**
     * 插入横向
     * Perform minus.
     */
    public void performMinus() {
        String source = editText.getText().toString();
        int selectionStart = editText.getSelectionStart();

        String result;
        if (hasNewLine(source, selectionStart)) {
            //已经单独一行
            result = "-------\n";
        } else {
            //同一行，加上换行
            result = "\n-------\n";
        }
        editText.getText().replace(selectionStart, selectionStart, result);
        editText.setSelection(result.length() + selectionStart);
        completed();
    }

    protected void completed() {

    }


    private boolean hasNewLine(String source, int selectionStart) {
        if (source.isEmpty()) return true;
        source = source.substring(0, selectionStart);
        return source.charAt(source.length() - 1) == 10;
    }

    private boolean hasNewTwoLine(String source, int selectionStart) {
        source = source.substring(0, selectionStart);
        return source.length() >= 2 && source.charAt(source.length() - 1) == 10 && source.charAt(source.length() - 2) == 10;
    }

    private boolean isEmptyLine(String source, int selectionStart) {
        if (source.isEmpty()) return true;
        if (selectionStart == source.length()) return hasNewLine(source, selectionStart);

        String startStr = source.substring(0, selectionStart);
        //最后一行
        return source.charAt(startStr.length() - 1) == 10 && source.charAt(startStr.length()) == 10;

    }


}
