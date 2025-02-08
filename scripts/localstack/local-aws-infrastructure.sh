#!/bin/bash
set -e

echo
echo "Printing all environment variables from the .env file..."
echo "-------------------------------------------------------"

while IFS='=' read -r key value; do
  if [[ ! -z "$key" && ! "$key" =~ ^# ]]; then
    echo "$key=${value:-<unset>}"
  fi
done < <(env)

echo
echo "Validating required environment variables..."
echo "--------------------------------------------"

REQUIRED_ENV_VARS=(
  # "AWS_REGION"
  "AWS_DEFAULT_REGION"
  "AWS_ACCESS_KEY_ID"
  "AWS_SECRET_ACCESS_KEY"
  "SERVICES"
)

all_set=true

for VAR in "${REQUIRED_ENV_VARS[@]}"; do
  if [[ -z "${!VAR}" ]]; then
    echo "ERROR: Required environment variable '$VAR' is not set."
    all_set=false
  else
    echo "OK: $VAR=${!VAR}"
  fi
done

if [[ $all_set == false ]]; then
  echo
  echo "Exiting due to missing required environment variables."
  exit 1
fi

echo "============= Setting up localstack profile aws region $AWS_REGION default region $AWS_DEFAULT_REGION =========="
aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID --profile=localstack
aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY --profile=localstack
aws configure set region $AWS_DEFAULT_REGION --profile=localstack

echo "Setting default profile"
echo "-----------------------"
export AWS_DEFAULT_PROFILE=localstack

SNS_TICKET_TOPIC="ticket-topic"
export SQS_SPORT_QUEUE="sport-consumer-queue"
export SQS_MOVIE_QUEUE="movie-consumer-queue"

echo
echo "============ Initializing LocalStack ======================="
echo "using awslocal instead of: aws --endpoint-url=http://localhost:4566"
echo 

echo
echo "Installing jq"
echo "-------------"
apt-get -y install jq


echo
echo "Creating $SNS_TICKET_TOPIC in SNS"
echo "----------------------------"
awslocal sns create-topic --name $SNS_TICKET_TOPIC

echo
echo "Creating $SQS_SPORT_QUEUE in SQS"
echo "-----------------------------------"
# awslocal sqs create-queue --queue-name $SQS_SPORT_QUEUE
SPORT_QUEUE_URL=$(awslocal sqs create-queue --queue-name $SQS_SPORT_QUEUE | jq -r '.QueueUrl')
MOVIE_QUEUE_URL=$(awslocal sqs create-queue --queue-name $SQS_MOVIE_QUEUE | jq -r '.QueueUrl')

echo
echo "Subscribing $SQS_SPORT_QUEUE to $SNS_TICKET_TOPIC with event_type=sport FilterPolicy. NOTE eventType does not work."
echo "---------------------------------------------"
awslocal sns subscribe \
  --topic-arn arn:aws:sns:$AWS_DEFAULT_REGION:000000000000:$SNS_TICKET_TOPIC \
  --protocol sqs \
  --attributes '{"RawMessageDelivery":"true"}' \
  --attributes '{"RawMessageDelivery": "true", "FilterPolicy": "{\"event_type\": [\"sport\"]}"}' \
  --notification-endpoint arn:aws:sqs:$AWS_DEFAULT_REGION:000000000000:$SQS_SPORT_QUEUE

echo
echo "Subscribing $SQS_MOVIE_QUEUE to $SNS_TICKET_TOPIC with event_type=movie FilterPolicy. NOTE eventType does not work."
echo "---------------------------------------------"
awslocal sns subscribe \
  --topic-arn arn:aws:sns:$AWS_DEFAULT_REGION:000000000000:$SNS_TICKET_TOPIC \
  --protocol sqs \
  --attributes '{"RawMessageDelivery": "true", "FilterPolicy": "{\"event_type\": [\"movie\"]}"}' \
  --notification-endpoint arn:aws:sqs:$AWS_DEFAULT_REGION:000000000000:$SQS_MOVIE_QUEUE

echo
echo "Listing SQS Queues"
echo "---------------------------------------------"
awslocal sqs list-queues


echo
echo "Creating ticket table in DynamoDB"
echo "---------------------------------"
awslocal dynamodb create-table \
  --table-name ticket \
  --attribute-definitions AttributeName=id,AttributeType=S \
  --key-schema AttributeName=id,KeyType=HASH \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --stream-specification StreamEnabled=true,StreamViewType=NEW_AND_OLD_IMAGES

echo
echo "Getting ticket table DynamoDB Stream ARN"
echo "-----------------------------------------"
TICKET_TABLE_DYNAMODB_STREAM_ARN=$(awslocal dynamodb describe-table --table-name ticket | jq -r '.Table.LatestStreamArn')
echo "TICKET_TABLE_DYNAMODB_STREAM_ARN=${TICKET_TABLE_DYNAMODB_STREAM_ARN}"

echo
echo "Creating Lambda Function called ProcessDynamoDBEvent"
echo "----------------------------------------------------"
awslocal lambda create-function \
  --function-name ProcessDynamoDBEvent \
  --runtime java21 \
  --memory-size 512 \
  --handler org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest \
  --zip-file fileb:///shared/dynamodb-lambda-function-java21-aws.jar \
  --environment "Variables={AWS_REGION=$AWS_DEFAULT_REGION,AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID,AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY}" \
  --role arn:aws:iam::000000000000:role/service-role/irrelevant \
  --timeout 60

echo
echo "Creating a mapping between ticket table DynamoDB event source and ProcessDynamoDBEvent lambda function"
echo "----------------------------------------------------------------------------------------------------"
awslocal lambda create-event-source-mapping \
  --function-name ProcessDynamoDBEvent \
  --batch-size 100 \
  --event-source $TICKET_TABLE_DYNAMODB_STREAM_ARN \
  --starting-position LATEST

echo
echo "Successfully provisioned resources"
echo "==================================="