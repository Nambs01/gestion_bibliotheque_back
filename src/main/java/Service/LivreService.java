package Service;

import java.sql.*;
import java.util.List;
import Module.Livre;
import config.Database;
import org.jetbrains.annotations.NotNull;
import config.Util;

public class LivreService {
    private final Statement stmt ;

    public LivreService() throws SQLException, ClassNotFoundException {
        this.stmt = Database.connection().createStatement();
    }


    public List<Livre> getAll(){
        try {
            ResultSet rs = this.stmt.executeQuery("select * from livre");

            return Util.getLivres(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public  List<Livre> get(String param){
        try {
            String sql = "SELECT * FROM livre WHERE id LIKE '%"+param+"%' OR designation LIKE '%"+param+"%'";
            ResultSet rs = this.stmt.executeQuery(sql);

            return Util.getLivres(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public boolean add(@NotNull Livre livre){
        try {
            String sql = "INSERT INTO livre (designation, exemplaire) VALUES ('" + livre.getDesignation() + "', '" + livre.getExemplaire() + "')";
            this.stmt.executeUpdate(sql);
            System.out.println("Add Success !!");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(@NotNull Livre livre){
        try {
            String sql = "UPDATE livre SET designation = '" + livre.getDesignation() + "', exemplaire = " + livre.getExemplaire() + " WHERE id = " + livre.getId();
            this.stmt.executeUpdate(sql);
            System.out.println("Update Success !!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateExemplaire(@NotNull int id, @NotNull int nbr, boolean isPret){
        try {
            String sql = "UPDATE livre SET exemplaire = (exemplaire ";

            if(isPret) sql += "-";
            else sql += "+";

            sql += +nbr+") WHERE id = " + id;

            this.stmt.executeUpdate(sql);
            System.out.println("Update Success !!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id){
        try {
            String sql = "DELETE FROM livre WHERE id = " + id;
            this.stmt.executeUpdate(sql);
            System.out.println("Delete Success !!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
