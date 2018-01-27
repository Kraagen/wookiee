package com.webtrends.harness.command.typed

import akka.actor.Actor
import akka.pattern._

import scala.concurrent.{ExecutionContext, Future}


trait TypedCommand[T, V] extends Actor {

  implicit val executionContext: ExecutionContext = context.dispatcher

  val commandName: String

  def receive: Receive = {
    case ExecuteTypedCommand(args) => pipe(execute(args.asInstanceOf[T])) to sender
  }

  def execute(args: T): Future[V]
}