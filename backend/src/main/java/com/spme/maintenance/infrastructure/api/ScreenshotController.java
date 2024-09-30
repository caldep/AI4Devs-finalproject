package com.spme.maintenance.infrastructure.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/resources")
public class ScreenshotController {

    @Value("${alert.screenshot.path}")
    private String screenshotPath;

    @PostMapping("/save-screenshot")
    public ResponseEntity<String> saveScreenshot(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("fileName") String fileName) {
        try {
            // Asegúrate de que el directorio existe
            File directory = new File(screenshotPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Guarda el archivo
            Path path = Paths.get(screenshotPath + fileName);
            Files.write(path, file.getBytes());

            return ResponseEntity.ok("Captura de pantalla guardada exitosamente");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al guardar la captura de pantalla: " + e.getMessage());
        }
    }
}
