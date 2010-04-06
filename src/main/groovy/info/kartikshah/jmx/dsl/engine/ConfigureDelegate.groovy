package info.kartikshah.jmx.dsl.engine

/**
 * Created by IntelliJ IDEA.
 * User: lclaude
 * Date: 31 mars 2010
 * Time: 17:47:33
 * To change this template use File | Settings | File Templates.
 */

class ConfigureDelegate {
  static def mode
  //static Timer timer = new Timer()

  def modules

  def category
  def attributes
  def labels
  def resetOnRead

  CollectDataDelegate(modules){
    this.modules = modules
  }

  def start(){
    if (mode == 'spec')
      printSpecs()
    //else
    // collectData periodically
  }

  def collectData(){
    modules.each{ m ->
      def dsCall = attributes.call(m)
      newDataset.addValue dsCall[0], 0, dsCall[1]
    }
  }

  def printSpecs(){
    println category
    for (label in labels) {
      println '\t' + label
    }
  }
}