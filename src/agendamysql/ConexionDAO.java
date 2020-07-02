package agendamysql;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConexionDAO {

    Connection conexion = null;
    
    List<DatosDTO> listaDatos = new ArrayList<>();

    private void conecta() throws SQLException {
        String user = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/datos?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean inserta(DatosDTO datos) {
        boolean estado = true;
        try {
            conecta();
            PreparedStatement ps = conexion.prepareStatement("insert into info(Nombre,Edad,Sexo) values(?,?,?)");
            ps.setString(1, datos.getNombre());
            ps.setString(2, datos.getEdad());
            ps.setString(3, datos.getSexo());
            ps.execute();
        } catch (SQLException ex) {
            estado = false;
        } finally {
            cerrar();
        }
            return estado;
    }
    
    public boolean cargar(){
            boolean estado = true;
            DatosDTO datos;
            try {
                conecta();
                PreparedStatement ps = conexion.prepareStatement("select * from info");
                ResultSet resultado = ps.executeQuery();
                while(resultado.next()){
                    datos = new DatosDTO();
                    datos.setId(resultado.getInt("id"));
                    datos.setNombre(resultado.getString("Nombre"));
                    datos.setEdad(resultado.getString("Edad"));
                    datos.setSexo(resultado.getString("Sexo"));
                    listaDatos.add(datos);
                
                }
        } catch (SQLException e) {
            estado = false;
        }finally{
            cerrar();
            
            }
            
            return estado;
    }
    
    public boolean actualiza(DatosDTO datos){
       boolean estado = true;
       
       try {
           conecta();
            PreparedStatement ps = conexion.prepareStatement("update info set Nombre = ?, Edad = ?, Sexo = ? where id = ?");
            ps.setString(1, datos.getNombre());
            ps.setString(2, datos.getEdad());
            ps.setString(3, datos.getSexo());
            ps.setInt(4, datos.getId());
            ps.execute();
            
        } catch (SQLException ex) {
            estado = false;
        }finally{
        cerrar();
        }
        return estado;
    }
    
    public boolean eliminar(DatosDTO datos){
    boolean estado = true;
        try {
            conecta(); 
            PreparedStatement ps = conexion.prepareStatement("delete from info where id = ?");
            ps.setInt(1, datos.getId());
            ps.execute();
        } catch (SQLException e) {
        estado = false;
        
        }finally{
        cerrar();
        }
       return estado;
    }
    
    public List<DatosDTO> getDatos(){
      return listaDatos;
    }

    private void cerrar() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
