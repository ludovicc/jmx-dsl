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
  def lastSampleTimes = [:]

  def RRDDataPipeline() {
    Runtime.addShutdownHook {
      println "Closing RRD databases..."
      close()
    }
  }

  def config(def category, def labels, def configuration) {
    println "Configuring rdd for $category"
    rootDir.mkdirs()
    File rrdFile = new File(rootDir, category + '.rrd')
    if (rrdFile.exists()) rrdFile.delete()
    def rrdDef = new RrdDef(rrdFile.toString())
    rrdDef.startTime = System.currentTimeMillis() / 1000
    lastSampleTimes[category] = rrdDef.startTime

    if (configuration) {
      def rrdConfig = configuration['rrd']
      rrdConfig?.call(rrdDef)
      rrdDef.validate()
      rrdDbs[category] = new RrdDb(rrdDef)
    }
    next?.config(category, labels, configuration)
  }

  def processData(def category, def labels, def values) {
    println "Process data for $category"
    RrdDb rrd = rrdDbs[category]
    if (rrd) {
      def sample = rrd.createSample()
      def lastSampleTime = lastSampleTimes[category]
      sample.time = System.currentTimeMillis() / 1000
      if (sample.time <= lastSampleTime) sample.time = lastSampleTime + 1
      lastSampleTimes[category] = sample.time
      def i = 0
      values.each {def value ->
        def label = labels[i]
        println "Received $label = $value"
        sample.setValue(i++, value)
      }
      sample.update()
    }
    next?.processData(category, labels, values)
  }

  def close() {
    try {
      rrdDbs.values().each {
        it.close()
      }
    } catch (Exception e) {
      e.printStackTrace()
    }
    println "Closed all RRD databases"
  }
}