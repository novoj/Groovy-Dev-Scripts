#!/opt/GroovyServer/bin/gscript

package cz.novoj.infrastructure.groovy

import org.springframework.beans.BeanWrapper
import org.springframework.beans.BeanWrapperImpl
import org.springframework.beans.BeansException

/**
* Description
*
* @author Jan Novotný (novotny@fg.cz), FG Forrest a.s. (c) 2010
* @version $Id: InfrastructureScriptInvoker,7.6.11 14:31 u_jno Exp $
*/
class InfrastructureScriptInvoker {
	private static Map<String, Class> commands = new HashMap<String, Class>();
	private static SortedMap<String, List<String>> commandIndex = new TreeMap<String, String[]>()

	static {
		registerCommand(PrintWorkingDomain.class)
		registerCommand(SaveWorkingDomain.class)
		registerCommand(RestoreDomain.class)
		registerCommand(EncodingConversion.class)
	}

	private static void registerCommand(Class command) {
		registerCommand(command.simpleName, command)
		registerCommand(command.simpleName[0], command)
		String cCase = "";
		command.simpleName.chars.each {
			if (it.isUpperCase()) {
				cCase += it;
			}
		}
		registerCommand(cCase, command);
	}

	private static void registerCommand(String name, Class command) {
		if(!commands.containsKey(name.toLowerCase())) {
			commands[name.toLowerCase()] = command
			List<String> aliases = commandIndex[command.simpleName];
			if (aliases == null) {
				aliases = new ArrayList<String>()
				commandIndex[command.simpleName] = aliases
			}
			aliases.add(name)
		}
	}

	private static void printHelp() {
		println "Usage: invoker commandName [arguments]"
		println "Registered commands:"
		commandIndex.each {
			k,v ->
			BeanWrapper wrapper = new BeanWrapperImpl(commands[k.toLowerCase()].newInstance())
			String arguments = "";
			wrapper.propertyDescriptors.each {
				if (it.name != "class" && it.name != "metaClass" && it.writeMethod != null) {
					arguments += it.name + " [${wrapper.getPropertyValue(it.name)}], "
				}
			}
			println "${k}: " + v + " arguments: " + arguments.substring(0, arguments.length() - 2)
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			printHelp()
		} else {
			int index = 0
			if (args[0].endsWith("InfrastructureScriptInvoker.groovy")) {
				index = 1
			}
			String cmdName = args[index]
			Class commandClass = commands[cmdName.toLowerCase()]
			if (commandClass != null) {
				Command command = commandClass.newInstance()
				BeanWrapper wrapper = new BeanWrapperImpl(command)
				int namelessArgs = 0;
				Map<String, String> params = [:]
				for(int i = index + 1; i < args.length; i++) {
					namelessArgs = getParameters(command, wrapper, params, args[i], namelessArgs)
				}
				try {
					params.each {
						k, v -> wrapper.setPropertyValue(k, v)
					}
				} catch (BeansException ex) {
					println "Error setting property: ${ex.localizedMessage}"
					println "Avalable command properties: " + wrapper.propertyDescriptors.each { it + ", " }
				}
				command.execute()
			} else {
				println "Command with name ${args[0]} was not found"
				printHelp()
			}
		}
	}

	private static int getParameters(Command command, BeanWrapper commandWrapper, Map<String, String> params, String argument, int namelessArgs) {
		String[] argItems = argument.split(",:")
		argItems.each {
			String[] argTuple = it.split("=")
			if (argTuple.length == 2) {
				params[argTuple[0]] = argTuple[1]
			} else {
				List availableProperties = commandWrapper.propertyDescriptors.findAll {it.name != "class" && it.name != "metaClass" && it.writeMethod != null}
				commandWrapper.setPropertyValue(
					availableProperties[namelessArgs].name,
					it
				)
				namelessArgs++
			}
		}
		return namelessArgs;
	}


}
