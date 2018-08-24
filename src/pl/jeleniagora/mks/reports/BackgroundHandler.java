package pl.jeleniagora.mks.reports;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfPage;

public class BackgroundHandler implements IEventHandler {

	@Override
	public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfPage page = docEvent.getPage();
        int pagenum = docEvent.getDocument().getPageNumber(page);
		if (pagenum > 1) {
			AddBackgroundWatermark.addBackgroundWatermark(docEvent.getDocument(), pagenum);

		}
	}

}
