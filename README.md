# codepoint-distance

AWS Lambda REST API to calculate distances between postcodes using Ordnance Survey Code-Point Open data

### Deploy Network

```bash
aws cloudformation deploy \
 --template codepoint-distance-network.json \
 --stack-name codepoint-distance-network
```

### Package and Deploy Lambda

```bash
aws cloudformation package \
 --template-file codepoint-distance-lambda.json \
 --s3-bucket <BUCKET ID> \
 --output-template-file codepoint-distance-lambda-output.json
```

```bash
aws cloudformation deploy \
 --template-file codepoint-distance-lambda-output.json \
 --stack-name codepoint-distance \
 --capabilities CAPABILITY_IAM
```

### Upload and Load Data

```bash
aws s3 cp codepo_gb.zip s3://<BUCKET ID>
```

```bash
aws lambda invoke \
  --function-name CodePointLoad \
  --payload '{"bucket": "<BUCKET ID>", "key": "codepo_gb.zip"}' \
  response.json
```

### Get API URL

```bash
aws cloudformation describe-stacks \
  --stack-name codepoint-distance \
  --query 'Stacks[0].Outputs[?OutputKey==`ApiUrl`].OutputValue'
```

### Curl URL

```bash
curl -s https://<SERVER ID>.execute-api.eu-west-2.amazonaws.com/Prod/codepoint/distance/<POSTCODE>/<POSTCODE> | json_pp
```

### Example Usage and Output

```bash
curl -s https://77waizvyq3.execute-api.eu-west-2.amazonaws.com/Prod/codepoint/distance/YO241AB/YO17HH | json_pp
{
   "distance" : 817.743235985477,
   "toCodePoint" : {
      "postcode" : "YO1 7HH",
      "eastings" : 460350,
      "northings" : 452085
   },
   "fromCodePoint" : {
      "northings" : 451737,
      "postcode" : "YO24 1AB",
      "eastings" : 459610
   }
}
```

### Delete Stacks

```bash
aws cloudformation delete-stack \
 --stack-name codepoint-distance
```

```bash
aws cloudformation delete-stack \
 --stack-name codepoint-distance-network
```
