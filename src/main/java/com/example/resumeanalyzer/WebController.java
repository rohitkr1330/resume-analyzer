package com.example.resumeanalyzer;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    @Autowired
private ResumeService resumeService;

@GetMapping("/")
public String index() {
    return "index";
}

@PostMapping("/upload")
public String handleFileUpload(
        @RequestParam("file") MultipartFile file,
        @RequestParam("jobDescription") String jobDescription,
        RedirectAttributes redirectAttributes) {

    if (file.isEmpty() || jobDescription == null || jobDescription.trim().isEmpty()) {
        redirectAttributes.addFlashAttribute("message", "Please upload a file and paste a job description.");
        return "redirect:/";
    }

    try {
        // Read resume PDF text
        PDDocument document = Loader.loadPDF(file.getBytes());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String resumeText = pdfStripper.getText(document);
        document.close();

        // Compute keywords and metrics
        List<String> jdKeywords = resumeService.extractKeywords(jobDescription);
        List<String> foundKeywords = resumeService.matchKeywords(jdKeywords, resumeText);
        List<String> missingKeywords = resumeService.missingFromResume(jdKeywords, foundKeywords);
        List<String> resumeKeywordsAll = resumeService.extractKeywordsFromResume(resumeText);
        List<String> noiseKeywords = resumeService.noiseInResume(resumeKeywordsAll, jdKeywords);

        int foundCount = foundKeywords.size();
        int jdCount = jdKeywords.size();
        int score = jdCount == 0 ? 0 : (int) ((foundCount / (double) jdCount) * 100);

        // Data for UI
        redirectAttributes.addFlashAttribute("message", "File processed successfully!");
        redirectAttributes.addFlashAttribute("foundKeywords", foundKeywords);
        redirectAttributes.addFlashAttribute("missingKeywords", missingKeywords);
        redirectAttributes.addFlashAttribute("noiseKeywords", noiseKeywords);
        redirectAttributes.addFlashAttribute("foundCount", foundCount);
        redirectAttributes.addFlashAttribute("jdCount", jdCount);
        redirectAttributes.addFlashAttribute("score", score);

    } catch (IOException e) {
        redirectAttributes.addFlashAttribute("message", "Error processing file: " + e.getMessage());
    }
    return "redirect:/";
}
}