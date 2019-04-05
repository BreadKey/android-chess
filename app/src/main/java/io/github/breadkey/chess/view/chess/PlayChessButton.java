package io.github.breadkey.chess.view.chess;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

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
        setTextColor(getResources().getColorStateList(R.color.play_chess_button_text_selector));
        setHighlightColor(getResources().getColor(R.color.bread));
    }
}
