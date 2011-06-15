package cz.novoj.infrastructure.groovy

/**
* Description
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: AbstractBatchCommand,15.6.11 23:56 u_jno Exp $
*/
abstract class AbstractBatchCommand implements Command {
	String sourceEncoding;
	String targetEncoding = "utf-8";
	String sourceDirectory;
	String targetDirectory;
	String[] extensions = ["java","properties","txt","sql","ftl","vm"];

	public String getSourceDirectory() {
		if (sourceDirectory == null) {
			return System.getProperty("currentDir")
		}
		return sourceDirectory
	}

	public String getTargetDirectory() {
		if (targetDirectory == null) {
			return System.getProperty("currentDir")
		}
		return targetDirectory
	}

}
