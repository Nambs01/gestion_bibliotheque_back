package Controller;

import Service.LivreService;
import com.google.gson.Gson;
import config.Header;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.annotation.*;
import Module.Livre;

@WebServlet(name = "livre", value = "/livre")
public class LivreController extends HttpServlet {
    private LivreService livreService;
    private final Gson gson = new Gson();
    private final Map<Object, Object> responseAPI = new HashMap<>();

    @Override
    public void init() {
        try {
            livreService = new LivreService();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("param");
        List<Livre> livres = new ArrayList<Livre>();

        if(param != null) livres = livreService.get(param);
        else livres = livreService.getAll();

        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(livres));
        out.flush();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String designation = request.getParameter("designation");
        String exemplaire = request.getParameter("exemplaire");
        responseAPI.clear();

        if(designation != null && exemplaire != null) {
            designation = designation.trim();
            exemplaire = exemplaire.trim();

            if(!designation.isEmpty() && !exemplaire.isEmpty() && Integer.parseInt(exemplaire) > 0){
                Livre livre = new Livre(designation, Integer.parseInt(exemplaire));
                livreService.add(livre);

                this.responseAPI.put("success", true);
                this.responseAPI.put("message", "Add livre successfully !!");
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                this.responseAPI.put("success", false);
                this.responseAPI.put("message", "Verifier le nom de la livre et le nombre d'exemplaire");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            this.responseAPI.put("success", false);
            this.responseAPI.put("message", "Manque de parametre");
        }

        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(this.responseAPI));
        out.flush();

    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idString = request.getParameter("id");
        String designation = request.getParameter("designation");
        String exemplaire = request.getParameter("exemplaire");
        responseAPI.clear();

        if(idString != null && designation != null && exemplaire != null) {
            idString = idString.trim();
            designation = designation.trim();
            exemplaire = exemplaire.trim();
            if(!idString.isEmpty() && !designation.isEmpty() && !exemplaire.isEmpty()) {
                int id = Integer.parseInt(idString);
                int exemplaireInt = Integer.parseInt(exemplaire);
                Livre livre = new Livre(id, designation, exemplaireInt);
                livreService.update(livre);

                this.responseAPI.put("success", true);
                this.responseAPI.put("message", "Update livre successfully !!");
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                this.responseAPI.put("success", false);
                this.responseAPI.put("message", "Verifier le nom de la livre et le nombre d'exemplaire");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            this.responseAPI.put("success", false);
            this.responseAPI.put("message", "Manque de parametre");
        }

        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(this.responseAPI));
        out.flush();
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idString = request.getParameter("id");
        responseAPI.clear();

        if(idString != null) {
            idString = idString.trim();
            if(!idString.isEmpty()) {
                int id = Integer.parseInt(idString);
                livreService.delete(id);

                this.responseAPI.put("success", true);
                this.responseAPI.put("message", "Delete livre successfully !!");
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                this.responseAPI.put("success", false);
                this.responseAPI.put("message", "Verifier le nom de la livre et le nombre d'exemplaire");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            this.responseAPI.put("success", false);
            this.responseAPI.put("message", "Manque de parametre");
        }

        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(this.responseAPI));
        out.flush();
    }
    public void destroy() {
    }
}