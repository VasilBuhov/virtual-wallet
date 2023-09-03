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
            switch (statusCode) {
                case 401:
                    return "errors/401";
                case 403:
                    return "errors/403";
                case 404:
                    return "errors/404";
                case 413:
                    return "errors/413";
                case 415:
                    return "errors/415";
                case 500:
                    return "errors/500";
                default:
                    break;
            }
        }
        return "errors/generic";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}