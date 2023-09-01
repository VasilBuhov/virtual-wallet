package com.company.web.wallet.controllers.MvcController;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class WalletErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public String handleError(WebRequest webRequest) {
        Integer statusCode = (Integer) webRequest.getAttribute("javax.servlet.error.status_code", WebRequest.SCOPE_REQUEST);

        if (statusCode != null) {
            if (statusCode == 403) {
                return "errors/403";
            } else if (statusCode == 404) {
                return "errors/404";
            }
        }

        return "errors/generic";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}