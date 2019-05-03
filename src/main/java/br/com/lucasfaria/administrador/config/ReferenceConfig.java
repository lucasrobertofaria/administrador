package br.com.lucasfaria.administrador.config;


import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import br.com.lucasfaria.administrador.dao.ParametrosDAO;

public class ReferenceConfig {
    private static DatabaseReference usuarioLogadoReference = null;
    private static DatabaseReference paroquiaReference = null;
    private static DatabaseReference contatoReference = null;
    private static DatabaseReference confissoesReference = null;
    private static DatabaseReference missasReference = null;

    private final String CODIGO_PAROQUIA;
    private final String noContatos = "CONTATOS";
    private final String noParoquia = "PAROQUIA";
    private final String noMissas = "MISSAS";
    private final String noConfissoes = "CONFISSOES";

    public ReferenceConfig(Context ctx) {
        ParametrosDAO dao = new ParametrosDAO(ctx);
        CODIGO_PAROQUIA = dao.recuperarCodigoParoquia();
    }

    public DatabaseReference recuperarReferenciaContatos() {
        if (contatoReference == null) {
            DatabaseReference firebaseDatabase = FirebaseConfig.getFirebaseDatabase();
            contatoReference = firebaseDatabase.child(CODIGO_PAROQUIA).child(noContatos);
        }
        return contatoReference;

    }

    public DatabaseReference recuperarReferenciaParoquia() {
        if (paroquiaReference == null) {
            DatabaseReference firebaseDatabase = FirebaseConfig.getFirebaseDatabase();
            paroquiaReference = firebaseDatabase.child(CODIGO_PAROQUIA).child(noParoquia);
        }
        return paroquiaReference;

    }

    public DatabaseReference recuperarReferenciaUsuarioLogado() {
        if (usuarioLogadoReference == null) {
            DatabaseReference firebaseDatabase = FirebaseConfig.getFirebaseDatabase();
            usuarioLogadoReference = firebaseDatabase.child("usuarios").child(FirebaseConfig.getIdUsuarioLogado());
        }
        return usuarioLogadoReference;

    }

    public DatabaseReference recuperarReferenciaConfissoes() {
        if (confissoesReference == null) {
            DatabaseReference firebaseDatabase = FirebaseConfig.getFirebaseDatabase();
            confissoesReference = firebaseDatabase.child(CODIGO_PAROQUIA).child(noConfissoes);
        }
        return confissoesReference;
    }

    public DatabaseReference recuperarReferenciaMissas() {
        if (missasReference == null) {
            DatabaseReference firebaseDatabase = FirebaseConfig.getFirebaseDatabase();
            missasReference = firebaseDatabase.child(CODIGO_PAROQUIA).child(noMissas);
        }
        return missasReference;
    }


}
