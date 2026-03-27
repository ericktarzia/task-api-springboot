package com.tarzia.taskapi.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarzia.taskapi.dto.common.BaseResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public ApiResponseBodyAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        String path = request.getURI().getPath();

        if (isInfrastructurePath(path)) {
            return body;
        }

        if (body instanceof BaseResponse<?>) {
            return body;
        }

        BaseResponse<Object> wrapped = BaseResponse.ok(body);

        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(wrapped);
            } catch (JsonProcessingException ex) {
                throw new IllegalStateException("Could not serialize BaseResponse", ex);
            }
        }

        return wrapped;
    }

    private boolean isInfrastructurePath(String path) {
        return path.contains("/v3/api-docs")
                || path.contains("/swagger-ui")
                || path.contains("/actuator")
                || path.endsWith("/docs")
                || path.contains("/docs/");
    }
}
