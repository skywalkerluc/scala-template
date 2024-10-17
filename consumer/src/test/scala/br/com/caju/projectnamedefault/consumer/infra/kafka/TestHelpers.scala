package br.com.caju.projectnamedefault.consumer.infra.kafka

import br.com.caju.kafkacommons.{Serdes, commit, recordAsOrIgnore}
import io.circe.{Decoder, Json}
import zio.kafka.KafkaTestUtils
import zio.kafka.consumer.{CommittableRecord, Consumer, ConsumerSettings, Subscription}
import zio.kafka.serde.Serde
import zio.stream.ZStream
import zio.{Chunk, Duration, ZIO, ZLayer, durationInt}

import scala.util.Try

trait TestHelpers {
  def purge(topics: String*) =
    KafkaTestUtils.withAdmin { adminClient =>
      ZIO.foreachDiscard(topics)(topic => adminClient.deleteTopic(topic)).orElseSucceed(())
    }

  def collectNextMessage[T](topic: String)(implicit
      decoder: Decoder[T],
      timeout: Duration = 3.seconds
  ): ZIO[ConsumerSettings, Nothing, Option[T]] =
    baseConsumer(topic)
      .take(1)
      .flatMap(processRecord[T])
      .runLast
      .timeoutFail(
        new RuntimeException(
          s"Timeout after ${timeout.getSeconds} seconds on collectNextMessage for $topic."
        )
      )(timeout)
      .orDie

  def collectNextNMessages[T](n: Long, topic: String)(implicit
      decoder: Decoder[T],
      timeout: Duration = 3.seconds
  ): ZIO[ConsumerSettings, Nothing, Chunk[T]] =
    baseConsumer(topic)
      .take(n)
      .flatMap(processRecord[T])
      .runCollect
      .timeoutFail(
        new RuntimeException(
          s"Timeout after ${timeout.getSeconds} seconds on collectNextNMessages with $n for $topic."
        )
      )(timeout)
      .orDie

  private def baseConsumer(
      topic: String
  ): ZStream[ConsumerSettings, Throwable, CommittableRecord[String, Try[Json]]] = {
    val consumer = ZLayer.scoped(ZIO.serviceWithZIO[ConsumerSettings](settings => Consumer.make(settings).orDie))
    Consumer
      .subscribeAnd(Subscription.topics(topic))
      .plainStream(Serde.string, Serdes.json.asTry)
      .provideLayer(consumer)
  }

  private def processRecord[T](
      record: CommittableRecord[String, Try[Json]]
  )(implicit decoder: Decoder[T]): ZStream[Any, Nothing, T] =
    for {
      event <- recordAsOrIgnore[T](record)
      _     <- commit(record.offset)
    } yield event
}
