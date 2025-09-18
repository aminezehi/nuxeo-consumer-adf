package com.example.nuxeo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "documentsBean")
@RequestScoped
public class DocumentsBean {
    
    private List<Document> documents = new ArrayList<>();
    
    public List<Document> getDocuments() {
        if (documents.isEmpty()) {
            loadDocuments();
        }
        return documents;
    }
    
    private void loadDocuments() {
        try {
            String url = "http://localhost:7001/nuxeo-api/nuxeo/documents";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            // Parse simple du JSON (sans Jackson)
            String json = response.toString();
            parseJsonSimple(json);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void parseJsonSimple(String json) {
        // Parse simple pour extraire les documents
        String[] items = json.split("\\},\\{");
        
        for (String item : items) {
            Document doc = new Document();
            
            // Extraire UID
            if (item.contains("\"uid\":")) {
                int start = item.indexOf("\"uid\":\"") + 7;
                int end = item.indexOf("\"", start);
                if (start > 6 && end > start) {
                    doc.setUid(item.substring(start, end));
                }
            }
            
            // Extraire Title
            if (item.contains("\"title\":")) {
                int start = item.indexOf("\"title\":\"") + 9;
                int end = item.indexOf("\"", start);
                if (start > 8 && end > start) {
                    doc.setTitle(item.substring(start, end));
                }
            }
            
            // Extraire Type
            if (item.contains("\"type\":")) {
                int start = item.indexOf("\"type\":\"") + 8;
                int end = item.indexOf("\"", start);
                if (start > 7 && end > start) {
                    doc.setType(item.substring(start, end));
                }
            }
            
            // Extraire Path
            if (item.contains("\"path\":")) {
                int start = item.indexOf("\"path\":\"") + 8;
                int end = item.indexOf("\"", start);
                if (start > 7 && end > start) {
                    doc.setPath(item.substring(start, end));
                }
            }
            
            documents.add(doc);
        }
    }
    
    public static class Document {
        private String uid;
        private String title;
        private String type;
        private String path;
        
        // Getters et setters
        public String getUid() { return uid; }
        public void setUid(String uid) { this.uid = uid; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
    }
}