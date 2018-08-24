package pl.jeleniagora.mks.reports;

import java.net.MalformedURLException;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;

public class AddBackgroundWatermark {

	private final static String mks = "./res/mks_logo_small.jpg";
	
	public static void addBackgroundWatermark(PdfDocument pdf, int pagenum) {
        PdfCanvas over;
        ImageData img = null;
		try {
			img = ImageDataFactory.create(mks);
		} catch (MalformedURLException e) {}
        PdfExtGState gs1 = new PdfExtGState();
        gs1.setFillOpacity(0.3f);


			over = new PdfCanvas(pdf.getPage(pagenum));
	        over.saveState();
	        over.setExtGState(gs1);
	        
	        float x = (pdf.getPage(pagenum).getPageSizeWithRotation().getLeft() + pdf.getPage(pagenum).getPageSizeWithRotation().getRight()) / 2;
	        float y = (pdf.getPage(pagenum).getPageSizeWithRotation().getTop() + pdf.getPage(pagenum).getPageSizeWithRotation().getBottom()) / 2;
	        over.addImage(img, img.getWidth(), 0, 0, img.getHeight(), x - (img.getWidth() / 2), y - (img.getHeight() / 2), false);
	
	        over.restoreState();
	}
}
