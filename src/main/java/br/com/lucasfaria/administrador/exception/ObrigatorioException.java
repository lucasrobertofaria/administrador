package br.com.lucasfaria.administrador.exception;

public class ObrigatorioException extends Exception{

    public ObrigatorioException(String campo){
        super("É obrigatório informar " + campo);
    }

}
