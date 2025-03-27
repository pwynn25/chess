package ui;

import com.google.gson.Gson;
import exception.ExceptionResponseNoThrow;
import request.*;
import result.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private String authToken;
    private String serverUrl;

    public ServerFacade(String url){
        this.serverUrl = url;
    }

    public RegisterResult register(String username, String password, String email) throws ServerException{
        RegisterRequest r = new RegisterRequest(username,password,email);
        var path = "/user";
        RegisterResult res = makeRequestPlusGetResponse("POST",path,r,RegisterResult.class);
        this.authToken = res.authToken();
        return res;
    }
    public JoinResult join(JoinRequest r) throws ServerException{
        var path = "/game";
        return makeRequestPlusGetResponse("PUT",path,r,JoinResult.class);
    }
    public ListResult list(ListRequest r) throws ServerException{
        var path = "/game";
        return makeRequestPlusGetResponse("GET",path,r,ListResult.class);
    }
    public LogoutResult logout(LogoutRequest r) throws ServerException{
        var path = "/session";
        return makeRequestPlusGetResponse("DELETE",path,r,LogoutResult.class);
    }
    public CreateResult create(CreateRequest r) throws ServerException{
        var path = "/game";
        return makeRequestPlusGetResponse("POST",path,r,CreateResult.class);
    }
    public LoginResult login(LoginRequest r) throws ServerException{
        var path = "/session";
        LoginResult res = makeRequestPlusGetResponse("POST",path,r,LoginResult.class);
        this.authToken = res.authToken();
        return res;
    }

    public <T> T makeRequestPlusGetResponse(String method, String path, Object request, Class<T> responseClass) throws ServerException{
        try {
            URL url = (new URI(this.serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeBodyAndHeader(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(500, ex.getMessage());
        }
    }

    private void writeBodyAndHeader(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            addAuthToken(request,http);
//            System.out.println(request);
            String reqData = new Gson().toJson(request);
            if(!(request instanceof ListRequest)) {
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(reqData.getBytes());
                }
            }
        }
    }
    private void addAuthToken(Object request, HttpURLConnection http) {

        if (request instanceof JoinRequest) {
            http.addRequestProperty("authorization",this.authToken);
        }
        if (request instanceof CreateRequest) {
            http.addRequestProperty("authorization",this.authToken);
        }
        if (request instanceof ListRequest) {
            http.addRequestProperty("authorization",this.authToken);
        }
        if (request instanceof LogoutRequest) {
            http.addRequestProperty("authorization",this.authToken);
        }
        if (request instanceof ClearRequest) {
            http.addRequestProperty("authorization",this.authToken);
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ServerException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErrJSON = http.getErrorStream()) {
                ExceptionResponseNoThrow respError = new Gson().fromJson(new InputStreamReader(respErrJSON), ExceptionResponseNoThrow.class);
                throw new ServerException(status,respError.getMessage());
            }
//            catch (Exception e) {
//                throw new ServerException(500,e.getMessage());
//            }
        }
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


}
