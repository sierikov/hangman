package dev.sierikov

package object hangman {

  // Used for example can be replaced with call to external dictionary
  val words = List("zio", "scala", "work")

  val hangmanStages = List(
    """
      #   --------
      #   |      |
      #   |
      #   |
      #   |
      #   |
      #   -
      #""".stripMargin('#'),
    """
      #   --------
      #   |      |
      #   |      0
      #   |
      #   |
      #   |
      #   -
      #""".stripMargin('#'),
    """
      #   --------
      #   |      |
      #   |      0
      #   |      |
      #   |      |
      #   |
      #   -
      #""".stripMargin('#'),
    """
      #   --------
      #   |      |
      #   |      0
      #   |     \|
      #   |      |
      #   |
      #   -
      #""".stripMargin('#'),
    """
      #   --------
      #   |      |
      #   |      0
      #   |     \|/
      #   |      |
      #   |
      #   -
      #""".stripMargin('#'),
    """
      #   --------
      #   |      |
      #   |      0
      #   |     \|/
      #   |      |
      #   |     /
      #   -
      #""".stripMargin('#'),
    """
      #   --------
      #   |      |
      #   |      0
      #   |     \|/
      #   |      |
      #   |     / \
      #   -
      #""".stripMargin('#')
  )
}
