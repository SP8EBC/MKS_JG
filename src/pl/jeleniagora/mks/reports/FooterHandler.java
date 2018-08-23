package pl.jeleniagora.mks.reports;

import java.awt.Font;
import java.io.IOException;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;

public class FooterHandler implements IEventHandler {

    protected Document doc;
    protected PdfFont fnt;

    public FooterHandler(Document document, PdfFont font) {
    	doc = document;
    	fnt = font;
    }
	
	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfCanvas canvas = new PdfCanvas(docEvent.getPage());
        Rectangle pageSize = docEvent.getPage().getPageSize();
        canvas.beginText();
        canvas.setFontAndSize(fnt, 5);
        canvas.moveText(0, (pageSize.getBottom() + doc.getBottomMargin()) - (pageSize.getTop() + doc.getTopMargin()) - 20)
                .showText("this is a footer")
                .endText()
                .release();
    }

}
