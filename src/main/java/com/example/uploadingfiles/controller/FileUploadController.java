package com.example.uploadingfiles.controller;

import com.example.uploadingfiles.models.CSVModel;
import com.example.uploadingfiles.storage.StorageFileNotFoundException;
import com.example.uploadingfiles.storage.StorageService;
import com.sun.tools.jdeprscan.CSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
public class FileUploadController {

	private final StorageService storageService;
	public String nameFile;
	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {
//		model.addAttribute("files", storageService.loadAll().map(
//				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//						"serveFile", path.getFileName().toString()).build().toUri().toString())
//				.collect(Collectors.toList()));
 //print link to download complete generated file csv
		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@GetMapping("/generate")
	public String list (Model model) throws IOException {


//        while ((row = csvReader.readLine()) != null) {
//            String[] data = row.split(",");
//            // do something with the data
//        }
//        csvReader.close();

		return "generateForm";
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws IOException {
		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");
		redirectAttributes.addFlashAttribute("success", "Group and generate file");
		String row;
		BufferedReader csvReader = new BufferedReader(new FileReader("upload-dir/" + file.getOriginalFilename()));
		boolean firstLine = true;
		//String[][] arr = new String[][];
		while ((row = csvReader.readLine()) != null) {

			if(firstLine) {
				firstLine = false;
				continue;
			}

			List<String> data = Arrays.asList(row.split(","));

			//arr[data.get(0)][data.get(2)].add(data.get(1));

			CSVModel csvModel = new CSVModel(data.get(0), data.get(2), data.get(1));
			Stream<CSVModel> csvModelStream = Stream.of(csvModel);

			Map<String, List<CSVModel>> listMap = csvModelStream.collect(Collectors.groupingBy(CSVModel::getDate));

//
			for (Map.Entry<String, List<CSVModel>> item : listMap.entrySet()) {
				System.out.println(item.getKey());

				for (CSVModel csvModel1 : item.getValue()) {
					System.out.println(csvModel1.getName());
				}
				System.out.println();
			}
//
	}
		csvReader.close();


		return "redirect:";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
