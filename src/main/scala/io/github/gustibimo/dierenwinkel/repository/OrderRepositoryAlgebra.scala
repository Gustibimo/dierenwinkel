package io.github.gustibimo.dierenwinkel.repository

import io.github.gustibimo.dierenwinkel.model.Order

import scala.language.higherKinds

trait OrderRepositoryAlgebra[F[_]] {

  def put(order: Order): F[Order]

  def get(orderId: Long): F[Option[Order]]

  def delete(orderId: Long): F[Option[Order]]
}
