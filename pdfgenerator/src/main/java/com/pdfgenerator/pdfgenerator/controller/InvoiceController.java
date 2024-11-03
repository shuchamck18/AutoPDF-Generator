package com.pdfgenerator.pdfgenerator.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdfgenerator.pdfgenerator.service.PdfGeneratorService;

@RestController
@RequestMapping("/api/pdf")
public class InvoiceController
    {
    private final PdfGeneratorService pdfGeneratorService;

   
    public InvoiceController(PdfGeneratorService pdfGeneratorService)
        {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @PostMapping("/generate")
    public String generatePdf(@RequestBody Map<String, Object> data)
        {
        try
            {
            return pdfGeneratorService.generatePdf(data);
        } 
        catch (Exception e)
            {
            return "Error generating PDF: " + e.getMessage();
        }
    }
}
