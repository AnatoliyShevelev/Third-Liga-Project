package ru.liga.tgbot.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.liga.tgbot.config.HttpHeadersConfig;
import ru.liga.tgbot.config.ProfileServiceConfig;
import ru.liga.tgbot.config.RestTemplateConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class ProfileAdapterService {


    @Autowired
    private ProfileServiceConfig profileServiceConfig;
    @Autowired
    private RestTemplateConfig restTemplateConfig;

    @Value("${path.image}")
    private String filePath;
    private static final Integer POSITION_X = 20;
    private static final Integer POSITION_Y = 50;

    /**
     * Сервис вызова перевода текста на старославянский
     *
     * @param text Входной обычный текст
     * @return Переведенный текст на старославянский
     * @throws URISyntaxException
     */
    public String translateToOldSlavonic(String text) throws URISyntaxException {
        HttpHeaders headers = new HttpHeadersConfig().settingHttpHeaders();
        URI url = new URI(profileServiceConfig.getTranslateUrl());
        HttpEntity<String> requestEntity = new HttpEntity<>(text, headers);
        return restTemplateConfig.restTemplate().postForObject(url, requestEntity, String.class);
    }

    /**
     * Сервис для размещения текста на картинке
     *
     * @param text входной текст
     * @throws URISyntaxException
     * @throws IOException
     */
    public ByteArrayOutputStream profileToPicture(String text) {
        File file = new File(filePath);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            log.error("Ошибка чтения изображения" + e);
            throw new RuntimeException(e);
        }
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(new Font("Old Standard TT", Font.BOLD, 15));

        int width = image.getWidth(); // ширина картинки
        int height = image.getHeight(); // высота картинки

//        Font font = g2d.getFont();
//        FontMetrics metrics = g2d.getFontMetrics(font);
//
//        int fontSize = 100; // начальный размер шрифта
//        while (true) {
//            Font newFont = font.deriveFont((float) fontSize);
//            metrics = g2d.getFontMetrics(newFont);
//
//            int lineHeight = metrics.getHeight();
//            String[] lines = text.split("\n");
//
//            int textHeight = lines.length * lineHeight;
//            int maxWidth = 0;
//            for (String line : lines) {
//                int lineWidth = metrics.stringWidth(line);
//                if (lineWidth > maxWidth) {
//                    maxWidth = lineWidth;
//                }
//            }
//
//            if (maxWidth > width || textHeight > height) {
//                fontSize--;
//            } else {
//                //g2d.setFont(newFont);
//                g2d.setFont(new Font("Old Standard TT", Font.BOLD, fontSize));
//                break;
//            }
//        }

        Font font = g2d.getFont();
        FontMetrics metrics = g2d.getFontMetrics(font);

        List<String> lines = new ArrayList<>();
        String[] words = text.split("\\s+");
        String currentLine = "";
        for (String word : words) {
            if (metrics.stringWidth(currentLine + " " + word) <= width - POSITION_X) {
                currentLine += " " + word;
            } else {
                lines.add(currentLine.trim());
                currentLine = word;
            }
        }
        lines.add(currentLine.trim());

        int x = POSITION_X;
        int y = POSITION_Y;
        for (String line : lines) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Old Standard TT", Font.BOLD, 15));
            g2d.drawString(line, x, y + metrics.getAscent());
            y += metrics.getHeight();
        }

        g2d.dispose(); // освобождение ресурсов Graphics2D

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return byteArrayOutputStream;
    }
}
