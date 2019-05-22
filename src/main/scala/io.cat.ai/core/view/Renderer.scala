package io.cat.ai.core.view

trait Renderer[T] {
  def render(t: T): Unit
}
