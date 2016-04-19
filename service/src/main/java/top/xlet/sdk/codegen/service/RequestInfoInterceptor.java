package top.xlet.sdk.codegen.service;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 打印请求参数.
 */
public class RequestInfoInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(RequestInfoInterceptor.class);

    private static final String START_TIME = "_START_TIME";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String agent = request.getHeader("User-Agent");

        Enumeration<?> parameterNames = request.getParameterNames();
        StringBuilder sb = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            String paramName = (String) parameterNames.nextElement();
            String[] values = request.getParameterValues(paramName);
            sb.append("[").append(paramName).append(":").append(ArrayUtils.toString(values)).append("]");
        }
        String params = StringUtils.isNotEmpty(sb.toString()) ? sb.insert(0, "\n").toString() : "";

        LOG.info("[{}] =>[{}] [{}]\n[{}]{}", IpUtils.getIpAddr(request), request.getMethod(), request.getRequestURL(), agent, params);
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        long start = Long.parseLong(request.getAttribute(START_TIME).toString());
        LOG.info("[{}] =>[{}] [{}] cost[{}]ms", IpUtils.getIpAddr(request), request.getMethod(), request.getRequestURL(), (System.currentTimeMillis() - start));
    }
}
