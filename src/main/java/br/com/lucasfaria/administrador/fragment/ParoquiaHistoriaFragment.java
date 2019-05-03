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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;

import br.com.lucasfaria.administrador.R;
import br.com.lucasfaria.administrador.config.ReferenceConfig;
import br.com.lucasfaria.comuns.helper.DateHelper;
import br.com.lucasfaria.comuns.model.Paroquia;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParoquiaHistoriaFragment extends AppBaseFragment {
    private ReferenceConfig config;
    private FloatingActionButton fab;
    private Paroquia paroquia = new Paroquia();
    private ValueEventListener paroquiaListener;
    private DatabaseReference paroquiaReference;
    private TextInputEditText editPadroeiro, editDataFundacao, editHistoria;
    private ProgressBar progressBar;


    public ParoquiaHistoriaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paroquia_historia, container, false);

//        setUpToolbar(view);

        editPadroeiro = view.findViewById(R.id.editPadroeiro);
        editDataFundacao = view.findViewById(R.id.editDataFundacao);
        editHistoria = view.findViewById(R.id.editHistoria);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        config = new ReferenceConfig(getActivity());
        editDataFundacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDatePicker();
            }
        });
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        pesquisarParoquia();
        super.onStart();
    }

    private void pesquisarParoquia() {
        paroquiaReference = config.recuperarReferenciaParoquia();
        progressBar.setVisibility(View.VISIBLE);
        paroquiaListener = paroquiaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        paroquia = dados.getValue(Paroquia.class);
                        paroquia.setId(dados.getKey());
                        preencherTela();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        if (paroquiaReference != null && paroquiaListener != null)
            paroquiaReference.removeEventListener(paroquiaListener);
        super.onStop();
    }

    public void salvar() {
        preencherObjeto();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceConfig config = new ReferenceConfig(getContext());
        DatabaseReference contatoReference = config.recuperarReferenciaParoquia();

        if (paroquia.getId() == null || paroquia.getId().isEmpty()) {
            String key = contatoReference.push().getKey();
            paroquia.setId(key);
        }
        contatoReference.child(paroquia.getId()).setValue(paroquia).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                String mensagem = null;
                if (task.isSuccessful())
                    mensagem = "Salvo";
                else
                    mensagem = "Não foi possível salvar";
                Snackbar.make(getView(), mensagem, Snackbar.LENGTH_LONG).show();
            }
        });


    }

    private void preencherTela() {
        editPadroeiro.setText(paroquia.getPadroeiro());
        if (paroquia.getDataFundacao() != null) {
            editDataFundacao.setText(DateHelper.formatDateToString(paroquia.getDataFundacao()));
        }

        editHistoria.setText(paroquia.getHistoria());
    }

    private void preencherObjeto() {
        String padroeiro = editPadroeiro.getText().toString();
        String historia = editHistoria.getText().toString();
        String strDataFundacao = editDataFundacao.getText().toString();

        paroquia.setPadroeiro(padroeiro.isEmpty() ? null : padroeiro);
        paroquia.setHistoria(historia.isEmpty() ? null : historia);

        if (!strDataFundacao.isEmpty()) {
            try {
                paroquia.setDataFundacao(DateHelper.parseStringToDate(strDataFundacao));
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Data de fundação inválida", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void abrirDatePicker() {
        MyDatePickerFragment datePicker = new MyDatePickerFragment();
        datePicker.setEditData(editDataFundacao);
        datePicker.show(getFragmentManager(), getString(R.string.data_fundacao));
    }

}
