package info.kartikshah.jmx.dsl

import info.kartikshah.jmx.dsl.engine.CollectConfigDelegate

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
          resetOnRead = [true, true]
          start()
        }
      }
      findAll "type=Compilation" {
        collectData {
          category='JVMCompilation'
          labels = ['Total compilation time']
          attributes = {m -> [m.TotalCompilationTime]}
          start()
        }
      }
      findAll "type=GarbageCollector" {
        collectData {
          category='JVMGarbageCollector'
          labels = ['Collection count', 'Collection time', 'Last GC start time', 'Last GC duration']
          attributes = {m -> [m.CollectionCount, m.CollectionTime, m.LastGCInfo.startTime, m.LastGCInfo.duration]}
          start()
        }
      }
      findAll "type=Memory" {
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
          start()
        }
      }
      findAll "type=MemoryPool" {
        collectData {
          category='JVMMemoryPool'
          labels = ['Name', 'Type', 'Memory committed', 'Memory max', 'Memory used']
          attributes = {m -> [m.Name, m.Type, m.Usage.committed, m.Usage.max, m.Usage.used]}
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