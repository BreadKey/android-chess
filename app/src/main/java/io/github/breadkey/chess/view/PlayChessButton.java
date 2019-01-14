package io.github.breadkey.chess.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import io.github.breadkey.chess.R;

public class PlayChessButton extends AppCompatButton {
    public PlayChessButton(Context context) {
        super(context);
        initButton();
    }

    public PlayChessButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public PlayChessButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton();
    }

    void initButton() {
        setBackgroundResource(R.color.plate);
        setTextColor(getResources().getColorStateList(R.color.play_chess_button_selector));
        setHighlightColor(getResources().getColor(R.color.bread));
    }
}
