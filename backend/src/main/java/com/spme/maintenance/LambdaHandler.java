package com.spme.maintenance;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.mock.web.MockServletConfig;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static ConfigurableApplicationContext applicationContext;
    private static DispatcherServlet dispatcherServlet;
    //private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        if (applicationContext == null) {
            applicationContext = SpringApplication.run(MaintenanceApplication.class);
            dispatcherServlet = applicationContext.getBean(DispatcherServlet.class);
            dispatcherServlet.setContextConfigLocation("");
            try {
                MockServletConfig config = new MockServletConfig();
                config.addInitParameter("dispatchOptionsRequest", "true");
                dispatcherServlet.init(config);
            } catch (ServletException e) {
                throw new RuntimeException("Error al inicializar DispatcherServlet", e);
            }
            
        }

        MockHttpServletRequest mockRequest = createMockRequest(input);
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        try {
            dispatcherServlet.service(mockRequest, mockResponse);
            return createApiGatewayResponse(mockResponse);
        } catch (Exception e) {
            context.getLogger().log("Error al procesar la solicitud: " + e.getMessage());
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Error interno del servidor: " + e.getMessage());
        }
    }

    private MockHttpServletRequest createMockRequest(APIGatewayProxyRequestEvent input) {
        MockHttpServletRequest mockRequest;
        if ("POST".equalsIgnoreCase(input.getHttpMethod()) && input.getPath().equals("/resources/save-screenshot")) {
            mockRequest = new MockMultipartHttpServletRequest();
        } else {
            mockRequest = new MockHttpServletRequest();
        }

        mockRequest.setMethod(input.getHttpMethod());
        mockRequest.setRequestURI(input.getPath());
        mockRequest.setAsyncSupported(true);

        Map<String, String> headers = input.getHeaders();
        if (headers != null) {
            headers.forEach(mockRequest::addHeader);
        }

        Map<String, String> queryStringParameters = input.getQueryStringParameters();
        if (queryStringParameters != null) {
            queryStringParameters.forEach(mockRequest::setParameter);
        }

        if (input.getBody() != null) {
            if ("POST".equalsIgnoreCase(input.getHttpMethod()) && input.getPath().equals("/resources/save-screenshot")) {
                handleMultipartRequest(mockRequest, input);
            } else {
                mockRequest.setContent(input.getBody().getBytes());
            }
        }

        return mockRequest;
    }

    private void handleMultipartRequest(MockHttpServletRequest mockRequest, APIGatewayProxyRequestEvent input) {
        String boundary = extractBoundary(input.getHeaders().get("Content-Type"));
        String[] parts = input.getBody().split("--" + boundary);

        for (String part : parts) {
            if (part.contains("filename=")) {
                String[] lines = part.split("\r\n");
                String fileName = extractFileName(lines[1]);
                String fileContent = lines[lines.length - 2];

                MockMultipartFile mockFile = new MockMultipartFile("file", fileName, "application/octet-stream", fileContent.getBytes());
                ((MockMultipartHttpServletRequest) mockRequest).addFile(mockFile);
            } else if (part.contains("name=\"fileName\"")) {
                String fileName = part.split("\r\n\r\n")[1].trim();
                mockRequest.setParameter("fileName", fileName);
            }
        }
    }

    private String extractBoundary(String contentType) {
        return contentType.split("boundary=")[1];
    }

    private String extractFileName(String contentDisposition) {
        return contentDisposition.split("filename=")[1].replace("\"", "").trim();
    }

    private APIGatewayProxyResponseEvent createApiGatewayResponse(MockHttpServletResponse mockResponse) throws Exception {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(mockResponse.getStatus());
        response.setBody(mockResponse.getContentAsString());

        Map<String, String> headers = new HashMap<>();
        for (String headerName : mockResponse.getHeaderNames()) {
            headers.put(headerName, mockResponse.getHeader(headerName));
        }
        response.setHeaders(headers);

        return response;
    }
}
