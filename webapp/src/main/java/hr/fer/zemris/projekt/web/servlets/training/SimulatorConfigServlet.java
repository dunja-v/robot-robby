package hr.fer.zemris.projekt.web.servlets.training;

import com.google.gson.Gson;
import hr.fer.zemris.projekt.web.servlets.Constants;
import hr.fer.zemris.projekt.web.utils.SimulatorConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Consumer;

@WebServlet(name = "SimulatorConfigServlet", urlPatterns = "/simulatorConfig")
public class SimulatorConfigServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        if (req.getSession().getAttribute(Constants.SESSION_KEY_TRAINING_THREAD) != null) {
            return;
        }

        SimulatorConfiguration simulatorConfig = (SimulatorConfiguration)
                req.getSession().getAttribute(Constants.SESSION_KEY_SIMULATOR_CONFIG);

        String json = new Gson().toJson(simulatorConfig);

        resp.getWriter().write(json);
        resp.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SimulatorConfiguration simulatorConfig = (SimulatorConfiguration)
                req.getSession().getAttribute(Constants.SESSION_KEY_SIMULATOR_CONFIG);

        configureParameter(req, "maxMoves", simulatorConfig::setMaxMoves);
        configureParameter(req, "gridHeight", simulatorConfig::setGridHeight);
        configureParameter(req, "gridWidth", simulatorConfig::setGridWidth);
        configureParameter(req, "numberOfGrids", simulatorConfig::setNumberOfGrids);
        configureParameter(req, "numberOfBottles", simulatorConfig::setNumberOfBottles);
        configureParameter(req, "mapRegenFrequency", simulatorConfig::setMapRegenFrequency);

        String variableBottlesParam = req.getParameter("variableBottles");
        if (variableBottlesParam != null) {
            boolean variableBottles = Boolean.valueOf(variableBottlesParam);
            simulatorConfig.setVariableBottles(variableBottles);
        }

        req.getSession().setAttribute(Constants.SESSION_KEY_SIMULATOR_CONFIG, simulatorConfig);

        this.doGet(req, resp);
    }

    private static void configureParameter(HttpServletRequest request, String parameterName, Consumer<Integer> setter) {
        String paramString = request.getParameter(parameterName);
        if (paramString == null) return;

        try {
            int parameter = Integer.parseInt(paramString);
            setter.accept(parameter);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
