package br.com.lucasfaria.administrador.dao;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import br.com.lucasfaria.administrador.config.ReferenceConfig;
import br.com.lucasfaria.comuns.model.Contato;

public class ContatoDAO {


    public static void salvar(Context ctx, Contato contato) {
        ReferenceConfig config = new ReferenceConfig(ctx);
        DatabaseReference contatoReference = config.recuperarReferenciaContatos();

        if (contato.getId() == null || contato.getId().isEmpty()) {
            String key = contatoReference.push().getKey();
            contato.setId(key);
        }
        contatoReference.child(contato.getId()).setValue(contato);

    }
}
