package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;
import utils.CookiesChecker;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private static final String DEFAULT_TIME_ZONE = "UTC";
    private TemplateEngine templateEngine;

    @Override
    public void init() {
        templateEngine = new TemplateEngine();

        JakartaServletWebApplication jakartaServletWebApplication =
                JakartaServletWebApplication.buildApplication(this.getServletContext());

        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(jakartaServletWebApplication);

        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setOrder(templateEngine.getTemplateResolvers().size());
        templateResolver.setCacheable(false);

        templateEngine.addTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder resultTime = new StringBuilder();

        Optional<String> parameterOptional = Optional.ofNullable(req.getParameter("timezone"));

        if(parameterOptional.isPresent()){
            String timeZone = parameterOptional.get().replace(" ", "+");

            resp.addCookie(new Cookie("lastTimezone", timeZone));

            String time = LocalDateTime.now(ZoneId.of(timeZone))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            resultTime.append(time).append(" ").append(timeZone);
        }
        else{
            if(CookiesChecker.checkIfContainsCookie(req)) {
                String timeZone = DEFAULT_TIME_ZONE;
                Cookie[] cookies = req.getCookies();

                for(Cookie cookie : cookies){
                    if(cookie.getName().equals("lastTimezone")){
                        timeZone = cookie.getValue();
                        break;
                    }
                }
                String time = LocalDateTime.now(ZoneId.of(timeZone))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                resultTime.append(time).append(" ").append(timeZone);
            }
            else{
                String time = LocalDateTime.now(ZoneId.of(DEFAULT_TIME_ZONE))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                resultTime.append(time).append(" ").append(DEFAULT_TIME_ZONE);
            }
        }

        Context context = new Context(req.getLocale(), Map.of("time", resultTime.toString()));

        resp.setContentType("text/html");
        templateEngine.process("current_time", context, resp.getWriter());
        resp.getWriter().close();
    }
}
