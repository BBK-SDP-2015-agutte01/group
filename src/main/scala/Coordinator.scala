import akka.actor.{ActorRef, Props, Actor}

object Coordinator {
  // Boolean flag for shutting down ActorSystem
  var finished = false

  def init(im: Image, of: String) = {
    image = im
    outfile = of
    waiting = im.width * im.height
  }

  // Number of pixels we're waiting for to be set.
  var waiting = 0
  var outfile: String = null
  var image: Image = null


  def set(x: Int, y: Int, c: Colour) = {
    image(x, y) = c
    waiting -= 1
  }

  def print = {
    if (waiting == 0) {
      image.print(outfile)
      finished = true
    }
  }
}

class Coordinator extends Actor {

  def receive = {

    case Start(scene, width, height) => {

      val actorArray = new Array[ActorRef](height)

      for (a <- actorArray.indices) {
          actorArray(a) = context.actorOf(Props[Calculator], "row" + a)
      }

      for (a <- actorArray.indices) {
        actorArray(a) ! Calculate(scene, width, a)
      }
    }

    case Set(results) =>
      
      for (r <- results) {
        // r is a tuple of (Int, Int, Colour)
        Coordinator.set(r._1, r._2, r._3)
      }
      Coordinator.print
  }
}

case class Start(scene: Scene, width: Int, height: Int)
case class Calculate(scene: Scene, width: Int, row: Int)
case class Set(results: List[(Int, Int, Colour)])
