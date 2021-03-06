package cz.novoj.infrastructure.groovy

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

/**
* Converts encoding of text based files in batch style.
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: EncodingConversion,15.6.11 7:55 u_jno Exp $
*/
class RemoveBOM extends AbstractBatchCommand {

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
				byte[] bytes = IOUtils.toByteArray(fis);
				if (bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65) {
					byte[] updatedBytes = new byte[bytes.length-3];
					System.arraycopy(bytes, 3, updatedBytes, 0, bytes.length - 3);
					fos = new BufferedOutputStream(new FileOutputStream(targetFile));
					IOUtils.write(updatedBytes, fos);
					println("Writing: " + targetFile.absolutePath);
					result++;
				}
			} finally {
				IOUtils.closeQuietly(fis);
				IOUtils.closeQuietly(fos);
			}
		}
		println "Converted ${result} file(s)."
	}

}
