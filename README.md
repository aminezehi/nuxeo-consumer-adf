# NuxeoConsumerApp (ADF)

Client ADF/JSF consommant l'API Spring (Nuxeo).

## Prérequis
- JDeveloper 12c / ADF Runtime
- Java 17 (selon votre config)
- HttpClient 4.x JARs ajoutés au classpath du module ViewController

## Pages
- public_html/documents.jsf : liste (GET backend)
- public_html/upload.jspx : upload multipart vers /nuxeo-api/nuxeo/upload
- public_html/delete.jspx : suppression par UID via /nuxeo-api/nuxeo/delete/select

## Démarrer
- Run IntegratedWebLogicServer depuis JDeveloper
- Accéder via mapping JSF: http://127.0.0.1:7101/ViewController/faces/upload.jspx

## Intégration backend
- Backend Spring: https://github.com/aminezehi/nuxeo-client
- Assurez-vous qu'il est déployé sur WebLogic avec contexte /nuxeo-api

