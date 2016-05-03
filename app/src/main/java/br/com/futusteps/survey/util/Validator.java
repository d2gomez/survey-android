package br.com.futusteps.survey.util;

import android.text.TextUtils;

import java.util.Calendar;

public class Validator {


    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidCPF(String cpf) {
        if (StringUtils.isEmpty(cpf) || cpf.length() != 11 || isDefaultCPF(cpf))
            return false;

        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) {
            return false;
        }
        return calcVerificationDigit(cpf.substring(0, 9)).equals(cpf.substring(9, 11));
    }

    private static boolean isDefaultCPF(String cpf) {
        return (cpf.equals("11111111111") || cpf.equals("22222222222")
                || cpf.equals("33333333333")
                || cpf.equals("44444444444")
                || cpf.equals("55555555555")
                || cpf.equals("66666666666")
                || cpf.equals("77777777777")
                || cpf.equals("88888888888")
                || cpf.equals("99999999999"));
    }

    private static String calcVerificationDigit(String num) {
        Integer firstDig, secDig;
        int sum = 0, weight = 10;
        for (int i = 0; i < num.length(); i++) {
            sum += Integer.parseInt(num.substring(i, i + 1)) * weight--;
        }

        if (sum % 11 == 0 | sum % 11 == 1) {
            firstDig = 0;
        } else {
            firstDig = 11 - (sum % 11);
        }

        sum = 0;
        weight = 11;
        for (int i = 0; i < num.length(); i++) {
            sum += Integer.parseInt(num.substring(i, i + 1)) * weight--;
        }

        sum += firstDig * 2;
        if (sum % 11 == 0 | sum % 11 == 1) {
            secDig = 0;
        } else {
            secDig = 11 - (sum % 11);
        }

        return firstDig.toString() + secDig.toString();
    }

    public static boolean isValidBirthDate(Calendar bDate){
        if(bDate != null){
            int yearBDate = bDate.get(Calendar.YEAR);
            int yearNow = Calendar.getInstance().get(Calendar.YEAR);
            if(yearBDate < yearNow && yearBDate >= yearNow - 100){
                return true;
            }
        }
        return false;
    }
}
