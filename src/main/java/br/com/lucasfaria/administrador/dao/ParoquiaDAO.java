package br.com.lucasfaria.administrador.dao;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import br.com.lucasfaria.administrador.config.ReferenceConfig;
import br.com.lucasfaria.comuns.model.Paroquia;

public class ParoquiaDAO {
    public static void salvar(Context ctx, Paroquia paroquia) {
        ReferenceConfig config = new ReferenceConfig(ctx);
        DatabaseReference contatoReference = config.recuperarReferenciaParoquia();

        if (paroquia.getId() == null || paroquia.getId().isEmpty()) {
            String key = contatoReference.push().getKey();
            paroquia.setId(key);
        }
        contatoReference.child(paroquia.getId()).setValue(paroquia);

    }
}
