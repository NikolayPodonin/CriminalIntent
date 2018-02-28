package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Ybr on 28.02.2018.
 */

public class ImageDialogFragment extends DialogFragment {
    private static final String IMAGE_PATH = "image_path";

    private ImageView mImageView;
    private String mPath;
    public static ImageDialogFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(IMAGE_PATH, path);

        ImageDialogFragment fragment = new ImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mPath = getArguments().getString(IMAGE_PATH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image, null);

        mImageView = (ImageView) v.findViewById(R.id.dialog_image_image_view);
        Bitmap bitmap = PicturesUtils.getScaledBitmap(mPath, getActivity());
        mImageView.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.image_dialog_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }
}
