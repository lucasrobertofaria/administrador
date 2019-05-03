package br.com.lucasfaria.administrador.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.lucasfaria.administrador.R;
import br.com.lucasfaria.administrador.config.ReferenceConfig;
import br.com.lucasfaria.comuns.model.Confissao;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfissoesFragment extends AppBaseFragment {
    private TextInputEditText editConfissoes;
    private ReferenceConfig config;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private ValueEventListener confissoesListener;
    private DatabaseReference confissoesReference;

    private Confissao confissoes = new Confissao();

    public ConfissoesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confissoes, container, false);

        carregarComponentes(view);
        config = new ReferenceConfig(getActivity());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        return view;
    }
    @Override
    public void onStop() {
        if (confissoesListener != null && confissoesReference != null)
            confissoesReference.removeEventListener(confissoesListener);
        super.onStop();
    }

    public void salvar() {
        preencherConfissoes();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceConfig config = new ReferenceConfig(getContext());
        DatabaseReference confissoesReference = config.recuperarReferenciaConfissoes();

        if (confissoes.getId() == null || confissoes.getId().isEmpty()) {
            String key = confissoesReference.push().getKey();
            confissoes.setId(key);

        }
        confissoesReference.child(confissoes.getId()).setValue(confissoes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(getView(), "Salvo", Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private void preencherConfissoes() {
        String infoConfissoes = editConfissoes.getText().toString();

        confissoes.setInfoConfissoes(infoConfissoes.isEmpty() ? null : infoConfissoes);
    }

    private void carregarComponentes(View view) {
        editConfissoes = view.findViewById(R.id.editConfissoes);
        progressBar = view.findViewById(R.id.progressBar);

        fab = view.findViewById(R.id.fab);
    }

}
