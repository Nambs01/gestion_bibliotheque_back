package Controller;

import Service.PretService;
import com.google.gson.Gson;
import config.Header;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

import Module.Pret;
import config.Util;

@WebServlet(name = "pret", value = "/pret")
public class PretController extends HttpServlet {
    private PretService pretService;
    private final Gson gson = new Gson();
    private final Map<Object, Object> responseAPI = new HashMap<>();

    @Override
    public void init() {
        try {
            this.pretService = new PretService();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idPers = request.getParameter("idPers");
        String dateDeb = request.getParameter("dateDeb");
        String dateFin = request.getParameter("dateFin");
        List<Pret> listPret;

        if(idPers != null) {
            listPret = pretService.getHistoricByPers(idPers);
        }
        else if(dateDeb != null && dateFin != null) listPret = pretService.getBetweenDate(dateDeb, dateFin);
        else listPret = pretService.getAll();

        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(listPret));
        out.flush();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idPers = request.getParameter("idPers");
        String idLivre1 = request.getParameter("idLivre1");
        String idLivre2 = request.getParameter("idLivre2");
        String nbrLivre1 = request.getParameter("nbrLivre1");
        String nbrLivre2 = request.getParameter("nbrLivre2");
        String datePret = request.getParameter("datePret");

        responseAPI.clear();
        if (idPers != null && idLivre1 != null && nbrLivre1 != null && datePret != null) {
            Pret pret = new Pret();
            initPret(idPers, idLivre1, idLivre2, nbrLivre1, nbrLivre2, datePret, pret);

            pretService.add(pret);

            this.responseAPI.put("success", true);
            this.responseAPI.put("message", "Add pret successfully !!");
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
        String id = request.getParameter("id");
        String idPers = request.getParameter("idPers");
        String idLivre1 = request.getParameter("idLivre1");
        String idLivre2 = request.getParameter("idLivre2");
        String nbrLivre1 = request.getParameter("nbrLivre1");
        String nbrLivre2 = request.getParameter("nbrLivre2");
        String datePret = request.getParameter("datePret");

        responseAPI.clear();
        if (idPers != null && idLivre1 != null && nbrLivre1 != null && datePret != null) {
            Pret pret = new Pret();
            pret.setId(Integer.parseInt(id));
            initPret(idPers, idLivre1, idLivre2, nbrLivre1, nbrLivre2, datePret, pret);

            pretService.update(pret);

            this.responseAPI.put("success", true);
            this.responseAPI.put("message", "Update pret successfully !!");
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
        String id = request.getParameter("id");
        responseAPI.clear();

        if(id != null){
            pretService.delete(Integer.parseInt(id));

            this.responseAPI.put("success", true);
            this.responseAPI.put("message", "Delete livre successfully !!");
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



    private void initPret(String idPres, String idLivre1, String idLivre2, String nbrLivre1, String nbrLivre2, String datePret, Pret pret) {
        pret.setIdPers(Integer.parseInt(idPres));
        pret.setIdLivre1(Integer.parseInt(idLivre1));
        pret.setNbrLivre1(Integer.parseInt(nbrLivre1));
        pret.setIdLivre2(idLivre2);
        pret.setNbrLivre2(nbrLivre2);

        pret.setDatePret(datePret);
        pret.setDateRetour(Util.add14Days(datePret));
    }

    public void destroy() {}
}
