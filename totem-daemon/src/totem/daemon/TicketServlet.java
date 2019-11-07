package totem.daemon;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gmoreno on 29/09/2017.
 */
public class TicketServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PrintTicketData data = objectMapper.readValue(req.getInputStream(), PrintTicketData.class);
            objectMapper.writeValue(System.out, data);
            System.out.println();
            TicketPrinter.get().checkStatus();
            TicketPrinter.get().printTicket(data);
            resp.addHeader("Content-Type", "application/json");
            PrintTicketResult result = new PrintTicketResult();
            result.setOk(true);
            objectMapper.writeValue(resp.getOutputStream(), result);
        } catch (Throwable t) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error imprimiendo ticket", t);
            PrintTicketResult result = new PrintTicketResult();
            result.setOk(false);
            result.setMessage("Se produjo un error en la impresora");
            result.setErrorMessage(t.getMessage());
            objectMapper.writeValue(resp.getOutputStream(), result);
        }
    }

}
