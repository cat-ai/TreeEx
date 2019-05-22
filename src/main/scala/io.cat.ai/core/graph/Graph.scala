package io.cat.ai.core.graph

trait Graph[E] {

  def value: E

  def edges: Seq[Graph[E]]

  def isEmpty: Boolean

  def nonEmpty = !isEmpty

  def find(e: E): Option[Graph[E]]
}

object Graph {

  def dfs[E](current: Graph[E], acc: List[Graph[E]]): List[Graph[E]] =
    current :: current.edges.foldLeft(acc) {
      (resList, next) =>
        if (resList contains next) resList
        else dfs(next, current :: resList)
    }

  def empty[E]: Graph[E] = new Graph[E] {

    override def value: Nothing = throw new NoSuchElementException("Empty")

    override def edges: Seq[Nothing] = Nil

    override def isEmpty = true

    override def find(e: E): Option[Nothing] = None

    override def toString: String = s"Graph { \n value: , \n edges: $edges \n, isEmpty: $isEmpty }"
  }

  def one[E](e: E): Graph[E] = new Graph[E] {

    override def value: E = e

    override def edges: Seq[Graph[E]] = Nil

    override def isEmpty = false

    override def find(e: E): Option[Graph[E]] = None

    override def toString: String = s"Graph { \n value: $value, \n edges: $edges \n isEmpty: $isEmpty \n }"
  }

  def apply[E](e: E, _edges: Seq[Graph[E]]): Graph[E] = new Graph[E] {

    override def value: E = e

    override def edges: Seq[Graph[E]] = _edges

    override def isEmpty = false

    override def find(e: E): Option[Graph[E]] = dfs(this, Nil) find(_.value == e)

    override def toString: String = s"Graph { \n value: $value, \n edges: $edges \n, isEmpty: $isEmpty \n }"
  }
}