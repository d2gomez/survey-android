package br.com.futusteps.survey.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class Masks {

    public static final String CPF_MASK = "###.###.###-##";
    public static final String PHONE_8_DIGITS_MASK = "####-####";
    public static final String PHONE_9_DIGITS_MASK = "#####-####";

    public static TextWatcher insertCPFMask(EditText etCPF) {
        return insert(CPF_MASK, etCPF);
    }


    public static TextWatcher insert(final String mask, final EditText etField) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                if (str.length() > old.length()) {
                    for (char m : mask.toCharArray()) {
                        if (m != '#') {
                            mascara += m;
                            continue;
                        }
                        try {
                            mascara += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                } else {
                    mascara = s.toString();
                }
                isUpdating = true;
                etField.setText(mascara);
                etField.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    public static TextWatcher insertPhoneMask(final EditText etField) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                if (str.length() > old.length()) {
                    String mask = PHONE_8_DIGITS_MASK;
                    if (str.length() > 8) {
                        mask = PHONE_9_DIGITS_MASK;
                    }
                    for (char m : mask.toCharArray()) {
                        if (m != '#') {
                            mascara += m;
                            continue;
                        }
                        try {
                            mascara += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                } else {
                    mascara = s.toString();
                }
                isUpdating = true;
                etField.setText(mascara);
                etField.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "").replaceAll("[ ]", "");
    }

}
