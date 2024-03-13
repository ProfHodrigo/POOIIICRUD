package org.example.View;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;

public class PessoaView extends DefaultTableModel {

    public PessoaView(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.addElement(metaData.getColumnName(column));
            }
            setDataVector(new Vector<>(), columnNames);

            while (rs.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    // Se for a primeira coluna, aplica a formatação de CPF
                    if (i == 1) {
                        rowData.addElement(formatCPF(rs.getString(i)));
                    } else {
                        rowData.addElement(rs.getObject(i));
                    }
                }
                addRow(rowData);
            }
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatCPF(String cpf) throws ParseException {
        String formattedCPF = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        return formattedCPF;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // Retorna a classe dos dados da coluna para garantir a renderização correta na tabela
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Define se a célula é editável ou não
        return false;
    }

    // Correção do método getRowCount
    @Override
    public int getRowCount() {
        return dataVector.size();
    }
}