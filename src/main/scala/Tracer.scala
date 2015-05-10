import akka.actor.Actor

//Tracer Actor that carries out the computation for the pixels per row.
//Tracer Actor will send message back to the coordinator.

class Tracer(scene: Scene, height: Int, width: Int) extends Actor {
  val eye = Vector.origin
  val angle = 90f // viewing angle
  val frustum = (.5 * angle * math.Pi / 180).toFloat
  val cosf = math.cos(frustum)
  val sinf = math.sin(frustum)
  val ss = Trace.AntiAliasingFactor

  def receive = {
    // y being the variable in the for loop from scene.
    case y: Int => {

      for (x <- 0 until width) {

        // This loop body can be sequential.
        var colour = Colour.black

        for (dx <- 0 until ss) {
          for (dy <- 0 until ss) {

            // Create a vector to the pixel on the view plane formed when
            // the eye is at the origin and the normal is the Z-axis.
            val dir = Vector(
              (sinf * 2 * ((x + dx.toFloat / ss) / width - .5)).toFloat,
              (sinf * 2 * (height.toFloat / width) * (.5 - (y + dy.toFloat / ss) / height)).toFloat,
              cosf.toFloat).normalized

            val c = scene.trace(Ray(eye, dir)) / (ss * ss)
            colour += c
          }
        }

        if (Vector(colour.r, colour.g, colour.b).norm < 1)
          Trace.darkCount += 1
        if (Vector(colour.r, colour.g, colour.b).norm > 1)
          Trace.lightCount += 1

        Coordinator.set(x, y, colour)
      }
    }
  }
}
