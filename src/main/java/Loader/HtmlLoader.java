package Loader;

import Model.LoadingModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

    @Service
    public class HtmlLoader {

        private static final Logger logger = Logger.getLogger(HtmlLoader.class.getName());

        public String createHtmlFile(OperationModel model) throws IOException{

            String inputURL = model.getUrl();
            StringBuilder fileHtml = new StringBuilder();
            try {
                logger.info("Попытка получить данные по адресу: " + inputURL);

                Document document = Jsoup.connect(inputURL).userAgent("Chrome/81.0.4044.138").get();
                model.setDocument(document);

                for (String value : document.html().split("\n")) {
                    fileHtml.append(value).append("\n");
                }
            } catch (IllegalArgumentException e) {
                logger.log(Level.WARNING, "Некорректно введен адрес страницы", e);
            }

            logger.info("Скачивание html страницы успешно завершено");
            model.setFileHtml(fileHtml.toString());

            return fileHtml.toString();
        }

        public String getWordStatistic(OperationModel model){

            StringBuilder statistic = new StringBuilder();

            logger.info("Парсинг html страницы на слова по разделителям");
            List<String> results = parseHTML(model.getDocument());
            logger.info("Список слов на странице успешно сформирован");

            logger.info("Формирование статистики слов");
            TreeMap<String, Integer> dictionary = analyzeText(results);
            logger.info("Формирование статистики успешно завершено");

            for (HashMap.Entry result : dictionary.entrySet()) {
                statistic.append(result.getKey()).append(" - ").append(result.getValue()).append("\n");
            }
            model.setStatistic(statistic.toString());
            return statistic.toString();
        }

        private List<String> parseHTML(Document document){
            List<String> result = new ArrayList<>();
            for (Element element : document.getElementsByTag("body")) {
                Collections.addAll(result, element.text().toUpperCase().split("[\\s.,!?;:()\\\\\\]\\[\"]+"));
            }
            return result;
        }

        private TreeMap<String, Integer>  analyzeText(List<String> words){
            TreeMap<String, Integer> dictionary = new TreeMap<>();
            for (String word: words){
                dictionary.merge(word, 1, Integer::sum);
            }
            return dictionary;
        }
    }

