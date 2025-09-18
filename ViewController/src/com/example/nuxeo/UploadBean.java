package com.example.nuxeo;

import org.apache.myfaces.trinidad.model.UploadedFile;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import java.io.InputStream;

@ManagedBean(name = "uploadBean")
@RequestScoped
public class UploadBean {

    private UploadedFile file;
    private String message;
    private String error;

    public void upload(ActionEvent e) {
        if (file == null) {
            error = "Veuillez choisir un fichier.";
            message = null;
            return;
        }

        String targetUrl = "http://localhost:7001/nuxeo-api/nuxeo/upload";

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             InputStream is = file.getInputStream()) {

            HttpEntity multipart = MultipartEntityBuilder.create()
                    .addBinaryBody("file", is, ContentType.DEFAULT_BINARY, file.getFilename())
                    .build();

            HttpPost post = new HttpPost(targetUrl);
            post.setEntity(multipart);

            try (CloseableHttpResponse resp = httpClient.execute(post)) {
                int status = resp.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    message = "Fichier uploadé avec succès.";
                    error = null;
                } else {
                    error = "Erreur HTTP: " + status;
                    message = null;
                }
            }
        } catch (Exception ex) {
            error = "Erreur d'envoi: " + ex.getMessage();
            message = null;
        }
    }

    public UploadedFile getFile() { return file; }
    public void setFile(UploadedFile file) { this.file = file; }
    public String getMessage() { return message; }
    public String getError() { return error; }
}