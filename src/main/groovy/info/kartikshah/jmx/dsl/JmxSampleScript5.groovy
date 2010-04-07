package info.kartikshah.jmx.dsl

import info.kartikshah.jmx.dsl.engine.CollectConfigDelegate
import info.kartikshah.jmx.dsl.engine.DataPipeline
import info.kartikshah.jmx.dsl.engine.RRDDataPipeline

/**
 * Created by IntelliJ IDEA.
 * User: lclaude
 * Date: 6 avr. 2010
 * Time: 18:06:04
 * To change this template use File | Settings | File Templates.
 */

// Uncomment to view only the
//CollectConfigDelegate.mode='spec'
//CollectDataDelegate.mode='spec'

CollectConfigDelegate.root = new File('config')
DataPipeline.entry.next = new RRDDataPipeline()

// Collect data from GlassFish

jmx {
  server "service:jmx:rmi:///jndi/rmi://localhost:4848/jmxrmi" {
    username = "admin"
    password = "adminadmin"
    name = 'MyApp'

    query "java.lang:*" {
      findAll "type=OperatingSystem" {
        collectConfig {
          category='OperatingSystemConfig'
          labels = ['Arch', 'Name', 'Version', 'Available processors', 'Total physical memory', 'Total swap space', 'Max nb of file descriptors']
          attributes = {m -> [m.Arch, m.Name, m.Version, m.AvailableProcessors, m.TotalPhysicalMemorySize, m.TotalSwapSpaceSize, m.MaxFileDescriptorCount]}
          start()
        }
      }
      findAll "type=Runtine" {
        collectConfig {
          category='JVMProcess'
          labels = ["PID"]
          attributes = {m -> [m.Name =~ /(\d+)@.*/ [0][1] ]}
          start()
        }
      }
      findAll "type=JVMClassLoading" {
        collectData {
          category='ClassLoading'
          labels = ['Loaded classes', 'Unloaded classes']
          attributes = {m -> [m.LoadedClassesCount, m.UnloadedClassesCount]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "Loaded_classes", "COUNTER", 10, 0, Double.NaN
            rrdDef.addDatasource "Unloaded_classes", "COUNTER", 10, 0, Double.NaN
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
      findAll "type=Compilation" {
        collectData {
          category='JVMCompilation'
          labels = ['Total compilation time']
          attributes = {m -> [m.TotalCompilationTime]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "Compilation_time", "COUNTER", 10, 0, Double.NaN
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
      findAll "type=GarbageCollector" {
        collectData {
          category='JVMGarbageCollector'
          labels = ['Collection count', 'Collection time', 'Last GC start time', 'Last GC duration']
          attributes = {m -> [m.CollectionCount, m.CollectionTime, m.LastGcInfo.startTime, m.LastGcInfo.duration]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "GC_collection_count", "COUNTER", 10, 0, Double.NaN
            rrdDef.addDatasource "GC_collection_time", "COUNTER", 10, 0, Double.NaN
            rrdDef.addDatasource "GC_last_start_time", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "GC_last_duration", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
      findAllEndsWith "type=Memory" {
        collectData {
          category='JVMMemory'
          labels = ['Heap memory committed', 'Heap memory max', 'Heap memory used',
                  'Non heap memory committed', 'Non heap memory max', 'Non heap memory used',
                  'Nb of objects pending finalization'
          ]
          attributes = {m -> [m.HeapMemoryUsage.committed, m.HeapMemoryUsage.max, m.HeapMemoryUsage.used,
                  m.NonHeapMemoryUsage.committed, m.NonHeapMemoryUsage.max, m.NonHeapMemoryUsage.used,
                  m.ObjectPendingFinalizationCount
          ]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "Heap_memory_committed", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Heap_memory_max", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Heap_memory_used", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Non_heap_memory_committed", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Non_heap_memory_max", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Non_heap_memory_used", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Objects_pending_finalization", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
      findAll "type=MemoryPool,name=Code Cache" {
        collectData {
          category='JVMMemoryPool_CodeCache'
          labels = ['Memory committed', 'Memory max', 'Memory used']
          attributes = {m -> [m.Usage.committed, m.Usage.max, m.Usage.used]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "Memory_committed", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Memory_max", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Memory_used", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
      findAll "type=MemoryPool,name=PS Eden Space" {
        collectData {
          category='JVMMemoryPool_EdenSpace'
          labels = ['Memory committed', 'Memory max', 'Memory used']
          attributes = {m -> [m.Usage.committed, m.Usage.max, m.Usage.used]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "Memory_committed", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Memory_max", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Memory_used", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
      findAll "type=MemoryPool,name=PS Old Gen" {
        collectData {
          category='JVMMemoryPool_OldGen'
          labels = ['Memory committed', 'Memory max', 'Memory used']
          attributes = {m -> [m.Usage.committed, m.Usage.max, m.Usage.used]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "Memory_committed", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Memory_max", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Memory_used", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
      findAll "type=MemoryPool,name=PS Perm Gen" {
        collectData {
          category='JVMMemoryPool_PermGen'
          labels = ['Memory committed', 'Memory max', 'Memory used']
          attributes = {m -> [m.Usage.committed, m.Usage.max, m.Usage.used]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "Memory_committed", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Memory_max", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Memory_used", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
      findAll "type=OperatingSystem" {
        collectData {
          category='OperatingSystem'
          labels = ['Committed virtual memory', 'Free physical memory', 'Free swap space',
                  'Nb of open file descriptors', 'System load average'
          ]
          attributes = {m -> [m.CommittedVirtualMemorySize, m.FreePhysicalMemorySize, m.FreeSwapSpaceSize,
                  m.OpenFileDescriptorCount, m.SystemLoadAverage
          ]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "Virtual_memory_committed", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Physical_memory_free", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Swap_free", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "Open_file_descriptors", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addDatasource "System_load", "ABSOLUTE", 10, 0, 100
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
    }

    query "amx:*" {
      findAll "j2eeType=X-ModuleMonitoringLevelsConfig" {
        configure { m ->
          m.ConnectorConnectionPool = 'LOW'
          m.ConnectorService = 'LOW'
          m.EJBContainer = 'LOW'
          m.HTTPService = 'LOW'
          m.JDBCConnectionPool = 'LOW'
          // m.JMSService = 'OFF' - linked to ConnectorService
          m.JVM = 'LOW'
          m.ORB = 'OFF'
          m.ThreadPool = 'OFF'
          m.TransactionService = 'LOW'
          m.WebContainer = 'LOW'
        }
      }
      findAll "j2eeType=JVM" {
        collectConfig {
          category='JVMConfig'
          labels = ["JVM vendor", "JVM version"]
          attributes = {m -> [m.javaVendor, m.javaVersion]}
          start()
        }
      }
      findAll "j2eeType=X-ConfigConfig" {
        collectConfig {
          category='JVMConfig'
          labels = ["System properties"]
          attributes = {m -> [m.SystemProperties]}
          start()
        }
      }
      findAll "j2eeType=X-JVMMonitor" {
        collectData {
          category='JVM'
          labels = ["Heap size"
          ]
          attributes = {m -> [m.HeapSize_Current]}
          frequency = 10
          config = ['rrd': {rrdDef ->
            rrdDef.step = 10
            rrdDef.addDatasource "Heap_size", "ABSOLUTE", 10, 0, Double.NaN
            rrdDef.addArchive "AVERAGE", 0.5, 10, 720  // 2 hours of archive
          }]
          start()
        }
      }
      findAll "j2eeType=J2EEServer" {
        collectConfig {
          category='J2EEServerConfig'
          labels = ["Server version"]
          attributes = {m -> [m.serverVersion]}
          start()
        }
      }
    }
  }
}