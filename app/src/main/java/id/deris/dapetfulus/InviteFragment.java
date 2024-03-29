package id.deris.dapetfulus;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.ybq.android.spinkit.SpinKitView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;

public class InviteFragment extends Fragment {

    private CardView tabInvite1, tabInvite2, linkLayout, refferalLayout;
    private TextView tabInvite1text, tabInvite2text, refferalCodeOwner, linkRefferal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite, container, false);

        refferalCodeOwner = view.findViewById(R.id.refferal_code_owner);
        linkRefferal = view.findViewById(R.id.link_refferal);
        tabInvite1 = view.findViewById(R.id.tabInvite1);
        tabInvite2 = view.findViewById(R.id.tabInvite2);
        linkLayout = view.findViewById(R.id.link_layout);
        refferalLayout = view.findViewById(R.id.refferal_layout);
        tabInvite1text = view.findViewById(R.id.tabInvite1Text);
        tabInvite2text = view.findViewById(R.id.tabInvite2Text);
        ImageButton menuBtn = view.findViewById(R.id.btn_menu);
        ImageButton refresh = view.findViewById(R.id.btn_refresh);
        final TextView invited = view.findViewById(R.id.tv_invited);
        final SpinKitView spinKit =  view.findViewById(R.id.spinkit_invited);
        final CardView whatsappShare = view.findViewById(R.id.whatsapp_share);
        final CardView otherShare = view.findViewById(R.id.other_share);
        final CardView whatsappShare2 = view.findViewById(R.id.whatsapp_share2);
        final CardView otherShare2 = view.findViewById(R.id.other_share2);

        final String refferalCodeOwners = Prefs.getString("refferal_code_owner", "belum ada data");
        final String linkRefferals = Prefs.getString("link_refferal", "belum ada data");


        final String currentInvited = Prefs.getString("invited", "0");
        invited.setText(currentInvited);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });

        refferalCodeOwner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager) Objects.requireNonNull(getContext()).getSystemService(Context.CLIPBOARD_SERVICE);
                assert cm != null;
                cm.setText(refferalCodeOwner.getText());
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        linkRefferal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager) Objects.requireNonNull(getContext()).getSystemService(Context.CLIPBOARD_SERVICE);
                assert cm != null;
                cm.setText(linkRefferal.getText());
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        tabInvite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabInvite1.setCardBackgroundColor(Color.WHITE);
                tabInvite2.setCardBackgroundColor(getResources().getColor(R.color.tabTransparent));
                tabInvite1text.setTextColor(getResources().getColor(R.color.amber));
                tabInvite2text.setTextColor(Color.BLACK);
                linkLayout.setVisibility(View.VISIBLE);
                refferalLayout.setVisibility(View.GONE);
            }
        });

        tabInvite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabInvite2.setCardBackgroundColor(Color.WHITE);
                tabInvite1.setCardBackgroundColor(getResources().getColor(R.color.tabTransparent));
                tabInvite2text.setTextColor(getResources().getColor(R.color.amber));
                tabInvite1text.setTextColor(Color.BLACK);
                refferalLayout.setVisibility(View.VISIBLE);
                linkLayout.setVisibility(View.GONE);

                whatsappShare2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharetoWhatsapp(null, refferalCodeOwners);
                    }
                });

                otherShare2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharetoOther(null, refferalCodeOwners);
                    }
                });
            }
        });



        SpannableString content = new SpannableString(refferalCodeOwners);
        content.setSpan(new UnderlineSpan(), 0, refferalCodeOwners.length(), 0);
        SpannableString content2 = new SpannableString(linkRefferals);
        content2.setSpan(new UnderlineSpan(), 0, linkRefferals.length(), 0);

        refferalCodeOwner.setText(refferalCodeOwners);
        linkRefferal.setText(linkRefferals);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invited.setVisibility(View.GONE);
                spinKit.setVisibility(View.VISIBLE);

                ReadRefferal readRefferal = new ReadRefferal();
                readRefferal.execute();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper dbHelper = DBHelper.getInstance(getContext());
                        List<WithdrawModel> payments = dbHelper.getAllPayment();
                        Log.d("data payment", payments.toString());
                        spinKit.setVisibility(View.GONE);

                        String newInvited = Prefs.getString("invited", currentInvited);
                        invited.setText(newInvited);
                        invited.setVisibility(View.VISIBLE);
                    }
                }, 5000);
            }
        });

        whatsappShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sharetoWhatsapp(linkRefferals, null);
            }
        });

        otherShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sharetoOther(linkRefferals, null);
            }
        });

        return view;
    }

    private void sharetoWhatsapp(String linkRefferals, String refferalCodeOwners) {

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        if (refferalCodeOwners == null) {
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Ayo bergabung bersama saya di Aplikasi Dapet Fulus dan dapatkan penghasilan lebih.\n\nGunakan Link "+ linkRefferals);
        }
        else {
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Ayo bergabung bersama saya di Aplikasi Dapet Fulus dan dapatkan penghasilan lebih.\n\nGunakan kode Refferal "+refferalCodeOwners);
        }
        try {
            Objects.requireNonNull(getActivity()).startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
        }
    }

    private void sharetoOther(String linkRefferals, String refferalCodeOwners) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent .setType("text/plain");
        if (refferalCodeOwners == null) {
            txtIntent.putExtra(Intent.EXTRA_TEXT, "Ayo bergabung bersama saya di Aplikasi Dapet Fulus dan dapatkan penghasilan lebih.\n\nGunakan Link "+ linkRefferals);
        }
        else {
            txtIntent.putExtra(Intent.EXTRA_TEXT, "Ayo bergabung bersama saya di Aplikasi Dapet Fulus dan dapatkan penghasilan lebih.\n\nGunakan kode Refferal "+refferalCodeOwners);
        }
        startActivity(Intent.createChooser(txtIntent ,"Share"));
    }

}
