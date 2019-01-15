package io.github.breadkey.chess.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.github.breadkey.chess.R;

public class BakeryInformation {
    public static void showInformation(ViewGroup parent, String title, String text, InformationActionListener okButtonClickListener, InformationActionListener cancleButtonClickListener) {
        View inflatedView = inflateView(parent);
        setOkButton(parent, inflatedView, okButtonClickListener);
        setCancelButton(parent, inflatedView, cancleButtonClickListener);
        ((TextView) inflatedView.findViewById(R.id.information_title)).setText(title);
        ((TextView) inflatedView.findViewById(R.id.information_text)).setText(text);
        parent.addView(inflatedView);
    }

    private static View inflateView(ViewGroup parent) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.bakery_information, parent, false);
    }

    private static void setOkButton(final ViewGroup parent, final View inflatedView, final InformationActionListener okButtonClickListener) {
        Button okButton = inflatedView.findViewById(R.id.information_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okButtonClickListener.action();
                parent.removeView(inflatedView);
            }
        });
    }

    private static void setCancelButton(final ViewGroup parent, final View inflatedView, final InformationActionListener cancelButtonClickListener) {
        Button cancelButton = inflatedView.findViewById(R.id.information_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButtonClickListener.action();
                parent.removeView(inflatedView);
            }
        });
    }
}
