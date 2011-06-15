package cz.novoj.infrastructure.groovy

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

/**
* Converts encoding of text based files in batch style.
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: EncodingConversion,15.6.11 7:55 u_jno Exp $
*/
class EncodingConversion extends AbstractBatchCommand {

	void execute() {
		int result = 0;
		String sourceDirectory = getSourceDirectory()
		File sourceDirectoryFile = new File(sourceDirectory);
		int sourceDirectoryIndex = sourceDirectoryFile.absolutePath.length();
		Iterator it = FileUtils.iterateFiles(
				new File(this.sourceDirectory),
				extensions,
				true
		);
		while(it.hasNext()) {
			File file = (File)it.next();
			String targetFilePath = file.absolutePath.substring(sourceDirectoryIndex);
			String targetDirectory = getTargetDirectory()
			File targetFile = new File(targetDirectory + targetFilePath);
			targetFile.parentFile.mkdirs();
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			OutputStream fos = null;
			try {
				String text = IOUtils.toString(fis, sourceEncoding);
				fos = new BufferedOutputStream(new FileOutputStream(targetFile));
				IOUtils.write(text, fos, targetEncoding);
				println("Writing: " + targetFile.absolutePath);
				result++;
			} finally {
				IOUtils.closeQuietly(fis);
				IOUtils.closeQuietly(fos);
			}
		}
		println "Converted ${result} file(s)."
	}

}
