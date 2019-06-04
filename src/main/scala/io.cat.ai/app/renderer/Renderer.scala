package io.cat.ai.app.renderer

trait Renderer[T] {
  def render(t: T): Unit
}
