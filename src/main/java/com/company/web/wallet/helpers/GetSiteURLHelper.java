package com.company.web.wallet.helpers;

import javax.servlet.http.HttpServletRequest;

public class GetSiteURLHelper {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
