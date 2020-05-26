#!/usr/bin/env sh

set -v

echo "===\n===Listing screenings\n==="
curl 'http://localhost:8080/screenings?from=1970-01-01T00:00&to=1970-01-01T04:00' | jq

echo "===\n===Getting details of a screening\n==="
curl 'http://localhost:8080/screenings/1' | jq

echo "===\n===Creating a reservation\n==="
curl --header "Content-Type: application/json" \
  --data '{"screeningId": "1", "name": "Piotr Jander", "ticketsBreakdown": {"adults": 1, "students": 0, "children": 0 }, "seats": [{"row": "II", "name": "2"}]}' \
  'http://localhost:8080/reservations' | jq

echo "===\n===Confirming the reservation\n==="
curl --header "Content-Type: application/json" \
  --data '{"secret": "secretkey"}' \
  'http://localhost:8080/reservations/11/confirm' | jq