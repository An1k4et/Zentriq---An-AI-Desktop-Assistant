package com.zentriq.ai;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class CustomDocumentLoader {

	//Load .txt,.pdf or .csv files return chunked document
	public static List<Document> loadFiles(Path path) throws IOException{
		String content;
		if(path.toString().endsWith(".txt")) {
			content = Files.readString(path);
		} else if(path.toString().endsWith(".txt")) {
			content = Files.readString(path);
		} else if(path.toString().endsWith(".txt")) {
			content = Files.readString(path);
		} else {
			throw new IllegalArgumentException("Unsupported file type: "+path.toString());
		}
		
		return chunckDocument(content, path.getFileName().toString());
	}
	
	// Read PDF content as a string
	private static String readPDF(Path path) throws IOException{
		try( PDDocument pdf = Loader.loadPDF(path.toFile())){
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(pdf);
		}
	}
	
	// Read CSV content as a string (all rows concatenated)
	private static String readCsv(Path path) throws IOException, CsvValidationException {
        StringBuilder sb = new StringBuilder();
        try (CSVReader reader = new CSVReader(new FileReader(path.toFile()))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                sb.append(String.join(", ", line)).append("\n");
            }
        }
        return sb.toString();
    }

	// Split content into chunks for RAG
	private static List<Document> chunckDocument(String content, String fileName) {
		List<Document> chunks = new ArrayList<>();
        int chunkSize = 500; // adjust size depending on your LLM token limit
        int start = 0;

        while (start < content.length()) {
            int end = Math.min(start + chunkSize, content.length());
            String chunk = content.substring(start, end);
            Document doc = new Document(chunk);
            doc.getMetadata().put("filename", fileName);
            chunks.add(doc);
            start = end;
        }

        return chunks;
	}
}
