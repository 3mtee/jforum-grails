import org.apache.catalina.loader.WebappLoader
import org.codehaus.groovy.grails.commons.ConfigurationHolder

eventConfigureTomcat = {tomcat ->
    println "### Starting load of custom application"
    def contextRoot = ConfigurationHolder.config.grails.jforum.contextRoot
    def buildroot= ConfigurationHolder.config.grails.jforum.build.path
    def webroot  = ConfigurationHolder.config.grails.jforum.web.root
    println(webroot)
    File appDir = new File(webroot);
    println(appDir)
    println(appDir.absolutePath)
    context = tomcat.addWebapp(contextRoot, appDir.getAbsolutePath());
    context.reloadable = true

    WebappLoader loader = new WebappLoader(tomcat.class.classLoader)

    loader.addRepository(new File(buildroot).toURI().toURL().toString());
    context.loader = loader
    loader.container = context

    println "### Ending load of custom application"
}
