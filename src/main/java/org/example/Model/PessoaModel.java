package org.example.Model;

import java.sql.Connection;
import java.sql.SQLException;

public class PessoaModel {
    private String cpf;
    private String nome;
    private String sobrenome;
    private String endereco;
    private String cidade;

    public PessoaModel(String cpf, String nome, String sobrenome, String endereco, String cidade) {
        this.cpf = cpf;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.endereco = endereco;
        this.cidade = cidade;
    }

    public void inserirNoBanco(Connection conn) throws SQLException {
        //TO-DO IMPLEMENTAR
    }

    public void alterarNoBanco(Connection conn) throws SQLException {
        //TO-DO IMPLEMENTAR
    }

    public void deletarNoBanco(Connection conn) throws SQLException {
        //TO-DO IMPLEMENTAR
    }

}