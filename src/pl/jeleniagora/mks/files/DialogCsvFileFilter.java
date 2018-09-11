package pl.jeleniagora.mks.files;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

public class DialogCsvFileFilter extends FileFilter {

	@Override
	public boolean accept(File arg0) {
		if (arg0.isDirectory())
			return true;
		
		String e = FilenameUtils.getExtension(arg0.getName());
		
		if (e.equals("csv") || e.equals("CSV"))
			return true;
		
		return false;	
	}

	@Override
	public String getDescription() {
		return "CSV";
	}

}
