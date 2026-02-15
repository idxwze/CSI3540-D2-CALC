package com.seifeddine.calculator;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/calculatrice")
public class CalculatriceServlet extends HttpServlet {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CalculatorService calculatorService = new CalculatorService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String previousAccess = (String) session.getAttribute("last_access");
        String currentAccess = LocalDateTime.now().format(FORMATTER);
        session.setAttribute("last_access", currentAccess);

        String aRaw = request.getParameter("a");
        String bRaw = request.getParameter("b");
        String operationRaw = request.getParameter("operation");

        String message;
        boolean error;

        try {
            double a = InputValidator.parseDouble(aRaw, "Nombre A");
            double b = InputValidator.parseDouble(bRaw, "Nombre B");
            Operation operation = Operation.fromParam(operationRaw);
            double result = calculatorService.compute(a, b, operation);
            message = "Résultat: " + a + " " + operation.getSymbol() + " " + b + " = " + result;
            error = false;
        } catch (IllegalArgumentException ex) {
            message = ex.getMessage();
            error = true;
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!doctype html>");
            out.println("<html lang='fr'>");
            out.println("<head>");
            out.println("  <meta charset='UTF-8'>");
            out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("  <title>Résultat - Calculatrice Servlet</title>");
            out.println("  <link rel='stylesheet' href='styles.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("  <main class='card'>");
            out.println("    <h1>Résultat (Servlet)</h1>");
            out.println("    <div class='result " + (error ? "error" : "ok") + "'>" + escapeHtml(message) + "</div>");
            out.println("    <p class='meta'><strong>Dernier accès:</strong> "
                    + escapeHtml(previousAccess == null ? "Aucun accès précédent" : previousAccess) + "</p>");
            out.println("    <p class='meta'><strong>Accès actuel:</strong> " + escapeHtml(currentAccess) + "</p>");
            out.println("    <p><a class='back' href='index.html'>Nouvelle opération</a></p>");
            out.println("  </main>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
