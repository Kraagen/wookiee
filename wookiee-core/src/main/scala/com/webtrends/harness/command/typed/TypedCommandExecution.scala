package com.webtrends.harness.command.typed

import akka.pattern._
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag


case class ExecuteTypedCommand(args: Any)

class TypedCommandExecution[T, V: ClassTag](name: String, args: T)  {

  implicit val timeout: Timeout = 2 seconds

  def execute()(implicit executionContext: ExecutionContext): Future[V] = {
    TypedCommandManager.commands.get(name) match {
      case Some(commandActor) =>
        (commandActor ? ExecuteTypedCommand(args)).map(_.asInstanceOf[V])
      case None =>
        Future.failed(new IllegalArgumentException(s"Command $name not found."))
    }
  }
}
