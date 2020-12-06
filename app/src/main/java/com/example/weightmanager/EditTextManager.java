package com.example.weightmanager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextManager {
    Context context;

    public EditTextManager(Context context){
        this.context = context;
    }

    public boolean textErrorCheck(EditText...editText){
        final int maxDigitsBeforeDecimalPoint=3;
        final int maxDigitsAfterDecimalPoint=1;
        Pattern mPattern = Pattern.compile("(^[1-9]{1})([0-9]{0,"+(maxDigitsBeforeDecimalPoint-1)+"})?(\\.[0-9]{"+maxDigitsAfterDecimalPoint+"})?");
        Matcher matcher;
        boolean textError = true;

        for(EditText text : editText){
            if(TextUtils.isEmpty(text.getText().toString())){
                text.setError(context.getString(R.string.error_message_is_empty));
                textError = false;
            }else if(isNumber(text.getText().toString())){
                matcher = mPattern.matcher(text.getText().toString());
                if(!matcher.matches()){
                    text.setError(context.getString(R.string.error_message_is_decimal));
                    textError = false;
                }
            }
        }

        return textError;
    }

    private boolean isNumber(String num){
        try {
            Double.parseDouble(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean textIsEmpty(EditText...editText){
        boolean emptyCheck = false;
        for(EditText text : editText){
            if(TextUtils.isEmpty(text.getText().toString())){
                text.setError(context.getString(R.string.error_message_is_empty));
                emptyCheck = true;
            }
        }
        return emptyCheck;
    }

    public boolean checkDecimalInput(EditText...editText){
        final int maxDigitsBeforeDecimalPoint=3;
        final int maxDigitsAfterDecimalPoint=1;
        Pattern mPattern = Pattern.compile("(^[1-9]{1})([0-9]{0,"+(maxDigitsBeforeDecimalPoint-1)+"})?(\\.[0-9]{"+maxDigitsAfterDecimalPoint+"})?");
        Matcher matcher;
        boolean decimalCheck = true;
        for(EditText text : editText){
            matcher = mPattern.matcher(text.getText().toString());
            if(!matcher.matches()){
                decimalCheck = false;
            }
        }
        return decimalCheck;
    }

    public void startEditing(EditText...editText){
        for(EditText text : editText){
            text.setCursorVisible(true);
            text.setFocusable(true);
            text.setFocusableInTouchMode(true);
        }
    }

    public void stopEditing(EditText...editText){
        for(EditText text : editText){
            text.setCursorVisible(false);
            text.setFocusable(false);
            text.setFocusableInTouchMode(false);
        }
    }
}
