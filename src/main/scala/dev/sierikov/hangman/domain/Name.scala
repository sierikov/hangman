package dev.sierikov.hangman.domain

final case class Name (name: String)
object Name {
  def apply(name: String): Option[Name] = if (name.nonEmpty) Some(new Name(name)) else None
}
