# OrderProcessing

This is an order processing app. 

Postman collection with the endpoints are added in this repo.

Endpoints:

POST /orders : Accepts the order and item details from the user and save the same to the database and produce the message to the order-details topic.

GET /orders/{orderId} : Returns the order details by order id which is created while making an order (take it from the POST /orders response).

GET /orders/getStatus/{orderId} : Get the status of the order based on the orderId (PLACED/PROCESSED).


OrderConsumer consumes the messages in the topic and update the order status to PROCESSED in the db.

order_topic: order-details.

Database:

Cassandra (NoSQL) (Local db was used)

Schema:

Keyspace: order_processing

database_name: orders

Primary key: order_id UUID

Columns: 

String: customer_id, item_id, item_title, first_name, last_name, billing_address, shipping_address, price, order_status, phone_number, retailer_name

int: purchased_quantity

date: transaction_date, expected_delivery
