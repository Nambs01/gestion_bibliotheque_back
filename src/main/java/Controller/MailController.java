package Controller;

import Service.LivreService;
import Service.MailService;
import Service.MembreService;
import Service.PretService;
import Module.Pret;
import Module.Membre;
import Module.Livre;
import Util.Conversion;
import config.Header;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "sendEmail", value = "/sendEmail")
public class MailController extends HttpServlet {
    private final MailService mailService = new MailService();
    private final Gson gson = new Gson();
    private final Map<Object, Object> responseAPI = new HashMap<>();


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("idPret");
        if(id != null && !id.isEmpty()){
            try {
                Pret pret = new PretService().getById(id);
                Membre membre = new MembreService().getById(pret.getIdPers());

                List<Livre> livres = new ArrayList<Livre>();
                String idLivre = String.valueOf(pret.getIdLivre1());
                Livre livre = new LivreService().get(idLivre).get(0);
                livre.setExemplaire(pret.getNbrLivre1());
                livres.add(livre);

                if (pret.getIdLivre2() != null) {
                    idLivre = pret.getIdLivre2();
                    livre = new LivreService().get(idLivre).get(0);
                    livre.setExemplaire(pret.getNbrLivre1());
                    livres.add(livre);
                }

                String dateLimite = Conversion.sqlDateToString(pret.getDateRetour());

                if(mailService.sendMail(membre, livres, dateLimite)){
                    this.responseAPI.put("success", true);
                    this.responseAPI.put("message", "Email envoyée");
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    this.responseAPI.put("success", false);
                    this.responseAPI.put("message", "Email non envoyée, erreur de connexion.");
                }


            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            this.responseAPI.put("success", false);
            this.responseAPI.put("message", "Manque de parametre");
        }

        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(this.responseAPI));
        out.flush();

    }

    public void destroy() {}
}
