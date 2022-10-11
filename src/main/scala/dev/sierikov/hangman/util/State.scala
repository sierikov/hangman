package dev.sierikov.hangman.util

import dev.sierikov.hangman.domain.{Guess, Name, Word}

final case class State (name: Name, guesses: Set[Guess] = Set.empty, word: Word) {
  def failuresCount: Int = guesses.count(g => !word.contains(g.char))
  def playerLost: Boolean = failuresCount > 5
  def playerWon: Boolean = (guesses.map(_.char) -- word.toSet).isEmpty
  def addGuess(guess: Guess): State = this.copy(guesses = guesses + guess)
}
