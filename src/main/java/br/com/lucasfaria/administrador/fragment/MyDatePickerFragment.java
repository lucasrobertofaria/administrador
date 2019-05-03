package br.com.lucasfaria.administrador.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import br.com.lucasfaria.comuns.helper.DateHelper;


public class MyDatePickerFragment extends DialogFragment {

    private TextInputEditText editData;

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    if (editData != null) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, day);

                        editData.setText(DateHelper.formatDateToString(c.getTime()));
                    }
                }
            };


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year,month,day);
    }


    public TextInputEditText getEditData() {
        return editData;
    }

    public void setEditData(TextInputEditText editData) {
        this.editData = editData;
    }
}
