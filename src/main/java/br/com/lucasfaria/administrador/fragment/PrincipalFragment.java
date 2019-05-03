package br.com.lucasfaria.administrador.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.lucasfaria.administrador.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends AppBaseFragment {


    public PrincipalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Principal", "Principal");
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_paroquia_historia, container, false);

        // Set up the tool bar
//        setUpToolbar(view);

        return view;
    }


}
