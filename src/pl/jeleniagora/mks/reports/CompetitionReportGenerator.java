package pl.jeleniagora.mks.reports;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;

public class CompetitionReportGenerator {

	final static String karpacz = "./res/karpacz_zawsze_gora.jpg";
	final static String bielsko = "./res/BB_logo.jpg";
	final static String mks = "./res/mks_logo_small.jpg";
	
	PdfFont font;
	PdfFont bold;
	
	float tableHeaderFontSize;
	
	Competition competitionToGenerateFrom;
	Competitions competitions;
	
	public CompetitionReportGenerator(Competition c, Competitions cmps, int tableFontSize, int infoFontSize, int footerFontSize) {
		competitionToGenerateFrom = c;
		competitions = cmps;
	}
	
	public Cell getCell(String text, TextAlignment alignment, PdfFont font, int rowspan, int colspan,  boolean sideBorder, boolean bottomBorder) {
	    Cell cell = new Cell(rowspan, colspan).add(new Paragraph(text).setFont(font).setFontSize(tableHeaderFontSize));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
	    if (bottomBorder)
	    	cell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderBottom(Border.NO_BORDER);
	    if (sideBorder)
	    	cell.setBorderLeft(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderLeft(Border.NO_BORDER);
	    cell.setBorderTop(Border.NO_BORDER);
	    if (sideBorder)
	    	 cell.setBorderRight(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	 cell.setBorderRight(Border.NO_BORDER);	    
	    return cell;
	}
	
	public Cell getCell(String text, TextAlignment alignment, PdfFont font, boolean sideBorder, boolean bottomBorder) {
	    Cell cell = new Cell().add(new Paragraph(text).setFont(font));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//	    cell.setBorder(Border.NO_BORDER);
	    if (bottomBorder)
	    	cell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderBottom(Border.NO_BORDER);
	    if (sideBorder)
	    	cell.setBorderLeft(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderLeft(Border.NO_BORDER);
	    cell.setBorderTop(Border.NO_BORDER);
	    if (sideBorder)
	    	 cell.setBorderRight(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	 cell.setBorderRight(Border.NO_BORDER);	    
	    return cell;
	}
	
	public Cell getCell(String text, TextAlignment alignment, PdfFont font, float fontSize, boolean sideBorder, boolean bottomBorder) {
	    Cell cell = new Cell().add(new Paragraph(text).setFont(font).setFontSize(fontSize));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//	    cell.setBorder(Border.NO_BORDER);
	    if (bottomBorder)
	    	cell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderBottom(Border.NO_BORDER);
	    if (sideBorder)
	    	cell.setBorderLeft(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderLeft(Border.NO_BORDER);
	    cell.setBorderTop(Border.NO_BORDER);
	    if (sideBorder)
	    	 cell.setBorderRight(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	 cell.setBorderRight(Border.NO_BORDER);
	    //cell.setBorderRight(Border.NO_BORDER);
	    return cell;
	}
	
	private void createHeader(Table scoreTable, int runsNumber) {
		scoreTable.addHeaderCell(getCell("Lokata", TextAlignment.CENTER, font, 2, 1, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.addHeaderCell(getCell("Numer Startowy", TextAlignment.CENTER, font, 2, 1, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.addHeaderCell(getCell("Imię i Nazwisko", TextAlignment.CENTER, font, 2, 1, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.addHeaderCell(getCell("Klub", TextAlignment.CENTER, font, 2, 1, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.addHeaderCell(getCell("Czasy ślizgów", TextAlignment.CENTER, font, 1, runsNumber, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.startNewRow();
		for (int i = 0; i < runsNumber; i++)
			scoreTable.addHeaderCell(getCell("Pkt 2", TextAlignment.CENTER, font, tableHeaderFontSize,  true, true));
	}
	
	public void generate() {
		
	}
}
