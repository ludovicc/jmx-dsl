package info.kartikshah.jmx.dsl.engine

import javax.management.remote.JMXConnectorFactory

/**
 * Created by IntelliJ IDEA.
 * User: Kartik.Shah
 * Date: Feb 12, 2010
 * Time: 11:13:51 PM
 * To change this template use File | Settings | File Templates.
 */
class JmxClosureDelegate {

  def initialContextFactory
  def protocolProvider
  
  def methodMissing(String name, args) {
    if (args.length != 1)
      throw new MissingMethodException(name, this.class, args)
    return [name, args[0]]
  }

  void server(param){
    def (serverUrl, cl) = param
    def env = [:]
    env["java.naming.factory.initial"] = this.initialContextFactory
    env[JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES] = this.protocolProvider
    cl.delegate = new JmxServerClosureDelegate(serverUrl, env)
    cl.resolveStrategy = Closure.DELEGATE_FIRST
    cl()
  }
}
