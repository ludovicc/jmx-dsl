package info.kartikshah.jmx.dsl.engine

/**
 * Created by IntelliJ IDEA.
 * User: lclaude
 * Date: 31 mars 2010
 * Time: 17:47:33
 * To change this template use File | Settings | File Templates.
 */
class CollectDataDelegate {
  static def mode
  static Timer timer = new Timer()

  def modules

  def category
  def attributes
  def labels
  def config
  def frequency = 1

  CollectDataDelegate(modules){
    this.modules = modules
  }

  def start(){
    if (mode == 'spec')
      printSpecs()
    else {
      DataPipeline.entry.config(category, labels, config)
      // collect data periodically
      def task = new TimerTask() {
           void run() {
             collectData()
           }
      }
      timer.scheduleAtFixedRate task, frequency * 1000, frequency * 1000
    }
  }

  def collectData(){
    println 'Collecting data'
    modules.each{ m ->
      def values = attributes.call(m)
      DataPipeline.entry.processData category, labels, values
    }
  }

  def printSpecs(){
    println category
    for (label in labels) {
      println '\t' + label 
    }
  }
}
