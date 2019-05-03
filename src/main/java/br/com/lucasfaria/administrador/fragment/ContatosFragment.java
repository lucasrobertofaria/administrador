package br.com.lucasfaria.administrador.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.lucasfaria.administrador.R;
import br.com.lucasfaria.administrador.config.ReferenceConfig;
import br.com.lucasfaria.comuns.mask.TextMaskWatcher;
import br.com.lucasfaria.comuns.model.Contato;


public class ContatosFragment extends AppBaseFragment {
    private ReferenceConfig config;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private Contato contato = new Contato();
    private ValueEventListener contatoListener;
    private DatabaseReference contatoReference;
    private TextInputEditText editTelefone, editFacebook, editInstagram, editWhatsapp, editSite, editEmail;


    public ContatosFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

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

    private void preencherTela() {
        if (contato.getEmail() != null)
            editEmail.setText(contato.getEmail());
        if (contato.getTelefone() != null)
            editTelefone.setText(contato.getTelefone());
        if (contato.getWhatsapp() != null)
            editWhatsapp.setText(contato.getWhatsapp());
        if (contato.getFacebook() != null)
            editFacebook.setText(contato.getFacebook());
        if (contato.getInstagram() != null)
            editInstagram.setText(contato.getInstagram());
        if (contato.getSite() != null)
            editSite.setText(contato.getSite());
    }

    private void preencherContato() {
        String email = editEmail.getText().toString();
        String telefone = editTelefone.getText().toString();
        String facebook = editFacebook.getText().toString();
        String instagram = editInstagram.getText().toString();
        String whatsapp = editWhatsapp.getText().toString();
        String site = editSite.getText().toString();

        contato.setEmail(email.isEmpty() ? null : email);
        contato.setTelefone(telefone.isEmpty() ? null : telefone);
        contato.setFacebook(facebook.isEmpty() ? null : facebook);
        contato.setInstagram(instagram.isEmpty() ? null : instagram);
        contato.setWhatsapp(whatsapp.isEmpty() ? null : whatsapp);
        contato.setSite(site.isEmpty() ? null : site);
    }

    private void carregarComponentes(View view) {
        editTelefone = view.findViewById(R.id.editTelefone);
        editTelefone.addTextChangedListener(TextMaskWatcher.buildTelefone());

        editWhatsapp = view.findViewById(R.id.editWhatsapp);
        editWhatsapp.addTextChangedListener(TextMaskWatcher.buildTelefone());


        editFacebook = view.findViewById(R.id.editFacebook);
        editInstagram = view.findViewById(R.id.editInstagram);

        editSite = view.findViewById(R.id.editSite);
        editEmail = view.findViewById(R.id.editEmail);
        progressBar = view.findViewById(R.id.progressBar);

        fab = view.findViewById(R.id.fab);
    }

    @Override
    public void onStart() {
        pesquisarContatos();
        super.onStart();
    }

    private void pesquisarContatos() {
        contatoReference = config.recuperarReferenciaContatos();
        progressBar.setVisibility(View.VISIBLE);
        contatoListener = contatoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        contato = dados.getValue(Contato.class);
                        contato.setId(dados.getKey());
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
        if (contatoListener != null && contatoReference != null)
            contatoReference.removeEventListener(contatoListener);
        super.onStop();
    }

    public void salvar() {
        preencherContato();
        progressBar.setVisibility(View.VISIBLE);
        ReferenceConfig config = new ReferenceConfig(getContext());
        DatabaseReference contatoReference = config.recuperarReferenciaContatos();

        if (contato.getId() == null || contato.getId().isEmpty()) {
            String key = contatoReference.push().getKey();
            contato.setId(key);

        }
        contatoReference.child(contato.getId()).setValue(contato).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(getView(), "Salvo", Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
