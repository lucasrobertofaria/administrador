package br.com.lucasfaria.administrador.listener;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.com.lucasfaria.comuns.model.Contato;

public class ContatoListener implements ValueEventListener {
    private Contato contato;


    public ContatoListener(Contato contato) {
        this.contato = contato;

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot dados : dataSnapshot.getChildren()) {
            contato = dados.getValue(Contato.class);
            contato.setId(dados.getKey());
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
