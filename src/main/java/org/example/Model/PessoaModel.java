package org.example.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        String sql = "INSERT INTO Pessoas (CPF, Nome, Sobrenome, Endereco, Cidade) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cpf);
        pstmt.setString(2, nome);
        pstmt.setString(3, sobrenome);
        pstmt.setString(4, endereco);
        pstmt.setString(5, cidade);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void alterarNoBanco(Connection conn) throws SQLException {
        String sql = "UPDATE Pessoas SET Nome = ?, Sobrenome = ?, Endereco = ?, Cidade = ? WHERE CPF = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nome);
        pstmt.setString(2, sobrenome);
        pstmt.setString(3, endereco);
        pstmt.setString(4, cidade);
        pstmt.setString(5, cpf);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void deletarNoBanco(Connection conn) throws SQLException {
        String sql = "DELETE FROM Pessoas WHERE Nome = ? AND Sobrenome = ? AND Endereco = ? AND Cidade = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nome);
        pstmt.setString(2, sobrenome);
        pstmt.setString(3, endereco);
        pstmt.setString(4, cidade);
        pstmt.executeUpdate();
        pstmt.close();
    }

}