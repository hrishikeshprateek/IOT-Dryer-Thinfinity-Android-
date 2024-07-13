package thundersharp.thinkfinity.dryer.users.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import thundersharp.thinkfinity.dryer.R;

public class JobsheetRecord extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_jobsheet_record, container, false);

        return view;
    }
}