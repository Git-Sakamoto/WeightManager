package com.example.weightmanager;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {
    final int maxDigitsBeforeDecimalPoint=3;
    final int maxDigitsAfterDecimalPoint=1;

    Pattern mPattern = Pattern.compile("(^[1-9]{1})([0-9]{0,"+(maxDigitsBeforeDecimalPoint-1)+"})?(\\.[0-9]{0,"+maxDigitsAfterDecimalPoint+"})?");

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder(dest);
        builder.replace(dstart, dend, source.subSequence(start, end).toString());
        Matcher matcher = mPattern.matcher(builder.toString());
        if(matcher.matches()){
            return null;
        }else{
            return "";
        }
    }

}
