package pl.jeleniagora.mks.reports;

import java.awt.Font;
import java.io.IOException;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class FooterHandler implements IEventHandler {

    protected Document doc;
    protected PdfFont fnt;
    protected String heading;

    public FooterHandler(Document document, PdfFont font, String h) {
    	doc = document;
    	fnt = font;
    	heading = h;
    }
	
	@Override
	public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfPage page = docEvent.getPage();
        int pagenum = docEvent.getDocument().getPageNumber(page);
        
        PageSize pageSize = docEvent.getDocument().getDefaultPageSize();
        
		Paragraph footer = new Paragraph("Raport wygenerowany automatycznie przy użyciu programu MKS_JG. Autor: Mateusz Lubecki, Bielsko-Biała 2018, tel: +48 660 43 44 46").setFont(fnt);
		Paragraph header = new Paragraph("Raport końcowy z konkurencji: " + heading + " - strona " + pagenum).setFont(fnt);

		if (pagenum > 1) {
			doc.showTextAligned(header, pageSize.getWidth() / 2, pageSize.getHeight() - 20, pagenum, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
		}
		doc.showTextAligned(footer, pageSize.getWidth() / 2, 20, pagenum, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
		

    }

}
