

(x: Int) => x * 2

val f = (x: Int) => x * 2

val shouldBeTen = f(5)

for (x <- 0 until shouldBeTen) {
    println ("hello")
}


(0 until shouldBeTen) foreach {
  x => {
    println ("goodbye")
  }
}

