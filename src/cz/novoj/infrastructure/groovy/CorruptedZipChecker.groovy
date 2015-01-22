package cz.novoj.infrastructure.groovy
import org.apache.commons.io.FileUtils

import java.util.zip.ZipException
import java.util.zip.ZipFile
/**
* Converts encoding of text based files in batch style.
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: EncodingConversion,15.6.11 7:55 u_jno Exp $
*/
class CorruptedZipChecker implements Command {
	String sourceDirectory;

	public String getSourceDirectory() {
		if (sourceDirectory == null) {
			return System.getProperty("currentDir")
		}
		return sourceDirectory
	}

	void execute() {
		int result = 0;
		int errors = 0;
		int oks = 0;
		String sourceDirectory = getSourceDirectory()
		File sourceDirectoryFile = new File(sourceDirectory);

		Iterator it = FileUtils.iterateFiles(
				sourceDirectoryFile,
				["zip", "jar", "war"] as String[],
				true
		);
		while(it.hasNext()) {
			File file = (File)it.next();

			if (isValid(file)) {
				println("OK: " + file.absolutePath.substring(sourceDirectoryFile.absolutePath.length() + 1));
				oks++
			} else {
				println("ERROR: " + file.absolutePath.substring(sourceDirectoryFile.absolutePath.length() + 1));
				errors++
			}

		}
		println "Checked ${result} file(s): ${oks} ok, ${errors} errors"
	}

	private boolean isValid(final File file) {
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(file);
			return true;
		} catch (ZipException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (zipfile != null) {
					zipfile.close();
					zipfile = null;
				}
			} catch (IOException e) {
			}
		}
	}

}
