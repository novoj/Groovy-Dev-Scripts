package cz.novoj.infrastructure.groovy

/**
* Description
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: PrintWorkingDomain,7.6.11 14:12 u_jno Exp $
*/
class TableNameCaseChange implements Command {
	String fileName

	void execute() {
        int result = 0;
        File file = new File(fileName);
        File targetFile = new File(fileName + ".replaced");
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        def resultText = file.text.replaceAll(/`(t_.+?)`/, { result++; it[0].toUpperCase()})
        resultText = resultText.replaceAll(/`(v_.+?)`/, { result++; it[0].toUpperCase()})
        targetFile.write(resultText);
        println "Converted ${result} in file ${fileName}."
	}

}
