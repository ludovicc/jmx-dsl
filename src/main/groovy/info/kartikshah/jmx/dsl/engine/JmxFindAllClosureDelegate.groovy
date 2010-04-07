package info.kartikshah.jmx.dsl.engine

/**
 * Created by IntelliJ IDEA.
 * User: Kartik.Shah
 * Date: Feb 12, 2010
 * Time: 11:32:33 PM
 * To change this template use File | Settings | File Templates.
 */
class JmxFindAllClosureDelegate {
  def modules

  JmxFindAllClosureDelegate(modules){
    this.modules = modules
  }

  def methodMissing(String name, args) {
   if (args.length != 1)
     throw new MissingMethodException(name, this.class, args)
   return [name, args[0]]
  }
  
  void listAttributes(){
    modules.each { m ->
      println "Object " + m.name()
      println "Attributes " + m.listAttributeNames()
    }
  }
  void listOperations(){
    modules.each { m ->
      println "Object " + m.name()
      println "Operations " + m.listOperationNames()
    }
  }

  void chart(Closure cl){
    cl.delegate = new ChartDelegate(modules)
    cl.resolveStrategy = Closure.DELEGATE_FIRST
    cl()
  }

  void collectData(Closure cl){
    cl.delegate = new CollectDataDelegate(modules)
    cl.resolveStrategy = Closure.DELEGATE_FIRST
    cl()
  }

  void collectConfig(Closure cl){
    cl.delegate = new CollectConfigDelegate(modules)
    cl.resolveStrategy = Closure.DELEGATE_FIRST
    cl()
  }

  void configure(Closure cl){
    cl.delegate = new ConfigureDelegate(modules)
    cl.resolveStrategy = Closure.DELEGATE_FIRST
    cl.delegate.apply(cl)
  }

}
