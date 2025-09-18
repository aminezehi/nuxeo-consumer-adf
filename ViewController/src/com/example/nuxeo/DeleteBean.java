package com.example.nuxeo;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "deleteBean")
@RequestScoped
public class DeleteBean {

    private String name;
    private String message;
    private String error;

    public void delete(ActionEvent e) {
        if (name == null || name.trim().isEmpty()) {
            error = "Veuillez saisir le nom du document.";
            message = null;
            return;
        }

        String targetUrl = "http://localhost:7001/nuxeo-api/nuxeo/delete";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(targetUrl);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", name.trim()));
            post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            try (CloseableHttpResponse resp = httpClient.execute(post)) {
                int status = resp.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    message = "Document supprimé (nom: " + name + ").";
                    error = null;
                } else {
                    error = "Erreur HTTP: " + status + " lors de la suppression.";
                    message = null;
                }
            }
        } catch (Exception ex) {
            error = "Erreur de suppression: " + ex.getMessage();
            message = null;
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMessage() { return message; }
    public String getError() { return error; }
}