package cz.novoj.infrastructure.groovy
/**
* Description
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: RunTomcat,15.8.11 9:59 u_jno Exp $
*/
class RunTomcat  extends RestoreDomain {
    def startupString

    void execute() {
        println "Using Tomcat: ${tomcatDir}"
        super.execute();

        startupString = "${tomcatDir}/bin/catalina.sh"
        println "Starting Tomcat ... ${startupString} jpda run"
        ProcessBuilder pb = new ProcessBuilder(startupString, "jpda", "run");
        pb.redirectErrorStream(true)
        Process start = pb.start()

        def reader = new BufferedReader(new InputStreamReader(start.inputStream))
        String line = reader.readLine()
        while (line != null && ! line.trim().equals("--EOF--")) {
            System.out.println (line)
            line = reader.readLine()
        }

        start.waitFor()
	}

    @Override
    public void finalize() throws Throwable {
        super.finalize()
        println "Stopping Tomcat ... ${startupString} stop"
        ProcessBuilder pend = new ProcessBuilder(startupString, "stop");
        pend.redirectErrorStream(true)
        Process endProces = pend.start()
        endProces.waitFor()
    }

}
