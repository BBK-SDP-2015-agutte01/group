import akka.actor.Actor

class Calculator extends Actor {

  def receive = {
    case Calculate(scene: Scene, width: Int, row: Int) =>

      var resultList = List[(Int, Int, Colour)]()

      for (x <- 0 until width) {
        resultList = resultList :+ (x, row, scene.traceImage(x, row))
      }

      sender ! Set(resultList)
  }
}
