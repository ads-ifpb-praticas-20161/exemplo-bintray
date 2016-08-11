package br.edu.ifpb.praticas.teste;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author diogomoreira.
 */
public class NFDao {

    public Connection connection;


    public NFDao()  {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(getClass().getClassLoader().getResource("config.properties").getFile()));
            Class driverClass = Class.forName(properties.getProperty("db.class"));
            this.connection = DriverManager.getConnection(properties.getProperty("db.url"),
                    properties.getProperty("db.username"), properties.getProperty("db.password"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void persiste(NotaFiscal nf) throws SQLException {
        PreparedStatement statement = null;
        statement = connection.prepareStatement("INSERT INTO notafiscal VALUES (?,?,?)");
        statement.setString(1, nf.getCliente());
        statement.setDouble(2, nf.getValor());
        statement.setDate(3, new java.sql.Date(nf.getData().getTime()));
        statement.execute();
    }

    public void excluir(NotaFiscal nf) throws SQLException {
        PreparedStatement statement = null;
        statement = connection.prepareStatement("DELETE FROM notafiscal WHERE cliente = ?");
        statement.setString(0, nf.getCliente());
        statement.execute();
    }

    public List<NotaFiscal> listar() throws SQLException {
        PreparedStatement statement = null;
        statement = connection.prepareStatement("SELECT * FROM notafiscal");
        ResultSet resultSet = statement.executeQuery();
        List<NotaFiscal> notas = new LinkedList<NotaFiscal>();
        while(resultSet.next()) {
            NotaFiscal notaFiscal = new NotaFiscal();
            notaFiscal.setCliente(resultSet.getString(1));
            notaFiscal.setValor(resultSet.getDouble(2));
            notaFiscal.setData(resultSet.getDate(3));
            notas.add(notaFiscal);
        }
        return notas;
    }

    public NotaFiscal consultar(String cliente) throws SQLException {
        PreparedStatement statement = null;
        statement = connection.prepareStatement("SELECT * FROM notafiscal WHERE cliente = ?");
        statement.setString(1, cliente);
        ResultSet resultSet = statement.executeQuery();
        NotaFiscal notaFiscal = new NotaFiscal();
        while(resultSet.next()) {
            notaFiscal.setCliente(resultSet.getString(1));
            notaFiscal.setValor(resultSet.getDouble(2));
            notaFiscal.setData(resultSet.getDate(3));
        }
        return notaFiscal;
    }

}
