package info.kartikshah.jmx.dsl.engine

/**
 * Created by IntelliJ IDEA.
 * User: lclaude
 * Date: 6 avr. 2010
 * Time: 18:58:58
 * To change this template use File | Settings | File Templates.
 */

// Add to ~/.groovy/grapeConfig.xml
// <ibiblio name="openNMS" root="http://repo.opennms.org/maven2/" m2compatible="true"/>

@Grab(group='org.jrobin', module='jrobin', version='1.5.9')
class RRDDataPipeline extends DataPipeline {

  def rddFiles = [:]

  def config(def category, def labels, def configuration) {
    def rddConfig = configuration['rdd']
    if (rddConfig) {
      
    }
    next?.config(category, labels, configuration)
  }

  def processData(def category, def labels, def data) {
    
      next?.processData(category, labels, data)
  }

}