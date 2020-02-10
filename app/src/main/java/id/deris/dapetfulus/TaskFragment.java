package id.deris.dapetfulus;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

public class TaskFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        ImageView banner1 = view.findViewById(R.id.banner1);
        ImageView banner2 = view.findViewById(R.id.banner2);
        TextView scroller = view.findViewById(R.id.scroller);

        scroller.setSelected(true);




        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.drawable.isbanner);
        Bitmap src2 = BitmapFactory.decodeResource(res, R.drawable.pfbanner);


        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        RoundedBitmapDrawable dr2 = RoundedBitmapDrawableFactory.create(res, src2);

        dr.setCornerRadius(50);
        dr2.setCornerRadius(50);

        banner1.setImageDrawable(dr);
        banner2.setImageDrawable(dr2);


        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT );

        return view;
    }



}
