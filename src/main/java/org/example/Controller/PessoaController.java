package org.example.Controller;

import org.example.Model.PessoaModel;
import org.example.View.PessoaView;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;

public class PessoaController {

    private Connection conn;
    private PessoaView pessoaView;
    private JFrame frame;
    private JTable tablePopup;

    public PessoaController(Connection conn) {
        this.conn = conn;
        this.pessoaView = buildTableModel();
        this.frame = new JFrame("Opções e Dados");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        this.tablePopup = new JTable(pessoaView);
        JScrollPane scrollPane = new JScrollPane(tablePopup);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton novoDadosButton = new JButton("Inserir Novos Dados");
        novoDadosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    inserirDados();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(novoDadosButton);

        JButton alterarDadosButton = new JButton("Alterar Dados");
        alterarDadosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alterarDados();
            }
        });
        buttonPanel.add(alterarDadosButton);

        JButton deletarDadosButton = new JButton("Deletar Dados");
        deletarDadosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deletarDados();
            }
        });
        buttonPanel.add(deletarDadosButton);

        JButton criarTabelaButton = new JButton("Criar Tabela");
        criarTabelaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                criarTabela();
            }
        });
        buttonPanel.add(criarTabelaButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        this.frame.getContentPane().add(panel);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public PessoaView buildTableModel() {
        try {
            String sql = "SELECT * FROM Pessoas";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            return new PessoaView(rs);
        } catch (SQLException ex) {
            System.out.println("Exception ::" + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public void inserirDados() throws ParseException {
        JPanel panel = new JPanel(new GridLayout(5, 2));

        MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
        JFormattedTextField cpfField = new JFormattedTextField(cpfFormatter);
        cpfField.setColumns(11);

        JTextField nomeField = new JTextField(10);
        JTextField sobrenomeField = new JTextField(10);
        JTextField enderecoField = new JTextField(10);
        JTextField cidadeField = new JTextField(10);

        panel.add(new JLabel("CPF:"));
        panel.add(cpfField);
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Sobrenome:"));
        panel.add(sobrenomeField);
        panel.add(new JLabel("Endereço:"));
        panel.add(enderecoField);
        panel.add(new JLabel("Cidade:"));
        panel.add(cidadeField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Inserir Novos Dados",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String cpf = cpfField.getText().replaceAll("[.-]", "");
                String nome = nomeField.getText();
                String sobrenome = sobrenomeField.getText();
                String endereco = enderecoField.getText();
                String cidade = cidadeField.getText();

                if (cpf.matches("\\d{11}") && !nome.isEmpty() && !sobrenome.isEmpty() && !endereco.isEmpty() && !cidade.isEmpty()) {
                    PessoaModel pessoa = new PessoaModel(cpf, nome, sobrenome, endereco, cidade);
                    pessoa.inserirNoBanco(conn);

                    JOptionPane.showMessageDialog(null, "Dados inseridos na tabela Pessoas com sucesso!");

                    // Atualiza a tabela após a inserção de dados
                    pessoaView = buildTableModel();
                    tablePopup.setModel(pessoaView);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos corretamente.");
                }
            } catch (SQLException ex) {
                System.out.println("Exception ::" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void criarTabela() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS Pessoas ("
                    + "    CPF VARCHAR(14) PRIMARY KEY,"
                    + "    Nome VARCHAR(30),"
                    + "    Sobrenome VARCHAR(100),"
                    + "    Endereco VARCHAR(255),"
                    + "    Cidade VARCHAR(30)"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Tabela criada no Banco de Dados...");
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Exception ::" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void alterarDados() {
        int selectedRow = tablePopup.getSelectedRow();
        if (selectedRow != -1) { // Verifica se alguma linha foi selecionada
            JPanel panel = new JPanel(new GridLayout(5, 2));

            MaskFormatter cpfFormatter = null;
            try {
                cpfFormatter = new MaskFormatter("###.###.###-##");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JFormattedTextField cpfField = new JFormattedTextField(cpfFormatter);
            cpfField.setColumns(11);
            cpfField.setText((String) tablePopup.getValueAt(selectedRow, 0)); // Define o valor do campo CPF com o valor atual da tabela

            JTextField nomeField = new JTextField(10);
            nomeField.setText((String) tablePopup.getValueAt(selectedRow, 1)); // Define o valor do campo Nome com o valor atual da tabela

            JTextField sobrenomeField = new JTextField(10);
            sobrenomeField.setText((String) tablePopup.getValueAt(selectedRow, 2)); // Define o valor do campo Sobrenome com o valor atual da tabela

            JTextField enderecoField = new JTextField(10);
            enderecoField.setText((String) tablePopup.getValueAt(selectedRow, 3)); // Define o valor do campo Endereço com o valor atual da tabela

            JTextField cidadeField = new JTextField(10);
            cidadeField.setText((String) tablePopup.getValueAt(selectedRow, 4)); // Define o valor do campo Cidade com o valor atual da tabela

            panel.add(new JLabel("CPF:"));
            panel.add(cpfField);
            panel.add(new JLabel("Nome:"));
            panel.add(nomeField);
            panel.add(new JLabel("Sobrenome:"));
            panel.add(sobrenomeField);
            panel.add(new JLabel("Endereço:"));
            panel.add(enderecoField);
            panel.add(new JLabel("Cidade:"));
            panel.add(cidadeField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Alterar Dados",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String cpf = cpfField.getText().replaceAll("[.-]", "");
                    String nome = nomeField.getText();
                    String sobrenome = sobrenomeField.getText();
                    String endereco = enderecoField.getText();
                    String cidade = cidadeField.getText();

                    if (cpf.matches("\\d{11}") && !nome.isEmpty() && !sobrenome.isEmpty() && !endereco.isEmpty() && !cidade.isEmpty()) {
                        // Atualiza o registro na tabela do banco de dados
                        PessoaModel pessoa = new PessoaModel(cpf, nome, sobrenome, endereco, cidade);
                        pessoa.alterarNoBanco(conn);

                        JOptionPane.showMessageDialog(null, "Dados alterados na tabela Pessoas com sucesso!");

                        // Atualiza a tabela após a alteração de dados
                        pessoaView = buildTableModel();
                        tablePopup.setModel(pessoaView);
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos corretamente.");
                    }
                } catch (SQLException ex) {
                    System.out.println("Exception ::" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma linha para editar.");
        }
    }

    public void deletarDados() {
        int selectedRow = tablePopup.getSelectedRow();
        if (selectedRow != -1) { // Verifica se alguma linha foi selecionada
            int option = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar esta linha?",
                    "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String nome = (String) tablePopup.getValueAt(selectedRow, 1);
                    String sobrenome = (String) tablePopup.getValueAt(selectedRow, 2);
                    String endereco = (String) tablePopup.getValueAt(selectedRow, 3);
                    String cidade = (String) tablePopup.getValueAt(selectedRow, 4);

                    PessoaModel pessoa = new PessoaModel(null, nome, sobrenome, endereco, cidade);
                    pessoa.deletarNoBanco(conn);

                    JOptionPane.showMessageDialog(null, "Linha deletada com sucesso!");

                    // Atualiza a tabela após a deleção de dados
                    pessoaView = buildTableModel();
                    tablePopup.setModel(pessoaView);
                } catch (SQLException ex) {
                    System.out.println("Exception ::" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma linha para deletar.");
        }
    }
}
