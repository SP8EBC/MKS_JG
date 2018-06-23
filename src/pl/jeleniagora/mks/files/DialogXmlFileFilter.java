package pl.jeleniagora.mks.files;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

import org.apache.commons.io.FilenameUtils;

/**
 * Klasa służąca jako filtr dla okien zapisu i odczytu plików 
 * @author mateusz
 *
 */
public class DialogXmlFileFilter extends FileFilter {

	@Override
	public boolean accept(File arg0) {
		if (arg0.isDirectory())
			return true;
		
		String e = FilenameUtils.getExtension(arg0.getName());
		
		if (e.equals("xml") || e.equals("XML"))
			return true;
		
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "XML";
	}

}
