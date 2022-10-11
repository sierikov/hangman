package dev.sierikov.hangman

import dev.sierikov.hangman.domain.{ Guess, GuessResult, Name, Word }
import dev.sierikov.hangman.util.State
import zio.{ Console, IO, Random, UIO, ZIO, ZIOAppDefault }

import java.io.IOException
object Hangman extends ZIOAppDefault {
  def run: IO[IOException, Unit] =
    for {
      name <- Console.printLine("Welcome to ZIO Hangman!") <*> getName
      word <- chooseWord
      _    <- loop(State(name, word))
    } yield ()

  def getUserInput(message: String): IO[IOException, String] =
    Console.printLine(message) *> Console.readLine

  def getAndConvert[A](
    description: String,
    convertor: String => Option[A]
  ): IO[IOException, A] =
    for {
      input <- getUserInput(description)
      value <- ZIO.fromOption(convertor(input)) <>
                (Console.printLine("Invalid input. Please try again...") <*> getAndConvert(description, convertor))
    } yield value

  def getName: IO[IOException, Name]   = getAndConvert("What's your name?", Name.apply)
  def getGuess: IO[IOException, Guess] = getAndConvert("Guess the letter?", Guess.apply)

  def chooseWord: UIO[Word] =
    for {
      index <- Random.nextIntBounded(words.length)
      word  <- ZIO.from(words.lift(index).flatMap(Word.apply)).orDieWith(_ => new Error("Random is evil"))
    } yield word

  def analyzeNewGuess(oldState: State, newState: State, guess: Guess): GuessResult =
    if (oldState.guesses.contains(guess)) GuessResult.Unchanged
    else if (newState.playerWon) GuessResult.Won
    else if (newState.playerLost) GuessResult.Lost
    else if (oldState.word.contains(guess.char)) GuessResult.Correct
    else GuessResult.Incorrect

  def renderState(state: State): IO[IOException, Unit] = {
    val hangman = ZIO.attempt(hangmanStages(state.failuresCount)).orDieWith(_ => new Error("Thanks for playing!"))
    val word = state.word.toList
      .map(c =>
        if (state.guesses.map(_.char).contains(c)) s" $c "
        else " " * 3
      )
      .mkString

    val line    = List.fill(state.word.length)(" - ").mkString
    val guesses = s" Guesses: ${state.guesses.map(_.char).mkString(", ")}"

    hangman.flatMap { hangman =>
      Console.printLine {
        s"""
           #
           #$hangman
           #$word
           #$line
           #$guesses
           #
           #""".stripMargin('#')
      }
    }
  }

  def loop(currentState: State): IO[IOException, Unit] = {
    import GuessResult._
    for {
      guess       <- renderState(currentState) <*> getGuess
      newState    = currentState.addGuess(guess)
      guessResult = analyzeNewGuess(currentState, newState, guess)
      _ <- guessResult match {
            case Won       => Console.printLine(s"Congratulations ${newState.name.name}! You won!") <*> renderState(newState)
            case Correct   => Console.printLine(s"Good guess!") <*> loop(newState)
            case Incorrect => Console.printLine(s"Bad guess!") <*> loop(newState)
            case Unchanged =>
              Console.printLine(s"${newState.name.name}, you've already tried that letter!") <*> loop(newState)
            case Lost =>
              Console.printLine(s"Sorry ${newState.name.name}! You Lost! Word was: ${newState.word.value}") <*>
                renderState(newState)
          }
    } yield ()
  }

}
