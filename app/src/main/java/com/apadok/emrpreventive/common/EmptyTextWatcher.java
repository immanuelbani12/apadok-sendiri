package com.apadok.emrpreventive.common;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class EmptyTextWatcher implements TextWatcher
{
    public abstract void onEmptyField();

    public abstract void onFilledField();

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (s.toString().trim().length() == 0)
        {
            onEmptyField();
        } else
        {
            onFilledField();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }



    @Override
    public void afterTextChanged(Editable s)
    {

    }

}