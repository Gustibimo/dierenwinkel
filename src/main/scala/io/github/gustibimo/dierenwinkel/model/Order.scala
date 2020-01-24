package io.github.gustibimo.dierenwinkel.model

import java.time.Instant

case class Order(
    petId: Long,
    shipDate: Option[Instant] = None,
    status: OrderStatus = Placed,
    complete: Boolean = false,
    id: Option[Long] = None
)
