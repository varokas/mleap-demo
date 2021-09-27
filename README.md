# MLeap Demo

## Prerequisite
* Docker

## Train Model and Save to Zip

1. Run Jupyter & Spark
```
docker run -p 8888:8888 -e JUPYTER_ENABLE_LAB=yes -v $PWD:/home/jovyan jupyter/pyspark-notebook
```

2. Open airbnb.ipynb and run the steps to generate `models/*.zip`

## Serve Model

1. Run server at port 8080
```
docker run -p 8080:65327 -v $PWD/models:/models combustml/mleap-spring-boot:0.19.0-SNAPSHOT
```

2. Load Models 
```
curl --request POST \
  --url http://localhost:8080/models \
  --header 'Content-Type: application/json' \
  --data '{
	"modelName": "airbnb_lr",
	"uri": "file:/models/airbnb.lr.zip",
	"config": {"memoryTimeout": 900000,"diskTimeout": 900000},
	"force": false
}'
```

3. Check model loaded
```
curl --request GET \
  --url http://localhost:8080/models/airbnb_lr
  
Output:
{
  "name": "airbnb_lr",
  "uri": "file:/models/airbnb.lr.zip",
  "config": {
    "memoryTimeout": "900000",
    "diskTimeout": "900000"
  }
}

```
  
4. Infer
```

curl -X POST -H 'Content-Type: application/json' --data-binary "@mleap_request.json" http://localhost:8080/models/airbnb_lr/transform

Output :: 

{
  "schema": {
    "fields": [{
      "name": "bathrooms",
      "type": "double"
    }, {
      "name": "bedrooms",
      "type": "double"
    }, {
      "name": "security_deposit",
      "type": "double"
    }, {
      "name": "cleaning_fee",
      "type": "double"
    }, {
      "name": "extra_people",
      "type": "double"
    }, {
      "name": "number_of_reviews",
      "type": "integer"
    }, {
      "name": "square_feet",
      "type": "double"
    }, {
      "name": "review_scores_rating",
      "type": "double"
    }, {
      "name": "room_type",
      "type": "string"
    }, {
      "name": "host_is_superhost",
      "type": "double"
    }, {
      "name": "cancellation_policy",
      "type": "string"
    }, {
      "name": "instant_bookable",
      "type": "double"
    }, {
      "name": "state",
      "type": "string"
    }, {
      "name": "unscaled_continuous_features",
      "type": {
        "type": "tensor",
        "base": "double",
        "dimensions": [8]
      }
    }, {
      "name": "scaled_continuous_features",
      "type": {
        "type": "tensor",
        "base": "double",
        "dimensions": [8]
      }
    }, {
      "name": "room_type_index",
      "type": {
        "type": "basic",
        "base": "double",
        "isNullable": false
      }
    }, {
      "name": "host_is_superhost_index",
      "type": {
        "type": "basic",
        "base": "double",
        "isNullable": false
      }
    }, {
      "name": "cancellation_policy_index",
      "type": {
        "type": "basic",
        "base": "double",
        "isNullable": false
      }
    }, {
      "name": "instant_bookable_index",
      "type": {
        "type": "basic",
        "base": "double",
        "isNullable": false
      }
    }, {
      "name": "state_index",
      "type": {
        "type": "basic",
        "base": "double",
        "isNullable": false
      }
    }, {
      "name": "price_prediction",
      "type": {
        "type": "basic",
        "base": "double",
        "isNullable": false
      }
    }]
  },
  "rows": [[1.0, 2.0, 100.0, 20.0, 10.0, 8, 400.0, 94.0, "Entire home/apt", 0.0, "moderate", 0.0, "London", {
    "values": [1.0, 2.0, 100.0, 20.0, 10.0, 8.0, 400.0, 94.0],
    "dimensions": [8]
  }, {
    "values": [2.0701404784672404, 2.36222706296942, 0.4089700538272752, 0.4690169961895324, 0.5352011985607764, 0.28585910572404577, 1.1007203240214969, 10.959305553807575],
    "dimensions": [8]
  }, 0.0, 0.0, 1.0, 0.0, 3.0, 124.8613962206735]]
}
```

## Lambda
```
docker run -p 9000:8080  mleap-demo:latest 
```

```
curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
```