package br.com.lucasfaria.administrador.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.lucasfaria.comuns.helper.Base64Helper;

public class FirebaseConfig {
    private static FirebaseAuth firebaseAuth = null;
    private static DatabaseReference reference = null;
    private static String idUsuarioLogado = null;

    //Recuperar instancia FirebaseAuth
    public static FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }


    //Recuperar instancia DatabaseReference Firebase
    public static DatabaseReference getFirebaseDatabase() {
        if (reference == null)
            reference = FirebaseDatabase.getInstance().getReference();

        return reference;
    }

    public static String getIdUsuarioLogado() {
        if (idUsuarioLogado == null) {
            idUsuarioLogado = Base64Helper.codificarBase64(FirebaseConfig.getFirebaseAuth().getCurrentUser().getEmail());
        }
        return idUsuarioLogado;
    }


}
