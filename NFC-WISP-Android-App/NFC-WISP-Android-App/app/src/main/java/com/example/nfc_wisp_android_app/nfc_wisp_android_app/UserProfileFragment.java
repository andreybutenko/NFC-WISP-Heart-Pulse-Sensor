package com.example.nfc_wisp_android_app.nfc_wisp_android_app;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String GENDER = "GENDER";
    private static final String AGE = "AGE";
    private static final String WEIGHT = "WEIGHT";


    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        View curFrag = getActivity().findViewById(R.id.fragment_container);
        curFrag.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        String firstName = args.getString(FIRST_NAME);
        String lastName = args.getString(LAST_NAME);
        String gender = args.getString(GENDER);
        String age = args.getString(AGE);
        String weight = args.getString(WEIGHT);


        TextView fn = (TextView) getActivity().findViewById(R.id.user_first_name);
        fn.setText(firstName);

        TextView ln = (TextView) getActivity().findViewById(R.id.user_last_name);
        ln.setText(lastName);

        TextView gd = (TextView) getActivity().findViewById(R.id.user_gender);
        gd.setText(gender);

        TextView ag = (TextView) getActivity().findViewById(R.id.user_age);
        ag.setText(age);

        TextView wt = (TextView) getActivity().findViewById(R.id.user_weight);
        wt.setText(weight);
    }

}
