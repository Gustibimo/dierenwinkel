package io.github.gustibimo.dierenwinkel.service

import io.github.gustibimo.dierenwinkel.model.Order
import io.github.gustibimo.dierenwinkel.repository.OrderRepositoryAlgebra

import scala.language.higherKinds

class OrderService[F[_]](orderRepo: OrderRepositoryAlgebra[F]) {

  def placeOrder(order: Order): F[Order] = orderRepo.put(order)

}

object OrderService {
  def apply[F[_]](orderRepo: OrderRepositoryAlgebra[F]): OrderService[F] =
    new OrderService(orderRepo)
}
