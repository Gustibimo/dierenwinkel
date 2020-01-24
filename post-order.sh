#!/bin/bash

echo "This will only work one time, then you have to restart!"

echo "Creating pet..."
curl -X POST http://localhost:8088/orders -d @pet-order.json --header "Content-Type: application/json"

#echo "Deleting pet..."
#curl -X DELETE "http://localhost:8088/pets?id=1"
