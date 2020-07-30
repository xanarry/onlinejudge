package com.xanarry.onlinejudge.controller;

import com.xanarry.onlinejudge.controller.beans.MessageBean;
import com.xanarry.onlinejudge.controller.beans.PageBean;
import com.xanarry.onlinejudge.utils.Consts;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static String getErrorPageURL(MessageBean messageBean) {
        String url = "/message";
        try {
            url = "/message?" +
                    "title=" + URLEncoder.encode(messageBean.getTitle(), "utf8") +
                    "&header=" + URLEncoder.encode(messageBean.getHeader(), "utf8") +
                    "&message=" + URLEncoder.encode(messageBean.getMessage(), "utf8") +
                    "&url=" + URLEncoder.encode(messageBean.getUrl(), "utf8") +
                    "&linkText=" + URLEncoder.encode(messageBean.getLinkText(), "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String urlWithoutPageInfo(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Map<String, String[]> params = request.getParameterMap();

        ArrayList<String> strParams = new ArrayList<>(params.size());
        for (String key : params.keySet()) {
            if (!key.equals("page")) {
                strParams.add(key + "=" + params.get(key)[0]);
            }
        }

        if (strParams.size() == 0) {
            return uri + "?";//有唯一的page参数, 去除之后应该跟?
        } else {
            return String.format("%s?%s&", uri, String.join("&", strParams));//有参数, 在后面最佳参数
        }
    }

    public static PageBean getPagination(int recordCount, int currentPage, HttpServletRequest request) {
        PageBean pageBean = new PageBean();
        int maxPageVal = recordCount / Consts.COUNT_PER_PAGE;
        if (recordCount % Consts.COUNT_PER_PAGE != 0) {
            maxPageVal++; //整数页还有预项, 增加一页
        }
        pageBean.setMaxPageCount(maxPageVal);
        pageBean.setCurrentPage(currentPage);
        pageBean.setRecordCount(recordCount);
        pageBean.setCountPerPage(Consts.COUNT_PER_PAGE);
        pageBean.setBaseURL(Utils.urlWithoutPageInfo(request));
        return pageBean;
    }

    @Deprecated
    public static HashMap<String, String> getCookieMap(HttpServletRequest request) {
        HashMap<String, String> cookieMap = new HashMap<>(4);
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("userID") || c.getName().equals("userName") || c.getName().equals("userType")) {
                    cookieMap.put(c.getName(), c.getValue());
                }
            }
        }
        return cookieMap;
    }
}
