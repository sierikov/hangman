package dev.sierikov.hangman.domain

final class Word(word: String) {
  val length: Int = word.length
  val value: String = word
  def contains(char: Char): Boolean = word.contains(char)
  def toList: List[Char] = word.toList
  def toSet: Set[Char] = word.toSet
}

object Word {
  def apply(word: String): Option[Word] = {
    val consistOfOnlyLetters = word.nonEmpty && word.forall(_.isLetter)

    if(consistOfOnlyLetters) Some(new Word(word.toLowerCase)) else None
  }
}
