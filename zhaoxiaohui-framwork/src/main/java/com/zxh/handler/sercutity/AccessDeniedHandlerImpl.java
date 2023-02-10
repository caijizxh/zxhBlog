package com.zxh.handler.sercutity;

import com.alibaba.fastjson.JSON;
import com.zxh.domain.ResponseResult;
import com.zxh.enums.AppHttpCodeEnum;
import com.zxh.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        e.printStackTrace();
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);

        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
