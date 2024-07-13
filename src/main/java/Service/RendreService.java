package Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Module.Rendre;
import config.Database;
import config.Util;
import org.jetbrains.annotations.NotNull;
import Service.PretService;
import Module.Pret;
import Module.Livre;

public class RendreService {
    private final Connection connection;

    public RendreService() throws SQLException, ClassNotFoundException {
        this.connection = Database.connection();
    }

    public List<Rendre> getAll(){
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM rendre");

            List<Rendre> rendreList = new ArrayList<Rendre>();

            while (rs.next()) {
                int idRendu = rs.getInt("idRendu");
                int idMembre = rs.getInt("idMembre");
                int idPret = rs.getInt("idPret");
                java.sql.Timestamp  dateRendu = rs.getTimestamp("dateRendu");
                Rendre rendre = new Rendre(idRendu, idMembre, idPret, dateRendu);
                rendreList.add(rendre);
            }
            return rendreList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public boolean add(@NotNull Rendre rendre){
        try {
            String sql = "INSERT INTO rendre (idMembre, idPret, dateRendu) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1, rendre.getIdMembre());
            preparedStatement.setInt(2, rendre.getIdPret());
            preparedStatement.setTimestamp(3, rendre.getDateRendu());
            int rowInserted = preparedStatement.executeUpdate();

            String idPret = Integer.toString(rendre.getIdPret());
            Pret pret = new PretService().getById(idPret);
            new PretService().isRendu(idPret);

            new LivreService().updateExemplaire(pret.getIdLivre1(), pret.getNbrLivre1(), false);

            String idlivre = pret.getIdLivre2();
            if(idlivre != null) new LivreService().updateExemplaire(Integer.parseInt(idlivre), Integer.parseInt(pret.getNbrLivre2()), false);


            System.out.println("Add Success !!");
            return rowInserted > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(@NotNull Rendre rendre){
        try {
            String sql = "UPDATE rendre " +
                    "SET idMembre = ?, " +
                        "idPret = ?, " +
                        "dateRendu = ? " +
                    "WHERE idRendu = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1, rendre.getIdMembre());
            preparedStatement.setInt(2, rendre.getIdPret());
            preparedStatement.setTimestamp(3, rendre.getDateRendu());
            preparedStatement.setInt(4, rendre.getIdRendu());
            int rowsUpdated =preparedStatement.executeUpdate();
            System.out.println(rowsUpdated + " row(s) updated.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(int idRendu){
        try {
            String sql = "DELETE FROM rendre WHERE idRendu = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1, idRendu);
            preparedStatement.executeUpdate();
            System.out.println("Delete Success !!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Rendre> getHistoricByPers(String idPers) {
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM rendre WHERE idMembre = "+idPers+" ORDER BY dateRendu DESC ");
            List<Rendre> rendreList = new ArrayList<Rendre>();

            while (rs.next()) {
                int idRendu = rs.getInt("idRendu");
                int idMembre = rs.getInt("idMembre");
                int idPret = rs.getInt("idPret");
                java.sql.Timestamp  dateRendu = rs.getTimestamp("dateRendu");
                Rendre rendre = new Rendre(idRendu, idMembre, idPret, dateRendu);
                rendreList.add(rendre);
            }
            return  rendreList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
