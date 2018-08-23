package pl.jeleniagora.mks.reports;

import java.io.IOException;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
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
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class TestReport {

	public static String karpacz = "./res/karpacz_zawsze_gora.jpg";
	public static String bielsko = "./res/BB_logo.jpg";
	public static String mks = "./res/mks_logo_small.jpg";
	
	PdfFont font;
	PdfFont bold;
	
	public Cell getCell(String text, TextAlignment alignment, PdfFont font) {
	    Cell cell = new Cell().add(new Paragraph(text).setFont(font));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setBorder(Border.NO_BORDER);
	    cell.setBorderBottom(Border.NO_BORDER);
	    cell.setBorderLeft(Border.NO_BORDER);
	    cell.setBorderTop(Border.NO_BORDER);
	    cell.setBorderRight(Border.NO_BORDER);
	    return cell;
	}
	
	public Cell getCell(String text, TextAlignment alignment, PdfFont font, float fontSize) {
	    Cell cell = new Cell().add(new Paragraph(text).setFont(font).setFontSize(fontSize));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setBorder(Border.NO_BORDER);
	    cell.setBorderBottom(Border.NO_BORDER);
	    cell.setBorderLeft(Border.NO_BORDER);
	    cell.setBorderTop(Border.NO_BORDER);
	    cell.setBorderRight(Border.NO_BORDER);
	    return cell;
	}
	
	public void test() throws IOException {

		PdfDocument pdf = new PdfDocument(new PdfWriter("test.pdf"));
		Document document = new Document(pdf);
		pdf.setDefaultPageSize(PageSize.A4.rotate());
//        PageRotationEventHandler eventHandler = new PageRotationEventHandler();
		
        // image watermark
        ImageData img = ImageDataFactory.create(mks);
        PdfCanvas over;
        
        PdfExtGState gs1 = new PdfExtGState();
        gs1.setFillOpacity(0.3f);
   
		font = PdfFontFactory.createFont("./res/DejaVuSans.ttf", PdfEncodings.IDENTITY_H, true);
		bold = PdfFontFactory.createFont("./res/Uni_Sans_Heavy.otf", PdfEncodings.IDENTITY_H, true);
        
        
		Image bielskoImage = new Image(ImageDataFactory.create(bielsko), 0, 450, 200);
		Image karpaczImage = new Image(ImageDataFactory.create(karpacz), 720, 475, 100);
        
		Table titleTable = new Table(1);
		titleTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
		titleTable.addCell(getCell("Raport końcowy z konkurencji", TextAlignment.CENTER, bold, 18.0f));
		
		Table contestTable = new Table(1);
		contestTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
		contestTable.addCell(getCell("Jedynki Męskie", TextAlignment.CENTER, bold));
		contestTable.addCell(getCell("", TextAlignment.CENTER, font));
		contestTable.addCell(getCell("Memoriał Mariusza Warzyboka", TextAlignment.CENTER, font));
		contestTable.addCell(getCell("15 września 2018", TextAlignment.CENTER, font));
		
		Table scoreTable = new Table(5);
		scoreTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
		scoreTable.addCell(getCell("Numer Startowy", TextAlignment.CENTER, font));
		scoreTable.addCell(getCell("Lokata", TextAlignment.CENTER, font));
		scoreTable.addCell(getCell("Imię i Nazwisko", TextAlignment.CENTER, font));
		scoreTable.addCell(getCell("Klub", TextAlignment.CENTER, font));
		scoreTable.addCell(getCell("Czas", TextAlignment.CENTER, font));
	
		
//		titleTable.addCell("test").setTextAlignment(TextAlignment.CENTER).setBorderBottom(Border.NO_BORDER);
		
		document.add(bielskoImage);
		document.add(karpaczImage);
	
		
		over = new PdfCanvas(pdf.getFirstPage());
        over.saveState();
        over.setExtGState(gs1);
        
        float x = (pdf.getFirstPage().getPageSizeWithRotation().getLeft() + pdf.getFirstPage().getPageSizeWithRotation().getRight()) / 2;
        float y = (pdf.getFirstPage().getPageSizeWithRotation().getTop() + pdf.getFirstPage().getPageSizeWithRotation().getBottom()) / 2;
        over.addImage(img, img.getWidth(), 0, 0, img.getHeight(), x - (img.getWidth() / 2), y - (img.getHeight() / 2), false);

        over.restoreState();	
		document.add(titleTable);
		document.add(contestTable);
		
		Paragraph p = new Paragraph();
		p.setMultipliedLeading(2.0f);
		p.add(new Text("  ").setFontSize(20.0f));
		document.add(p);
		document.add(p);
		document.add(p);

		
		document.add(scoreTable);
		document.close();
		

	}

}
