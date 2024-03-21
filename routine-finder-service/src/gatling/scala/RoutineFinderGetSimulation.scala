import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class RoutineFinderGetSimulation extends Simulation {

  val httpProtocol = http

  val routine_service_url = "https://routine-finder-service-dot-nimble-equator-282207.nw.r.appspot.com/api/bc/store/eb52p33wq0/customer/1/routine"

  val scn = scenario("Routine Finder Service")
    .exec(http("get_routine")
      .get(routine_service_url)
      .check(status.is(200))
      .check(substring("\"complete\":false,\"questions\""))
    ).pause(2)

    .exec(http("get_enhance_routine")
      .put(routine_service_url)
      .body(RawFileBody("./routine-finder-service/src/gatling/resources/enhanced_routine_request.json")).asJson
      .check(status.is(200))
      .check(substring("Enhanced"))
    ).pause(2)

  // *** BE CAREFUL *** RUNNING LOAD TESTS CAN INCUR COSTS ON IF YOU EXCEED THE DAILY USAGE LIMIT ***
  // *** Be sure to check the costs incurred after each run ***
  setUp(
    scn.inject(rampConcurrentUsers(1).to(50).during(10.minutes))
  ).protocols(httpProtocol)

}