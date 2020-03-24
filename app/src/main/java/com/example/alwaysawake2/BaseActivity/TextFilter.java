package com.example.alwaysawake2.BaseActivity;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import java.util.regex.Pattern;

public class TextFilter {


    public InputFilter[] setIdFilter(final Context context) {
        InputFilter[] idFilter = new InputFilter[]{
                new InputFilter.LengthFilter(15),
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
                        if (!pattern.matcher(source).matches() && !source.equals("")) {
                            Toast.makeText(context, "영문과 숫자만 입력이 가능합니다", Toast.LENGTH_SHORT).show();
                            return "";
                        } else if (source.length() >= 15) {
                            Toast.makeText(context, "문자수 제한(15)", Toast.LENGTH_SHORT).show();
                            return source;
                        }
                        return null;
                    }
                }
        };
        return idFilter;
    }

    public InputFilter[] setNickFilter(final Context context) {
        InputFilter[] nickFilter = new InputFilter[]{
                new InputFilter.LengthFilter(15),
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        Pattern pattern = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ-가-힣-ㅏ-ㅣ-\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                        if (!pattern.matcher(source).matches() && !source.equals("")) {
                            Toast.makeText(context, "특수기호는 입력이 불가능합니다", Toast.LENGTH_SHORT).show();
                            return "";
                        } else if (source.length() >= 15) {
                            Toast.makeText(context, "문자수 제한(15)", Toast.LENGTH_SHORT).show();
                            return source;
                        }
                        return null;
                    }
                }
        };
        return nickFilter;
    }

    public InputFilter[] setPassFilter(final Context context) {
        InputFilter[] passFilter = new InputFilter[]{
                new InputFilter.LengthFilter(21),
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]+$");
                        if (!pattern.matcher(source).matches() && !source.equals("")) {
                            Toast.makeText(context, "영문, 숫자,(!@.#$%^&*?_~)만 입력 가능합니다", Toast.LENGTH_SHORT).show();
                            return "";
                        } else if (source.length() >= 20) {
                            Toast.makeText(context, "문자수 제한(20)", Toast.LENGTH_SHORT).show();
                            return source;
                        }
                        return null;
                    }
                }
        };
        return passFilter;
    }
}
