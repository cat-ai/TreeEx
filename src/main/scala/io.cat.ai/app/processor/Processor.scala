package io.cat.ai.app.processor

trait Processor[A, B] {
  def process(a: A): B
}
