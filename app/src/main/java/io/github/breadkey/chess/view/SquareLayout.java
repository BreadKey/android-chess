package io.github.breadkey.chess.view;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.widget.GridLayout;
import android.widget.ImageView;

public class SquareLayout extends ConstraintLayout {
    private char file;
    private int rank;
    private AppCompatButton pieceButton;

    public SquareLayout(Context context, char file, int rank) {
        super(context);
        this.file = file;
        this.rank = rank;
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
    }

    public AppCompatButton getPieceButton() {
        return pieceButton;
    }
}
