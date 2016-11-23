package com.example.nfc_wisp_android_app.nfc_wisp_android_app;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements OnClickListener, OnItemSelectedListener {
    private static final int USER_ID_LENGTH = 4;
    private static final int PASSWORD_LENGTH = 8;
    private String userGender;

    private DBManager dbManager;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Button register = (Button) view.findViewById(R.id.register_button);
        register.setOnClickListener(this);

        Button backToSignIn = (Button) view.findViewById(R.id.back_button);
        backToSignIn.setOnClickListener(this);


        // Spinner element
        Spinner spinner = (Spinner) view.findViewById(R.id.gender_sign_up);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select");
        categories.add("Male");
        categories.add("Female");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        View curFrag = getActivity().findViewById(R.id.fragment_container);
        curFrag.setVisibility(View.VISIBLE);

        // get the reference of the MainActivity's database manager (dbManager)
        dbManager = ((MainActivity) getActivity()).dbManager;

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        userGender = parent.getItemAtPosition(position).toString();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        userGender = null;
    }

    public void actionRegister(View view) {
        EditText editText = (EditText) getActivity().findViewById(R.id.first_name_sign_up);
        String fname = editText.getText().toString();
        if(fname.length() == 0) {
            Toast.makeText(getActivity(), "First name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        editText = (EditText) getActivity().findViewById(R.id.last_name_sign_up);
        String lname = editText.getText().toString();

        if(lname.length() == 0) {
            Toast.makeText(getActivity(), "Last name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        editText = (EditText) getActivity().findViewById(R.id.username_sign_up);
        String userid = editText.getText().toString();

        if(userid.length() == 0) {
            Toast.makeText(getActivity(), "User name is empty", Toast.LENGTH_SHORT).show();
            return;
        } else if(userid.length() < USER_ID_LENGTH) {
            Toast.makeText(getActivity(), "User name is too short. User name is at least " +
                    USER_ID_LENGTH + " characters", Toast.LENGTH_SHORT).show();
            return;
        }

        editText = (EditText) getActivity().findViewById(R.id.password_sign_up);
        String password = editText.getText().toString();

        if(password.length() == 0) {
            Toast.makeText(getActivity(), "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        } else if(password.length() < PASSWORD_LENGTH) {
            Toast.makeText(getActivity(), "Password is too short. Password is at least " +
                    PASSWORD_LENGTH + " digits", Toast.LENGTH_SHORT).show();
            return;
        }

        editText = (EditText) getActivity().findViewById(R.id.age_sign_up);
        String AGE = editText.getText().toString();
        if(AGE.length() == 0) {
            Toast.makeText(getActivity(), "Age is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        int age = Integer.parseInt(AGE);


        editText = (EditText) getActivity().findViewById(R.id.weight_sign_up);
        String Weight = editText.getText().toString();
        if(Weight.length() == 0) {
            Toast.makeText(getActivity(), "Weight is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        int weight = Integer.parseInt(Weight);


        if(dbManager.checkUserName(userid)) {
            Toast.makeText(getActivity(), "This user name has been used", Toast.LENGTH_SHORT).show();
            return;
        }

        if(userGender == null || userGender.equals("Select")) {
            Toast.makeText(getActivity(), "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        // sign up to database
        dbManager.onSignUp(userid, password, fname, lname, userGender, age, weight);
        Toast.makeText(getActivity(), "Welcome " + fname, Toast.LENGTH_SHORT).show();
        toLoginFragment(view);
    }


    private void toLoginFragment(View view) {
        LoginFragment fragment = new LoginFragment(); // create new fragment
        // set-up arguments
        Bundle args = new Bundle();
        fragment.setArguments(args);

        // begin fragment trasaction
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment); // replace the transaction with current transaction
        transaction.addToBackStack(null); // add the transaction to the back stack so the user can navigate back

        transaction.commit();

        // hide keyboard
        hideKeyboard(view);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.register_button) {
            actionRegister(view);
        } else if(id == R.id.back_button) {
            toLoginFragment(view);
        }
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
