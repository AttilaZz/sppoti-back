package com.fr.api;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	ResponseEntity handleFileUpload(@RequestParam("uploads") final MultipartFile[] files)
	{
		
		if (files.length == 0) {
			return ResponseEntity.badRequest().body("Please select a file to upload");
		}
		
		try {
			
			// Get the file and save it somewhere
			final List<byte[]> bytes = new ArrayList<>();
			for (final MultipartFile file : files) {
				bytes.add(file.getBytes());
			}
			
			//			final Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			//			Files.write(path, bytes);
			
			return ResponseEntity.ok().build();
			
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().body("");
		
	}
	
	@PostMapping(value = "/upload64")
	public @ResponseBody
	String uploadImage2(@RequestParam("uploads") final String imageValue, final HttpServletRequest request)
	{
		try {
			//This will decode the String which is encoded by using Base64 class
			final byte[] imageByte = Base64.decodeBase64(imageValue);
			
			return "success ";
		} catch (final Exception e) {
			e.printStackTrace();
			return "error = " + e;
		}
		
	}
	
}
