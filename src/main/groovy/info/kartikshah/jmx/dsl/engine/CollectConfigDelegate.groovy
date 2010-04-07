package info.kartikshah.jmx.dsl.engine

/**
 * Created by IntelliJ IDEA.
 * User: lclaude
 * Date: 31 mars 2010
 * Time: 17:47:33
 * To change this template use File | Settings | File Templates.
 */

class CollectConfigDelegate {
  static def mode
  static def root = new File('.')

  def modules

  def category
  def attributes
  def labels

  CollectConfigDelegate(modules){
    this.modules = modules
  }

  def start(){
    if (mode == 'spec')
      printSpecs()
    else
     collectConfig()
  }

  def collectConfig(){
    root.mkdirs()
    def configFile = new File(root, category + '.config')
    if (configFile.exists()) configFile.delete()
    configFile.createNewFile()
    modules.each{ m ->
      def dsCall = attributes.call(m)
      for (def i=0; i<labels.size(); i++) {
        def label = labels[i]
        def value = dsCall[i]
        println "$label = $value"
        configFile << "$label = $value\n"
      }
    }
  }

  def printSpecs(){
    println "[Configuration] $category"
    for (label in labels) {
      println '\t' + label
    }
  }
}