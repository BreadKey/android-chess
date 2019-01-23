package io.github.breadkey.chess.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import io.github.breadkey.chess.R;

public class SquareLayout extends ConstraintLayout {
    private AppCompatButton pieceButton;

    @SuppressLint("ClickableViewAccessibility")
    public SquareLayout(Context context) {
        super(context);
        float dp = context.getResources().getDisplayMetrics().density;
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = (int) (40 * dp);
        layoutParams.height = (int) (40 * dp);
        setLayoutParams(layoutParams);
        pieceButton = new AppCompatButton(context);
        pieceButton.setBackgroundColor(Color.TRANSPARENT);
        addView(pieceButton);
        ConstraintLayout.LayoutParams childLayoutParams = (LayoutParams) pieceButton.getLayoutParams();
        childLayoutParams.width = LayoutParams.MATCH_PARENT;
        childLayoutParams.height = LayoutParams.MATCH_PARENT;
        childLayoutParams.topToTop = 0;
        childLayoutParams.bottomToBottom = 0;
        childLayoutParams.leftToLeft = 0;
        childLayoutParams.rightToRight = 0;
        pieceButton.setLayoutParams(childLayoutParams);
        pieceButton.setOnTouchListener(new OnTouchListener() {
            int moveCount = 0;
            int sensitivity = 5;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moveCount = 0;
                        setBackgroundResource(R.color.strawberry);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (moveCount < sensitivity) {
                            pieceButton.performClick();
                            moveCount = sensitivity;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveCount++;
                        break;
                }
                if (moveCount >= sensitivity) {
                    setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
    }

    public AppCompatButton getPieceButton() {
        return pieceButton;
    }
}
