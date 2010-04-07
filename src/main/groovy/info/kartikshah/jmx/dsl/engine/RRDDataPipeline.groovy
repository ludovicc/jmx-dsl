package info.kartikshah.jmx.dsl.engine

import org.jrobin.core.RrdDb
import org.jrobin.core.RrdDef

/**
 * Created by IntelliJ IDEA.
 * User: lclaude
 * Date: 6 avr. 2010
 * Time: 18:58:58
 * To change this template use File | Settings | File Templates.
 */

class RRDDataPipeline extends DataPipeline {

  def rootDir = new File('logs')
  def rrdDbs = [:]

  def config(def category, def labels, def configuration) {
    println "Configuring rdd for $category"
    rootDir.mkdirs()
    def rrdDef = new RrdDef(new File(rootDir, category + '.rrd').toString())
    rrdDef.startTime = System.currentTimeMillis()

    if (configuration) {
      def rrdConfig = configuration['rrd']
      rrdConfig?.call(rrdDef)
      rrdDbs[category] = new RrdDb(rrdDef)
    }
    next?.config(category, labels, configuration)
  }

  def processData(def category, def labels, def values) {
    println "Process data for $category"
    def rrd = rrdDbs[category]
    if (rrd) {
      def sample = rrd.createSample()
      sample.time = System.currentTimeMillis()
      def i = 0
      values.each {def value ->
        sample.setValue(i++, value)
      }
      sample.update()
    }
    next?.processData(category, labels, values)
  }

}