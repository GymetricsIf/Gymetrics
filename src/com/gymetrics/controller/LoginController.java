package br.com.gymetrics.controller;

import br.com.gymetrics.dao.UsuarioDAO; import br.com.gymetrics.model.Usuario; import java.sql.SQLException; import java.util.*;

public class LoginController {
    private final UsuarioDAO dao=new UsuarioDAO(); private final Map<String,Integer> tentativas=new HashMap<>();
    public Usuario login(String login,String senha)throws SQLException{if(login==null||senha==null)return null;Usuario u=dao.autenticar(login.trim(),senha);if(u!=null){tentativas.remove(login);return u;}int n=tentativas.merge(login,1,Integer::sum);if(n>=10)dao.bloquear(login);return null;}
}
