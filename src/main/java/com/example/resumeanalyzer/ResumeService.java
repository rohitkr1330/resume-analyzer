package com.example.resumeanalyzer;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {

   private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
    "the","and","or","a","an","with","to","of","for","in","on","at","as","is","are","by","you","your",
    "we","our","from","this","that","it","its","be","will","can","etc"
));

// Basic normalization: lower-case and trim
private String norm(String s) {
    return s == null ? "" : s.toLowerCase().trim();
}

// Extract keywords from any text (JD or resume)
public List<String> extractKeywords(String text) {
    if (text == null) return Collections.emptyList();
    return Arrays.stream(text.split("[\\s,.;:()\\[\\]{}<>/\\\\]+"))
        .map(String::toLowerCase)
        .filter(w -> w.length() > 2 && !STOPWORDS.contains(w))
        .distinct()
        .collect(Collectors.toList());
}


// Match JD keywords against resume text
public List<String> matchKeywords(List<String> jdKeywords, String resumeText) {
    if (jdKeywords == null || resumeText == null) return Collections.emptyList();
    String lowerResume = norm(resumeText);
    List<String> found = new ArrayList<>();
    for (String keyword : jdKeywords) {
        if (lowerResume.contains(norm(keyword))) {
            found.add(keyword);
        }
    }
    return found;
}

// Keywords present in JD but not found in resume (suggest to add)
public List<String> missingFromResume(List<String> jdKeywords, List<String> foundKeywords) {
    if (jdKeywords == null) return Collections.emptyList();
    Set<String> found = new HashSet<>();
    if (foundKeywords != null) {
        for (String k : foundKeywords) found.add(norm(k));
    }
    List<String> missing = new ArrayList<>();
    for (String k : jdKeywords) {
        if (!found.contains(norm(k))) missing.add(k);
    }
    return missing;
}

// Keywords present in resume but not in JD (possible noise)
public List<String> noiseInResume(List<String> resumeKeywords, List<String> jdKeywords) {
    if (resumeKeywords == null) return Collections.emptyList();
    Set<String> jd = new HashSet<>();
    if (jdKeywords != null) {
        for (String k : jdKeywords) jd.add(norm(k));
    }
    List<String> noise = new ArrayList<>();
    for (String k : resumeKeywords) {
        if (!jd.contains(norm(k))) noise.add(k);
    }
    // keep unique items
    return noise.stream().distinct().collect(Collectors.toList());
}

// Convenience: extract keywords from resume text
public List<String> extractKeywordsFromResume(String resumeText) {
    return extractKeywords(resumeText);
}
}