package cz.novoj.infrastructure.groovy

/**
* Description
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: RunTomcat,15.8.11 9:59 u_jno Exp $
*/
class RunTomcat  extends AbstractTomcatCommand {

	void execute() {
		println "Using Tomcat: ${tomcatDir}"
		Runtime run = Runtime.runtime;
		Process child = run.exec("/bin/bash");
		BufferedWriter outCommand = new BufferedWriter(new OutputStreamWriter(child.outputStream));
		//TODO  InputStream console = child.inputStream;
		outCommand.writeLine("cd ${tomcatDir}/bin");
		outCommand.writeLine("./catalina.sh jpda run");
		outCommand.flush();
		int result = child.waitFor();
		println "Tomcat finished with code: ${result}"
	}

}
