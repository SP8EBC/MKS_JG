package pl.jeleniagora.mks.reports;

import java.io.IOException;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

public class TestReport {

	public static String karpacz = "./res/karpacz_zawsze_gora.jpg";
	public static String bielsko = "./res/BB_logo.jpg";
	
	public Cell getCell(String text, TextAlignment alignment) {
	    Cell cell = new Cell().add(new Paragraph(text));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setBorder(Border.NO_BORDER);
	    return cell;
	}
	
	public void test() throws IOException {

		PdfDocument pdf = new PdfDocument(new PdfWriter("test.pdf"));
		Document document = new Document(pdf);
		pdf.setDefaultPageSize(PageSize.A4.rotate());
//        PageRotationEventHandler eventHandler = new PageRotationEventHandler();
        
		Image bielskoImage = new Image(ImageDataFactory.create(bielsko), 0, 450, 200);
		Image karpaczImage = new Image(ImageDataFactory.create(karpacz), 720, 475, 100);
        
		Table titleTable = new Table(1);
		titleTable.addCell(getCell("Raport z zawodów: test testttt", TextAlignment.CENTER));
		
		PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
		PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
		Text author = new Text("Robert Louis Stevenson").setFont(font);
//		Paragraph p = new Paragraph().add(title).add(" by ").add(author);
//		document.add(p);
		document.add(titleTable);
		document.add(bielskoImage);
		document.add(karpaczImage);
		document.close();
		

	}

}
