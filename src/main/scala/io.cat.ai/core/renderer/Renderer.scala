package io.cat.ai.core.renderer

trait Renderer[T] {
  def render(t: T): Unit
}
