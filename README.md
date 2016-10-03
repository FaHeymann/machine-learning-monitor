[![Build Status](https://travis-ci.org/FaHeymann/machine-learning-monitor.svg?branch=master)](https://travis-ci.org/FaHeymann/machine-learning-monitor) ![Build Status](https://circleci.com/gh/FaHeymann/machine-learning-monitor.svg?&style=shield)

## Setup

### Requirements

* MySQL Database
* JDK >= 8.0
* NodeJS
* npm
* Activator e.g. `brew install typesafe-activator` (you can use the shipped version but installing it should be preferred)

### Configuration

* Change MySQL Data in conf/application.conf (at the bottom) to match your database
* Run `npm install`

## Run in dev mode

Run `activator run` and navigate to `localhost:9000` (use `activator "run 1234"` to use another port if necessary).
The first time you will be asked to run the database evolution script.

## Functionality

### Feature Sets

You can import Feature Sets from .csv files. The csv must fulfill the following requirements:
* The first row must contain the labels for the columns
    * All labels must be unique
* The last column must contain the result for that rows entry

### Algorithms

To use this tool with one of your algorithms your algorithm must provide a reachable http endpoint that accepts POST json requests with test data and returns a result. Test data send to your endpoint will be of the following form:

```json
{
  "type": "object",
  "properties": {
    "allLabels": {
      "type": "array",
      "items": {
        "type": "string"
      }
    },
    "excludeLabels": {
      "type": "array",
      "items": {
        "type": "string"
      }
    },
    "features": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "result": {
            "type": "number",
          },
          "data": {
            "type": "array",
            "items": {
              "type": "string"
            }
          }
        }
      }
    },
    "parameters": {
      "type": "object",
      "properties": {
        "string": {
          "type": "string"
        },
        "double": {
          "type": "number"
        },
        "enum": {
          "type": "string"
        },
        "int": {
          "type": "integer"
        }
      },
      "required": []
    }
  },
  "required": [
    "allLabels",
    "excludeLabels",
    "features",
    "parameters"
  ]
}
```

* The `allLabels` and `features` arrays will contain the featureSet data specified for the test.
* The `parameters object will contain all parameters specified for the algorithm (when creating it) in the form `parameter.name: parameter.value` where the value is specified for each test you run.
* The `excludeLabels` array will contain the names of the labels to be ignored in the test.

Your algorithm is then expected to return a json response of the following form:

```json
{
  "type": "array",
  "items": {
    "type": "object",
    "properties": {
      "expected": {
        "type": "number"
      },
      "actual": {
        "type": "number"
      }
    }
  }
}
```

For each feature you used as test data you should return a tuple

```json
{
  "actual": 0.2, // example values
  "expected": 0.3
}
```

where the `expected` value should be the (real) result (given by the request) of the feature you got and the `actual` value should be the result your algorithms calculated for that test feature.

To use your algorithm with this tool create an algorithm in the tool and specify the endpoint for it.

#### Algorithm Parameters

Each algorithm can be assigned a set of parameters. At each test you must specify a value for each of the algorithms parameters. You can use this to test your algorithm with different settings. Parameters can be of one of the following types:

* String
* Integer
* Double
* Enum

Integer and double values will be posted as number types. String and enum values will be posted as strings.