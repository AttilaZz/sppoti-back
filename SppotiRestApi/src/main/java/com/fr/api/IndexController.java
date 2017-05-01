package com.fr.api;

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
class IndexController
{
	
	
	//Save the uploaded file to this folder
	private static final String UPLOADED_FOLDER = "./upload/";
	
	//    @GetMapping(value = {"/", "/login"})
	//     String homePage() {
	//        return "index";
	//    }
	
	@PostMapping("/upload")
	ResponseEntity handleFileUpload(@RequestParam("file") final MultipartFile file)
	{
		
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please select a file to upload");
		}
		
		try {
			
			// Get the file and save it somewhere
			final byte[] bytes = file.getBytes();
			final Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			
			return ResponseEntity.ok().body(file.getOriginalFilename());
			
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().body("");
		
	}
	
	
}
