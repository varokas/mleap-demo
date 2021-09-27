import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import ml.combust.bundle.BundleFile
import ml.combust.mleap.runtime.MleapSupport.MleapBundleFileOps
import org.slf4j.LoggerFactory
import resource.managed

class LambdaHandler() extends RequestHandler[java.util.Map[String, String], String] {
  val logger = LoggerFactory.getLogger(classOf[LambdaHandler])

  val zipBundle = (for(bundle <- managed(BundleFile("jar:file:/models/airbnb.lr.zip"))) yield {
    bundle.loadMleapBundle().get
  }).opt.get

  override def handleRequest(event: java.util.Map[String, String], context: Context): String = {
    logger.info(s"ENVIRONMENT VARIABLES: ${System.getenv()}\n")
    logger.info(s"CONTEXT: ${context}\n")

    logger.info(s"EVENT: ${event}\n")

    "Hello from Scala!"
  }
}