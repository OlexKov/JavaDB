package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneDB  implements AutoCloseable {
    private enum FindColumn{
        id,
        phoneNumber
    }
    private final String tableName = "Phonebook";
    private Connection connection = null;
    private  Connection dbConnect(String user,String password,String url) throws SQLException {
        try{
            return DriverManager.getConnection(url,user,password);
        }
        catch(SQLException ex){
            System.out.println("Error database connection !!!\n"+ ex.getMessage());
            throw ex;
        }
    }
    private void createPhoneTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS "+ tableName +"(" +
                "ID SERIAL PRIMARY KEY, " +
                "Name VARCHAR(100) NOT NULL, " +
                "PhoneNumber VARCHAR(15) NOT NULL, " +
                "Email VARCHAR(100), " +
                "Address VARCHAR(255)" +
                ");";
       try{
            Statement command = connection.createStatement();
            command.execute(createTableSQL);
        }
        catch (SQLException ex){
            System.out.println("Error table creation !!!\n"+ ex.getMessage());
            throw ex;
        }
    };
    private  boolean checkPhoneTable() throws SQLException{
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet resultSet = metaData.getTables(null, null, tableName, null)) {
            return resultSet.next();
        }
        catch (SQLException ex) {
            System.out.println("Error phone table check !!!\n"+ ex.getMessage());
            throw ex;
        }
    }
    private ArrayList<Abonent> getAllAbonentFromResultSet(ResultSet result) throws SQLException{
        ArrayList<Abonent> abonents = new ArrayList<>();
        while (result.next()) {
            abonents.add(getAbonentFromResultSet(result));
        }
        return abonents;
    }
    private Abonent getAbonentFromResultSet(ResultSet result) throws SQLException {
        Abonent abonent = new Abonent();
        abonent.setId(result.getInt("id"));
        abonent.setName(result.getString("name"));
        abonent.setEmail(result.getString("email"));
        abonent.setAddress(result.getString("address"));
        abonent.setPhoneNumber(result.getString("phoneNumber"));
        return abonent;
    }
    private Abonent getAbonentFromColumn(FindColumn column,Object param){
        Abonent abonent = null;
        String baseSQL = "SELECT * FROM " + tableName + " WHERE " + column.name() + " = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(baseSQL)) {
            switch (column) {
                case id:
                    if (param instanceof Integer) {
                        preparedStatement.setInt(1, (Integer) param);
                    } else {
                        throw new IllegalArgumentException("Param must be an Integer for column id");
                    }
                    break;
                case phoneNumber:
                    if (param instanceof String) {
                        preparedStatement.setString(1, (String) param);
                    } else {
                        throw new IllegalArgumentException("Param must be a String for column " + column.name());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported column type: " + column.name());
            }

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    abonent = getAbonentFromResultSet(result);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return abonent;
    }

    public PhoneDB(String user,String password,String url) throws SQLException {
        connection = dbConnect(user, password, url);
        if(!checkPhoneTable())
            createPhoneTable();
    }
    public  int addAbonent(String name,String email,String phone,String address) throws SQLException {
        String insertSQL = "INSERT INTO "+ tableName + "(Name, PhoneNumber, Email, Address) VALUES (?, ?, ?, ?);";
        try{
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, phone);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, address);
                return preparedStatement.executeUpdate();
            }
            catch( SQLException ex){
                System.out.println("Error abonent adding !!!\n"+ ex.getMessage());
                return 0;
            }
        }
    public  int deleteAdonent(int id ) throws SQLException {
        String deleteSQL = "DELETE FROM "+tableName+" WHERE id = ?";
        try{
            PreparedStatement preparedStatement =connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate();
        }
        catch( SQLException ex){
            System.out.println("Error abonent deleting !!!\n"+ ex.getMessage());
            return 0;
        }
    }
    public  ArrayList<Abonent> getAllAbonents( ) throws SQLException{
        String query = "SELECT * FROM " +tableName;
        ArrayList<Abonent> abonents = null ;
        try( PreparedStatement preparedStatement = connection.prepareStatement(query)){
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                abonents=getAllAbonentFromResultSet(resultSet);
            }
        }
        catch( SQLException ex){
            System.out.println("Error  !!!\n"+ ex.getMessage());
            return null;
        }
        return abonents;
    }
    public  ArrayList<Abonent> getAbonentsLikeName(String name ) throws SQLException{
        String query = "SELECT * FROM " +tableName+"  WHERE name LIKE ?";
        ArrayList<Abonent> abonents = null;
        try( PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
               abonents=getAllAbonentFromResultSet(resultSet);
            }
        }
        catch( SQLException ex){
            System.out.println("Error  !!!\n"+ ex.getMessage());
            return null;
        }
        return abonents;
    }
    public  Abonent getAbonentByPhone(String phone ) throws SQLException{
        return getAbonentFromColumn(FindColumn.phoneNumber,phone);
    }
    public  Abonent getAbonentById(int id ) throws SQLException{
        return getAbonentFromColumn(FindColumn.id,id);
    }

    @Override
    public void close() throws Exception {
        if(connection != null){
            connection.close();
        }
    }
}
