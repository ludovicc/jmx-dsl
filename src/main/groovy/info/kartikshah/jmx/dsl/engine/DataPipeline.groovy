package info.kartikshah.jmx.dsl.engine

/**
 * Created by IntelliJ IDEA.
 * User: lclaude
 * Date: 6 avr. 2010
 * Time: 14:24:25
 * To change this template use File | Settings | File Templates.
 */
class DataPipeline {
  def static entry = new DataPipeline()

  def next

  def config(def category, def labels, def configuration) {
    next?.config(category, labels, configuration)
  }

  def processData(def category, def labels, def data) {
      next?.processData(category, labels, data)
  }
}


