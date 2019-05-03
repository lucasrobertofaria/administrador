package br.com.lucasfaria.administrador.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class ParametrosDAO {
    private SharedPreferences preferences;
    private static final String ARQUIVO_PREFERENCIAS = "ParoquiaPreferences";
    private Context context;
    private SharedPreferences.Editor editor;
    private final static String CHAVE_NOME = "CODIGO_PAROQUIA";

    public ParametrosDAO(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void salvarCodigoParoquia(String tamanhoFonte) {
        editor.putString(CHAVE_NOME, tamanhoFonte);
        editor.commit();
    }

    public String recuperarCodigoParoquia() {
        if (preferences.contains(CHAVE_NOME))
            return preferences.getString(CHAVE_NOME, null);
        else
            return null;
    }
}
