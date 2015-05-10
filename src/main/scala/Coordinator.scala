import akka.actor.Actor

// TODO
//
// Make this an actor and write a message handler for at least the
// set method.
//

object Coordinator {
  def init(im: Image, of: String) = {
    image = im
    outfile = of
    waiting = im.width * im.height
  }

  // Number of pixels we're waiting for to be set.
  var waiting = 0
  var outfile: String = null
  var image: Image = null

  // TODO: make set a message
  def set(x: Int, y: Int, c: Colour) = {
    image(x, y) = c
    waiting -= 1
  }

  def print = {
    assert(waiting == 0)
    image.print(outfile)
  }
}

class Coordinator extends Actor {
  def receive = {
    case (x: Int, y: Int, c: Colour) => Coordinator.set(x, y, c)
    case _ => println("Cannot understand message.")
  }
}

//class CoordinatorActor extends Actor {
//  def receive = {
//    case trace => {
//val coordinator = context.actorOf(Props[Calculator], "cal")
//coordinator ! Calculate(34)
//    }
//  }
//}
//case class Start()
//case class Calculate(row: Int)
