package com.fr.api;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@Controller
public class IndexController {


    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "./upload/";

    private static Logger LOGGER = Logger.getLogger(IndexController.class);

//    @GetMapping(value = {"/", "/login"})
//    public String homePage() {
//        return "index";
//    }

    @PostMapping("/upload")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            return ResponseEntity.ok().body(file.getOriginalFilename());

        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info(file.toString());

        return ResponseEntity.ok().body("");

    }


}
