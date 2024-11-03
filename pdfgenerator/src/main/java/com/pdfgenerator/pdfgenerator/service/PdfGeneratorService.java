package com.pdfgenerator.pdfgenerator.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PdfGeneratorService 
{

    public String generatePdf(Map<String, Object> data) throws IOException
    {
        String buyerGstin = (String) data.get("buyerGstin");
        String sellerGstin = (String) data.get("sellerGstin");

        
        if (buyerGstin.equals(sellerGstin)) 
        {
            return "Error: Duplicate GSTIN detected for buyer and seller. Please check the GSTIN numbers.";
        }

        String desktopPath = System.getProperty("user.home") + "/Desktop/invoice/";
        File invoiceDir = new File(desktopPath);
        if (!invoiceDir.exists()) 
        {
            invoiceDir.mkdirs(); 
        }

        
        String filePath = desktopPath + "invoice_" + sellerGstin + "_" + buyerGstin + ".pdf"; 
        if (new File(filePath).exists())
        {
            return "Error: An invoice already exists for this buyer and seller based on their GSTIN numbers.";
        }

    
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Table sellerTable = new Table(1);
        sellerTable.addCell(new Cell().add(new Paragraph("Seller").setBold()).setTextAlignment(TextAlignment.CENTER));
        sellerTable.addCell(new Cell().add(new Paragraph((String) data.get("seller"))));
        sellerTable.addCell(new Cell().add(new Paragraph(sellerGstin)));
        sellerTable.addCell(new Cell().add(new Paragraph((String) data.get("sellerAddress"))));

        Table buyerTable = new Table(1);
        buyerTable.addCell(new Cell().add(new Paragraph("Buyer").setBold()).setTextAlignment(TextAlignment.CENTER));
        buyerTable.addCell(new Cell().add(new Paragraph((String) data.get("buyer"))));
        buyerTable.addCell(new Cell().add(new Paragraph(buyerGstin)));
        buyerTable.addCell(new Cell().add(new Paragraph((String) data.get("buyerAddress"))));

      
        Table mainTable = new Table(2);
        mainTable.addCell(sellerTable);
        mainTable.addCell(buyerTable);
        document.add(mainTable);

  
        document.add(new Paragraph("Items").setBold().setFontSize(16).setMarginTop(20));

        
        Table itemsTable = new Table(4);
        itemsTable.addCell(new Cell().add(new Paragraph("Item Name")));
        itemsTable.addCell(new Cell().add(new Paragraph("Quantity")));
        itemsTable.addCell(new Cell().add(new Paragraph("Rate")));
        itemsTable.addCell(new Cell().add(new Paragraph("Amount")));

        List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
        for (Map<String, Object> item : items) 
        {
            itemsTable.addCell(new Cell().add(new Paragraph((String) item.get("name"))));
            itemsTable.addCell(new Cell().add(new Paragraph((String) item.get("quantity"))));
            itemsTable.addCell(new Cell().add(new Paragraph(item.get("rate").toString())));
            itemsTable.addCell(new Cell().add(new Paragraph(item.get("amount").toString())));
        }

      
        document.add(itemsTable);

        document.close();
        return "PDF generated successfully at " + filePath;
    }
}
