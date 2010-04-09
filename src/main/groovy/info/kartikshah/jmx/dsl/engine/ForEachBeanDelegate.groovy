package info.kartikshah.jmx.dsl.engine

/**
 * Created by IntelliJ IDEA.
 * User: lclaude
 * Date: 8 avr. 2010
 * Time: 15:02:38
 * To change this template use File | Settings | File Templates.
 */
class ForEachBeanDelegate {

  def module

  def ForEachBeanDelegate(def module) {
    this.module = module
  }

  void collectData(Closure cl){
    cl.delegate = new CollectDataDelegate([module])
    cl.resolveStrategy = Closure.DELEGATE_FIRST
    cl()
  }

  void collectConfig(Closure cl){
    cl.delegate = new CollectConfigDelegate([module])
    cl.resolveStrategy = Closure.DELEGATE_FIRST
    cl()
  }

}
