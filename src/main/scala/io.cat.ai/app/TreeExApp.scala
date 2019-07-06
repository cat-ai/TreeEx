package io.cat.ai.app

trait TreeExApp[F[_], Input, Args, Options, Graph, Processor] {

  def parseArgs(args: Input): F[Args]

  def buildOptions(args: Args): F[Options]

  def createGraph(options: Options): F[Graph]

  def createProcessor(options: Options): F[Processor]
}