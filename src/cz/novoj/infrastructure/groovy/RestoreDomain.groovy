package cz.novoj.infrastructure.groovy

import org.apache.commons.io.FileUtils

/**
* Description
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: PrintWorkingDomain,7.6.11 14:12 u_jno Exp $
*/
class RestoreDomain extends AbstractTomcatCommand {
	String name

	void execute() {
		println "Using Tomcat: ${tomcatDir}"
		File sourceFile = findSourceFile(new File("${tomcatDir}/conf/backup/"), name)
		if (sourceFile) {
			File targetFile = new File("${tomcatDir}/conf/server.xml")
			FileUtils.copyFile(
				sourceFile,
				targetFile
			)
			println "Restored file: ${sourceFile.absolutePath}"
			FileUtils.deleteDirectory(new File("${tomcatDir}/work/Catalina"))
			println "Deleted work directory: ${tomcatDir}/work/Catalina"
		}
	}

	private File findSourceFile(File directory, String name) {
		SortedMap<Integer, String> results = new TreeMap<Integer, String>()
		File recognizedFile = null;
		int recognizedFileMatchValue = 0;
		Collection<File> files = FileUtils.listFiles(directory, ["xml"] as String[], true)
		if (name) {
			files.each {
				String fileName = it.name.toLowerCase()
				String lookUpName = name.toLowerCase()
				String coreFileName = fileName.substring("server-".length(), fileName.size() - 4)

				int similarity = coreFileName.chars.toList().intersect(lookUpName.chars.toList()).size()
				int nameMatchValue = 0

				if (coreFileName == lookUpName) {
					recognizedFile = it
					nameMatchValue = 1000;
					recognizedFileMatchValue = nameMatchValue
				} else if (coreFileName.startsWith(lookUpName)) {
					nameMatchValue = lookUpName.length() * 3;
					if (lookUpName.length() * 3 > recognizedFileMatchValue) {
						recognizedFile = it
						recognizedFileMatchValue = nameMatchValue
					}
				} else if (coreFileName.contains(lookUpName)) {
					nameMatchValue = lookUpName.length() * 2
					if (lookUpName.length() * 2 > recognizedFileMatchValue) {
						recognizedFile = it
						recognizedFileMatchValue = nameMatchValue
					}
				} else {
					nameMatchValue = similarity
					if (similarity > recognizedFileMatchValue) {
						recognizedFile = it
						recognizedFileMatchValue = similarity
					}
				}

				int matchValue = 0;
				int recognizedChars = 0;
				coreFileName.split("[-_\\.]").each {
					if (it.charAt(0) == name.charAt(recognizedChars)) {
						recognizedChars++;
						matchValue += it.length()
					}
				}
				if (matchValue > recognizedFileMatchValue && recognizedChars == lookUpName.length()) {
					recognizedFile = it
					recognizedFileMatchValue = matchValue
				} else {
					matchValue = 0
				}

				if (nameMatchValue > 0 || matchValue > 0) {
					int finalMatchValue = nameMatchValue > matchValue ? nameMatchValue : matchValue
					results[finalMatchValue] = results[finalMatchValue] ? "${results[finalMatchValue]}, ${fileName}" : fileName
				}
			}
		}
		if (recognizedFile == null) {
			println "No saved configuration found for name: " + (name ? name : "(empty name)")
			println "There are these saved configurations to restore:"
			files.each {
				println it.name
			}
		} else {
			println "Found these matching files:\n---------------------------------------------------"
			results.each {
				k, v -> println "${k} pts. : ${v}"
			}
			println "---------------------------------------------------"
		}
		return recognizedFile
	}

}
