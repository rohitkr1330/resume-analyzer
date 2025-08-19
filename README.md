Resume Analyzer

A Spring Boot web app that parses a PDF resume and compares it with a pasted Job Description (JD). It highlights matched keywords as badges, shows a match score with a progress bar, and suggests keywords to add or remove.

Features

-PDF parsing via Apache PDFBox

-Keyword extraction and exact matching

-Found keywords as badges

-Match score with progress bar and X/Y metrics

Suggestions:

-Suggested to Add: keywords in the JD but missing in the resume

-Possibly Irrelevant: keywords in the resume but not in the JD

-Clean two-column UI with Thymeleaf

Tech Stack

-Java 17+ (works with 11+, adjust as needed)

-Spring Boot

-Maven

-Apache PDFBox

-Thymeleaf, HTML/CSS

* Project Structure

src/main/java/.../WebController.java — upload endpoint and analysis flow

src/main/java/.../ResumeService.java — keyword extraction, matching, suggestions, scoring

src/main/resources/templates/index.html — UI (badges, progress bar, results)

* How to Run Locally

1.Build
mvn clean install -DskipTests

2.Run
mvn spring-boot:run

3.Open
http://localhost:8080

4.Use

-Upload a PDF resume

-Paste a Job Description

-Click Analyze to see matches, score, and suggestions

Notes

-Only text inside the PDF is analyzed (images are ignored).

-The analyzer uses simple exact matching; upcoming versions will add synonyms and semantic scoring.


Contributing
Issues and pull requests are welcome.
![image alt](https://github.com/rohitkr1330/resume-analyzer/blob/0abc164752b676031ac243a4e892f3d4d018bff3/screenshot1.png)
