package com.outlay.view.numpad;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byagowi.persiancalendar.R;
import com.github.johnkil.print.PrintView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class NumpadButton extends RelativeLayout {

    @Nullable
    @Bind(R.id.textValue)
    TextView textValue;

    @Nullable
    @Bind(R.id.numpadIcon)
    PrintView numpadIcon;

    public NumpadButton(Context context) {
        super(context);
        init(null);
    }

    public NumpadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NumpadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumpadButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (attrs == null) {
            initTextButton(inflater);
            return;
        }

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.NumpadButton, 0, 0);
        try {
            String icon = ta.getString(R.styleable.NumpadButton_numpadButtonIcon);
            String name = ta.getString(R.styleable.NumpadButton_numpadButtonText);
            if (icon == null) {
                initTextButton(inflater);
                textValue.setText(name);
                textValue.setTextColor(getResources().getColor(R.color.textLightGray));
            } else {
                initIconButton(inflater);
                numpadIcon.setIconText(icon);
                numpadIcon.setColorFilter(getResources().getColor(R.color.textLightGray));
            }

        } finally {
            ta.recycle();
        }
    }

    private View initTextButton(LayoutInflater inflater) {
        View parent = inflater.inflate(R.layout.view_numpad_button_expense, this, true);
        ButterKnife.bind(this, parent);
        return parent;
    }

    private View initIconButton(LayoutInflater inflater) {
        View parent = inflater.inflate(R.layout.view_numpad_icon_button_expense, this, true);
        ButterKnife.bind(this, parent);
        return parent;
    }
}
