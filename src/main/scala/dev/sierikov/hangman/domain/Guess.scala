package dev.sierikov.hangman.domain

final case class Guess(char: Char)
object Guess {
  def apply(char: Char): Option[Guess] = if (char.isLetter) Some(new Guess(char)) else None
  def apply(string: String): Option[Guess] = if (string.length == 1) Guess(string.head) else None
}
