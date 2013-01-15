package cz.novoj.infrastructure.groovy

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

import java.util.regex.Pattern

/**
* Copies files in deep directory structure by RegEx.
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: EncodingConversion,15.6.11 7:55 u_jno Exp $
*/
class RegExCopy extends AbstractBatchCommand {
    String regex;
    String replace;
    boolean countOnly = false;

    public Pattern getRegularExpression() {
        return Pattern.compile(regex)
    }

	void execute() {
		int result = 0;
		String sourceDirectory = getSourceDirectory()
		File sourceDirectoryFile = new File(sourceDirectory);
		int sourceDirectoryIndex = sourceDirectoryFile.absolutePath.length();
        def expression = getRegularExpression()
		Iterator it = FileUtils.iterateFiles(
				new File(this.sourceDirectory),
				extensions,
				true
		);
		while(it.hasNext()) {
			File file = (File)it.next();
            if (expression.matcher(file.name).matches()) {
                String targetFileRelativePath = file.absolutePath.substring(sourceDirectoryIndex);
                String targetDirectory = getTargetDirectory()
                String targetFilePath = targetDirectory + targetFileRelativePath
                if (replace != null) {
                    targetFilePath = targetFilePath.substring(0, targetFilePath.length() - file.name.length())
                    targetFilePath = targetFilePath + file.name.replaceAll(expression, replace)
                }
                if (countOnly) {
                    println(targetFilePath)
                    result++
                } else {
                    File targetFile = new File(targetFilePath);
                    targetFile.parentFile.mkdirs();
                    if (!targetFile.exists()) {
                        targetFile.createNewFile();
                    }
                    InputStream fis = new BufferedInputStream(new FileInputStream(file));
                    OutputStream fos = null;
                    try {
                        byte[] bytes = IOUtils.toByteArray(fis);
                        byte[] updatedBytes = new byte[bytes.length];
                        System.arraycopy(bytes, 0, updatedBytes, 0, bytes.length);
                        fos = new BufferedOutputStream(new FileOutputStream(targetFile));
                        IOUtils.write(updatedBytes, fos);
                        println("Writing: " + targetFile.absolutePath);
                        result++
                    } finally {
                        IOUtils.closeQuietly(fis);
                        IOUtils.closeQuietly(fos);
                    }
                }
            }
		}
        if (countOnly) {
            println "Found ${result} file(s)."
        } else {
            println "Copied ${result} file(s)."
        }
	}

}
