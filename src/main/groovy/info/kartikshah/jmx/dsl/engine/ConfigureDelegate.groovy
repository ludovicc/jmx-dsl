package info.kartikshah.jmx.dsl.engine

/**
 * Created by IntelliJ IDEA.
 * User: lclaude
 * Date: 31 mars 2010
 * Time: 17:47:33
 * To change this template use File | Settings | File Templates.
 */

class ConfigureDelegate {

  def modules

  ConfigureDelegate(modules){
    this.modules = modules
  }

  def apply(Closure cl) {
    modules.each(cl)
  }
}