# Two Sum Finder

## Overview

A service that finds all the pairs of two integers in an unsorted array that sum up to a given target sum.

## REST API 

REST API is streaming, i.e. csv lines are fetched, processed and pushed back one-by-one.

###Request Protocol
```
POST upload/{targetSum}
Content-Type: text/csv
{csv}
{csv}
...
{csv}
```
###Response Protocol
```
Content-Type: application/json
{json}
{json}
...
{json}
```

###Example of Successful Request / Response
```
POST upload/5
Content-Type: text/csv
11,-4,3,4,3,2
2,5,5,3,0,1

Content-Type: application/json
{ "pairs": [[3, 2]] }
{ "pairs": [[2, 3], [5, 0]] }
```

###Example of Malformed Request / Error Response
```
POST upload/5
Content-Type: text/csv
11,-4,3,4,3,2
wrong

Content-Type: application/json
{ "pairs": [[3, 2]] }
{ "errorCode": "400", "message": "Couldn't parse one of the numbers. For input string: \"wrong\"" }
```

##Testing Locally

1. Run the `TwoSumFinderApp`, optionally [enabling throttling]() in `TwoSumHttpRoutes`
2. Install [httpie](https://httpie.io) to employ streaming: `brew install httpie`
3. `http --stream POST localhost:8080/upload/5 < testfiles/input.csv`
