### Run locally

1. Run RabbitMQ - docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.9-management
2. Run Producer application
3. Run Listener application
4. Run DL-Service application
5. Run MongoDB - docker run -it --rm -p 27017:27017 mongo:5.0.8
6. Run test requests at the test-requests.http file
