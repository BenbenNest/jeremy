package com.jeremy.lychee.utils.dialog;

import android.content.Context;
import android.text.InputFilter;
import android.widget.EditText;


public class EditTextDialog extends ConfirmDialog {

    private EditText content;
    public EditTextDialog(Context context) {
        super(context);
        setCustomContentView(getLayoutInflater().inflate(com.jeremy.lychee.R.layout.layout_dialog_edit_text_content, null, false));
    }

    public CharSequence getInput(){
        content = (EditText) findViewById(com.jeremy.lychee.R.id.edit);
        if (content != null) {
            return content.getText().toString().trim();
        }
        return null;
    }

    public void setInput(CharSequence str) {
        content = (EditText) findViewById(com.jeremy.lychee.R.id.edit);
        if (content != null) {
            content.setText(str);
            content.setSelection(str.length());
        }
    }

    public void setHint(String hint) {
        content = (EditText) findViewById(com.jeremy.lychee.R.id.edit);
        if (content != null) {
            content.setHint(hint);
        }
    }
    public void setSingleLine(boolean singleLine) {
        content = (EditText) findViewById(com.jeremy.lychee.R.id.edit);
        if (content != null) {
            content.setSingleLine(singleLine);
        }
    }

    public void setMaxLength(int length) {
        content = (EditText) findViewById(com.jeremy.lychee.R.id.edit);
        if (content != null) {
            InputFilter curFilters[];
            InputFilter.LengthFilter lengthFilter;
            int idx;

            lengthFilter = new InputFilter.LengthFilter(length);

            curFilters = content.getFilters();
            if (curFilters != null) {
                for (idx = 0; idx < curFilters.length; idx++) {
                    if (curFilters[idx] instanceof InputFilter.LengthFilter) {
                        curFilters[idx] = lengthFilter;
                        return;
                    }
                }

                // since the length filter was not part of the list, but
                // there are filters, then add the length filter
                InputFilter newFilters[] = new InputFilter[curFilters.length + 1];
                System.arraycopy(curFilters, 0, newFilters, 0, curFilters.length);
                newFilters[curFilters.length] = lengthFilter;
                content.setFilters(newFilters);
            } else {
                content.setFilters(new InputFilter[]{lengthFilter});
            }
        }
    }

    public String getContent(){
        return content.getText().toString();
    }
    public EditText getContentView(){
        content = (EditText) findViewById(com.jeremy.lychee.R.id.edit);
        return content;
    }
}
