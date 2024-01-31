package bg.sofia.uni.fmi.mjt.sender;

import bg.sofia.uni.fmi.mjt.exceptions.ConnectionFailedException;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public abstract class HttpRequestSender {

    protected final HttpClient httpClient;

    public HttpRequestSender() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public abstract HttpResponse<String> sendRequest(String uriString)
        throws ConnectionFailedException;

}
