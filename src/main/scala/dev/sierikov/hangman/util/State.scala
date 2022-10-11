package dev.sierikov.hangman.util

import dev.sierikov.hangman.domain.{Guess, Name, Word}

final case class State (name: Name, word: Word, guesses: Set[Guess] = Set.empty) {
  def failuresCount: Int = guesses.count(g => !word.contains(g.char))
  def playerLost: Boolean = failuresCount > 5
  def playerWon: Boolean = {
    val guessed = guesses.map(_.char)
    word.toSet.forall(guessed.contains)
  }
  def addGuess(guess: Guess): State = this.copy(guesses = guesses + guess)
}
