package info.kartikshah.jmx.dsl.engine

import javax.management.remote.JMXConnector
import javax.management.remote.JMXConnectorFactory
import javax.management.remote.JMXServiceURL as JmxUrl
import javax.management.ObjectName
import javax.naming.Context

/**
 * Created by IntelliJ IDEA.
 * User: Kartik.Shah
 * Date: Feb 12, 2010
 * Time: 11:16:46 PM
 * To change this template use File | Settings | File Templates.
 */
class JmxServerClosureDelegate {
  def serverUrl
  def env
  @Lazy
  def server = {
    env[Context.SECURITY_PRINCIPAL] = this.username
    env[Context.SECURITY_CREDENTIALS] = this.password
    env[JMXConnector.CREDENTIALS] = [this.username, this.password] as java.lang.String[]
    JMXConnectorFactory.connect(new JmxUrl(serverUrl),env).MBeanServerConnection
  }()
  def name

  def username
  def password
  
  JmxServerClosureDelegate(serverUrl, env){
    this.serverUrl = serverUrl
    this.env = env.clone()
  }

  def methodMissing(String name, args) {
    if (args.length != 1)
       throw new MissingMethodException(name, this.class, args)
    return [name, args[0]]
  }

  void query(param){
    def (objectName, cl) = param
    def query = new ObjectName(objectName)
    String[] allNames = server.queryNames(query, null)

    cl.delegate = new JmxQueryClosureDelegate(allNames, server)
    cl.resolveStrategy = Closure.DELEGATE_FIRST
    cl()
  }
}
