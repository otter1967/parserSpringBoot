package Controller;



import Model.LoadingModel;
import Loader.HtmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@ControllerAdvice
public class SpringController {

    private static Logger logger = Logger.getLogger(HtmlLoader.class.getName());
    LoadingModel loadingModel = new OperationModel();
    @Autowired
    private HtmlLoader htmlLoader;

    @RequestMapping("/")
    public String getIndexPage(Model model) {
        model.addAttribute("operationModel", loadingModel);
        model.addAttribute("error", "");
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params = "submit")
    public String parse(HttpServletResponse response, @ModelAttribute("operationModel") OperationModel operationModel, Model model) {
        if (!operationModel.getUrl().equals("")) {
            try {
                String fileHtml = htmlLoader.createHtmlFile(operationModel);

                if (operationModel.getFileHtml() != null && !operationModel.getFileHtml().equals("")) {
                    model.addAttribute("file", fileHtml);
                    model.addAttribute("statistic", htmlLoader.getWordStatistic(operationModel));
                    model.addAttribute("error", "");
                } else {
                    logger.log(Level.WARNING, "URL не введен или введен некорректно");
                    model.addAttribute("error", "Введите корректный URL адрес");
                }

            } catch (IOException e) {
                logger.log(Level.WARNING, e.getMessage());
                model.addAttribute("error", "Произошла ошибка");
            }
        } else {
            logger.log(Level.WARNING, "URL не введен или введен некорректно");
            model.addAttribute("error", "Введите корректный URL адрес");
        }
        this.loadingModel = operationModel;
        return "index";

    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params = "saveStatistic")
    public void saveStatistic(HttpServletResponse response) throws IOException {
        if (loadingModel.getUrl() != null) {
            logger.info("Запрос на скачивание статистки");
            downloadFile(response, loadingModel.getStatistic(), getName(loadingModel.getUrl()) + "-statistic.txt");
        }
        else {
            logger.log(Level.WARNING, "Данные о содержимом скачиваемого фала отсутствуют");
            response.sendRedirect("");
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params = "save")
    public void save(HttpServletResponse response) throws IOException {
        if (loadingModel.getUrl() != null) {
            logger.info("Запрос на скачивание HTML страницы");
            downloadFile(response, loadingModel.getFileHtml(), getName(loadingModel.getUrl()) + ".html");
        }
        else {
            logger.log(Level.WARNING, "Данные о содержимом скачиваемого файла отсутствуют");
            response.sendRedirect("");
        }
    }

    private void downloadFile(HttpServletResponse response, String fileStr, String fileName) throws IOException {
        if (fileStr != null && !fileStr.equals("")) {
            InputStream inputStream = new ByteArrayInputStream(fileStr.getBytes());
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
            OutputStream out = response.getOutputStream();
            FileCopyUtils.copy(inputStream, out);
            response.flushBuffer();
            out.flush();
            out.close();
            logger.info("Скачивание файла прошло успешно");
        } else {
            logger.log(Level.WARNING, "Данные о содержимом скачиваемого фала отсутствуют");
            response.sendRedirect("");
        }
    }

    private String getName(String url){
        String[] domain = url.split("/");
        return (domain.length > 2) ? domain[2] : "";
    }

}