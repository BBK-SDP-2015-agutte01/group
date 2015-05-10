
import akka.actor.{Actor, Props}

// TODO
//
// Make this an actor and write a message handler for at least the
// set method.
//
class Coordinator(val image: Image, val outfile: String) extends Actor {
  var waiting = image.width * image.height

  def receive = {
    case (x: Int, y: Int, c: Colour) => {
      set(x, y, c)
      if (waiting <= 0) {
        print
        context.system.shutdown()
      }
    }
    case (row: Int, height: Int, width: Int, scene: Scene) => {
      val tracer = context.actorOf(Props(new Tracer(scene, height, width)), "tracer" + row)
      tracer ! row
    }
    case _ => println("Cannot understand message.")
  }

  // Number of pixels we're waiting for to be set.
  private def set(x: Int, y: Int, c: Colour) = {
    image(x, y) = c
    waiting -= 1
  }

  private def print = {
    assert(waiting == 0)
    image.print(outfile)
  }
}

