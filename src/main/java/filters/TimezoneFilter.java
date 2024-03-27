package filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import validators.TimezoneValidator;

import java.io.IOException;
import java.util.Optional;

@WebFilter(value = "/time")
public class TimezoneFilter extends HttpFilter {

    private static final String ERROR_MESSAGE = "<html><body style=\"background-color: #404040\"><div style=\"display: flex; justify-content: center;\">" +
            "<h2 style=\"font-family: Monaco\">Invalid timezone</h2></div></body></html>";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Optional<String> parameterOptional = Optional.ofNullable(req.getParameter("timezone"));

        if(parameterOptional.isPresent()){
            if(TimezoneValidator.isValidTimezone(parameterOptional.get().replace(" ", "+")))
                chain.doFilter(req, res);
            else {
                res.setContentType("text/html; charset=utf-8");
                res.getWriter().write(ERROR_MESSAGE);
                res.getWriter().close();
            }
        }
        else{
            chain.doFilter(req, res);
        }

    }
}
