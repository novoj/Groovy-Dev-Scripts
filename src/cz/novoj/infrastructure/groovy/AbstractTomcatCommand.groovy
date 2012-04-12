package cz.novoj.infrastructure.groovy

/**
* Description
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: AbstractTomcatCommand,9.6.11 14:17 u_jno Exp $
*/
abstract class AbstractTomcatCommand implements Command {
	private static String TOMCAT_DIR = '/opt/Apache/Tomcat_#tomcatVersion#';
	private static String TOMCAT_DEFAULT_VERSION = '6.0';
	String tomcatVersion;

	static {
		if (System.getProperty("tomcatDir")) {
			TOMCAT_DIR = System.getProperty("tomcatDir")
		}
		if (System.getProperty("tomcatDefaultVersion")) {
			TOMCAT_DEFAULT_VERSION = System.getProperty("tomcatDefaultVersion")
		}
	}

	{
		tomcatVersion = TOMCAT_DEFAULT_VERSION
	}

	void setTomcatVersion(String version) {
		if (version.indexOf(".") == -1) {
			tomcatVersion = version + ".0"
		} else {
			tomcatVersion = version
		}
	}

	String getTomcatDir() {
		return getTomcatDir(tomcatVersion)
	}

	String getTomcatDir(String tomcatVersion) {
		return TOMCAT_DIR.replace('#tomcatVersion#', tomcatVersion)
	}

}
