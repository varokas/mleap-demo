import ml.combust.mleap.core.types.{StructField, _}
import ml.combust.mleap.runtime.frame.{DefaultLeapFrame, Row}
import ml.combust.mleap.runtime.serialization.FrameWriter

import java.nio.charset.StandardCharsets
import scala.util.Success


/*
Row(id=1949687, name='Delectable Victorian Flat for two', price=80.0, bedrooms=1.0,
bathrooms=1.0, room_type='Entire home/apt', square_feet=None, host_is_superhost=0.0,
state='London', cancellation_policy='moderate', security_deposit=100.0,
cleaning_fee=20.0, extra_people=10.0, number_of_reviews=8, price_per_bedroom=80.0,
review_scores_rating=94.0, instant_bookable=0.0)
 */
//continuous_features = ["bathrooms", "bedrooms", "security_deposit", "cleaning_fee", "extra_people", "number_of_reviews", "square_feet", "review_scores_rating"]
//categorical_features = ["room_type", "host_is_superhost", "cancellation_policy", "instant_bookable", "state"]

// Create a dataset to contain all of our values
// This dataset has two rows


object LeapFrame {
  // Create a schema. Returned as a Try monad to ensure that there
  // Are no duplicate field names
  val schema: StructType = StructType(StructField("bathrooms", ScalarType.Double),
    StructField("bedrooms", ScalarType.Double),
    StructField("security_deposit", ScalarType.Double),
    StructField("cleaning_fee", ScalarType.Double),
    StructField("extra_people", ScalarType.Double),
    StructField("number_of_reviews", ScalarType.Int),
    StructField("square_feet", ScalarType.Double),
    StructField("review_scores_rating", ScalarType.Double),
    StructField("room_type", ScalarType.String),
    StructField("host_is_superhost", ScalarType.Double),
    StructField("cancellation_policy", ScalarType.String),
    StructField("instant_bookable", ScalarType.Double),
    StructField("state", ScalarType.String),
  ).get

  val dataset = Seq(Row(1.0, 2.0, 100.0, 20.0, 10.0, 8, 400.0, 94.0, "Entire home/apt", 0.0, "moderate", 0.0, "London"))

  // Create a LeapFrame from the schema and dataset
  val leapFrame = DefaultLeapFrame(schema, dataset)

  def main(args: Array[String]): Unit = {
    var tryBytes = FrameWriter.apply(leapFrame).toBytes()
    tryBytes match {
      case Success(bytes) => {
        println(new String(bytes, StandardCharsets.UTF_8))
      }
      case x => {
        println(x)
      }
    }

  }
}