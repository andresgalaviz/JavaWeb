package Jeopardy;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author MarthaElena
 */
public class Controlador extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        String url = "/";
        if (action.equals("login")) {
            String user = request.getParameter("user");
            String password = request.getParameter("password");
            int id = DBhandler.validarUsuario(user, password);
            if (id != -1) {
                url = "/menu.jsp";
                request.getSession().setAttribute("idPerfil", id);
                request.getSession().removeAttribute("message");
            } else {
                url = "/login.jsp";
                Integer wrongPassword = 1;
                String mensaje;
                if(request.getSession().getAttribute("wrongPassword") != null) {
                    wrongPassword += (Integer) request.getSession().getAttribute("wrongPassword");
                } 
                mensaje = "Contraseña incorrecta, intente de nuevo. Tiene " + String.valueOf(3-wrongPassword) + " intentos mas.";
                if(wrongPassword >= 3) {
                        // TODO: Bloquear usuario
                    mensaje = "Su cuenta ha sido bloqueada";
                }
                request.getSession().setAttribute("message", mensaje);
                request.getSession().setAttribute("wrongPassword", wrongPassword);
            }
        } else if (action.equals("materias")) {
            int idPerfil = (int)request.getSession().getAttribute("idPerfil");
            List<Materia> materias = DBhandler.getMaterias(idPerfil);
            request.setAttribute("materias", materias);
            url = "/materias.jsp";
        } else if (action.equals("editarMateria")) {
            int id = Integer.valueOf(request.getParameter("id"));
            String elemento = (String)request.getParameter("element");
            String valor = (String)request.getParameter("valor");
            DBhandler.editarTabla("materias", id, elemento, valor);
        } else if (action.equals("borrarMateria")) {
            int id = Integer.valueOf(request.getParameter("id"));
            DBhandler.borrarElemento("materias", id);
        } else if (action.equals("agregarMateria")) {
            PrintWriter out = response.getWriter();
            int idPerfil = (int)request.getSession().getAttribute("idPerfil");
            int id = DBhandler.agregarMateria(idPerfil);
            out.println(id);
            return;
        }
        
        
        RequestDispatcher dispatcher
                = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
        
        /*try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Controlador</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Controlador at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }*/
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
