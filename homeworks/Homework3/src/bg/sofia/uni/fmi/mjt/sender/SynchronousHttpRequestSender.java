package bg.sofia.uni.fmi.mjt.sender;

import bg.sofia.uni.fmi.mjt.exceptions.ConnectionFailedException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SynchronousHttpRequestSender extends HttpRequestSender {

    @Override
    public HttpResponse<String> sendRequest(String uriString) throws ConnectionFailedException {
        var request = HttpRequest.newBuilder(URI.create(uriString)).build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ConnectionFailedException("Connection was abruptly stopped.", e);
        }

        return response;
    }

}
