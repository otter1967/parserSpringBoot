package Model;

import org.jsoup.nodes.Document;

public class LoadingModel {

    private String url;
    private String fileHtml;
    private String statistic;
    private Document document;


    public LoadingModel() {
    }


    public LoadingModel(String statistic) {
        this.statistic = statistic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }

    public String getFileHtml() {
        return fileHtml;
    }

    public void setFileHtml(String fileHtml) {
        this.fileHtml = fileHtml;
    }


    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}