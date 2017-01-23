object YarnApp extends App {

  import org.apache.hadoop.yarn.conf._

  val conf: YarnConfiguration = new YarnConfiguration()

  import org.apache.hadoop.yarn.client.api.YarnClient

  val yarnClient = YarnClient.createYarnClient()
  yarnClient.init(conf)
  yarnClient.start()

  import org.apache.hadoop.yarn.client.api._

  val app: YarnClientApplication = yarnClient.createApplication()

  import org.apache.hadoop.yarn.api.records.ContainerLaunchContext
  import org.apache.hadoop.yarn.util.Records

  val amContainer = Records.newRecord(classOf[ContainerLaunchContext])

  import org.apache.hadoop.yarn.api.records._

  val executorMemory = 1024
  val memoryOverhead = 384
  val executorCores = 2
  val capability = Resource.newInstance(executorMemory + memoryOverhead, executorCores)

  val appContext = app.getApplicationSubmissionContext
  appContext.setApplicationName(s"YARN Scala App")
  appContext.setAMContainerSpec(amContainer)
  appContext.setResource(capability)

  import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest

  val amClient: AMRMClient[ContainerRequest] = AMRMClient.createAMRMClient()
  try {
    amClient.init(conf)
    amClient.start()

    val appId = appContext.getApplicationId
    println(s"Submitting application $appId")
    yarnClient.submitApplication(appContext)

    val appReport = yarnClient.getApplicationReport(appId)
    println(appReport.getYarnApplicationState)
  } finally {
    amClient.stop()
  }
}
