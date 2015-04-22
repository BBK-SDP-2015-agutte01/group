import akka.actor.Actor

class Calculator extends Actor {
  def receive = {
    case Calculate(row) =>
  }
}
