package io.cat.ai.renderer

trait Renderer[T] {
  def render(t: T): Unit
}
