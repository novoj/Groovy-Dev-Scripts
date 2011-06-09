package cz.novoj.infrastructure.groovy

/**
* Description
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: PrintWorkingDomain,7.6.11 14:12 u_jno Exp $
*/
class PrintWorkingDomain extends AbstractTomcatCommand {

	void execute() {
		println "Using Tomcat: ${tomcatDir}"
		File tomcatCfg = new File("${tomcatDir}/conf/server.xml")
		String cfgContents = tomcatCfg.getText("utf-8")
		def parsedCfg = new XmlParser().parseText(cfgContents)
		println "Current project set to: ${parsedCfg.Service.Engine.Host.'@appBase'}"
	}

}
