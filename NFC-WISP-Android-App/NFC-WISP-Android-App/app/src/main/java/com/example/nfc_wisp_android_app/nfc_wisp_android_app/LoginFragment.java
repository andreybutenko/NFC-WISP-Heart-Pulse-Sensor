package com.example.nfc_wisp_android_app.nfc_wisp_android_app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment implements OnClickListener {

    private static final String DISPLAY_MESSAGE = "DISPLAY_MESSAGE";
    LoginFragmentListener mListener;
    private DBManager dbManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button signInButton = (Button) view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        Button signUpButton = (Button) view.findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(this);

        // get the reference of the MainActivity's database manager (dbManager)
        dbManager = ((MainActivity) getActivity()).dbManager;

        return view;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.sign_in_button) {
            actionSignIn(view);
        } else if(id == R.id.sign_up_button) {
            toSignUpFragment(view);
        }
    }

    public void actionSignIn(View view) {
        EditText userID = (EditText) getActivity().findViewById(R.id.username);
        String uid = userID.getText().toString();
        if(uid.length() == 0) {
            Toast.makeText(getActivity(), "User name is empty", Toast.LENGTH_SHORT).show();
            return ;
        }

        EditText pw = (EditText) getActivity().findViewById(R.id.password);
        String password = pw.getText().toString();
        if(password.length() == 0) {
            Toast.makeText(getActivity(), "Password is empty", Toast.LENGTH_SHORT).show();
            return ;
        }

        // get user info from database
        Cursor uinfo = dbManager.getUserProfile(uid, password);


        if(!uinfo.moveToFirst()) {
            Toast.makeText(getActivity(), "User not found, or password incorrect", Toast.LENGTH_SHORT).show();
        } else {
            String firstName = uinfo.getString(0);
            String lastName = uinfo.getString(1);
            String gender = uinfo.getString(2);
            int age = uinfo.getInt(3);
            int weight = uinfo.getInt(4);

            mListener.getProfileFromDB(uid, password, firstName, lastName, gender, age, weight);
            hideKeyboard(view);
            toDoMeasurementFragment();
        }
    }

    private void toSignUpFragment(View view) {
        SignUpFragment fragment = new SignUpFragment(); // create new fragment

        // set-up arguments
        Bundle args = new Bundle();
        fragment.setArguments(args);

        // begin fragment trasaction
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment); // replace the transaction with current transaction
        transaction.addToBackStack(null); // add the transaction to the back stack so the user can navigate back

        transaction.commit();
        hideKeyboard(view);
    }


    private void toDoMeasurementFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        DoMeasurementFragment fragment = new DoMeasurementFragment(); // create new fragment

        // set-up arguments
        Bundle args = new Bundle();
        args.putString(DISPLAY_MESSAGE, "Tap tag!");
        fragment.setArguments(args);

        // begin fragment trasaction
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment); // replace the transaction with current transaction
        transaction.addToBackStack(null); // add the transaction to the back stack so the user can navigate back

        transaction.commit();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (LoginFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement LoginFragmentListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface LoginFragmentListener {
        void getProfileFromDB(String uid,
                              String password,
                              String firstName,
                              String lastName,
                              String gender,
                              int age,
                              int weight);
    }
}
