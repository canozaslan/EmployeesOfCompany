
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeOperations {

    private Connection con = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    
    public EmployeeOperations(){
        String url = "jdbc:mysql://" + Database.host + ":"+ Database.port + "/" + Database.db_name + "?useUnicode=true&characterEncoding=utf8";
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException ex){
            System.out.println("Driver bulunamadı....");
        }
        
        try{
            con = DriverManager.getConnection(url, Database.user_name, Database.password);
            System.out.println("Bağlantı başarılı...");
        }
        catch(SQLException ex){
            System.out.println("Bağlantı başarısız....");
            ex.printStackTrace();
        }
    }

    public boolean login(String user_name, String password) {
        String query = "Select * From adminler where username = ? and password = ?";
        
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, user_name);
            preparedStatement.setString(2, password);
            
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next(); 
            
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public ArrayList<Employee> getEmployees(){
        ArrayList<Employee> employees = new ArrayList<Employee>();
        
        try {
            statement = con.createStatement();
            String query = "Select * From calisanlar";
            
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                int id = rs.getInt("id");
                String ad = rs.getString("ad");
                String soyad = rs.getString("soyad");
                String departman = rs.getString("departman");
                String maas = rs.getString("maas");
                
                employees.add(new Employee(id, ad, soyad, departman, maas));
            }
            
            return employees;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void addEmployee(String ad, String soyad, String departman, String maas) {
        
        String query = "Insert into calisanlar (ad, soyad, departman, maas) Values(?,?,?,?)";
        
        try {
            preparedStatement = con.prepareStatement(query);
         
            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, soyad);
            preparedStatement.setString(3, departman);
            preparedStatement.setString(4, maas);
            
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    void updateEmployee(int id, String ad, String soyad, String departman, String maas) {
        String query = "Update calisanlar set ad = ?, soyad = ?, departman = ?, maas = ? where id = ?";
        
        try {
            preparedStatement = con.prepareStatement(query);
            
            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, soyad);
            preparedStatement.setString(3, departman);
            preparedStatement.setString(4, maas);
            preparedStatement.setInt(5, id);
            
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void deleteEmployee(int id) {
        String query = "Delete from calisanlar where id = ?";
        
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
