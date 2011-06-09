package cz.novoj.infrastructure.groovy

import java.util.regex.Matcher
import java.util.regex.Pattern
import org.apache.commons.io.FileUtils

/**
* Description
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: PrintWorkingDomain,7.6.11 14:12 u_jno Exp $
*/
class SaveWorkingDomain extends AbstractTomcatCommand {
	private static final Pattern NAME_GUESS = Pattern.compile(/.+\/(.+?)\/?/)
	String name

	String getName() {
		File tomcatCfg = new File("${tomcatDir}/conf/server.xml")
		if (name == null) {
			String cfgContents = tomcatCfg.getText("utf-8")
			def parsedCfg = new XmlParser().parseText(cfgContents)
			Matcher matcher = NAME_GUESS.matcher(parsedCfg.Service.Engine.Host.'@appBase')
			if (matcher.matches()) {
				name = matcher.group(1)
			}
		}
		return name
	}

	void execute() {
		println "Using Tomcat: ${tomcatDir}"
		File tomcatCfg = new File("${tomcatDir}/conf/server.xml")
		File targetFile = new File("${tomcatDir}/conf/backup/server-${getName()}.xml")
		FileUtils.copyFile(
			tomcatCfg,
			targetFile
		)
		println "Saved file: ${targetFile.absolutePath}"
	}

}
