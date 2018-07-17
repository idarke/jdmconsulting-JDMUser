var LOCAL_ENV = "LOCAL";
var DEV_ENV = "DEV";
var PROD_ENV = "PROD";

//var deployEnvironment = LOCAL_ENV;
//var deployEnvironment = DEV_ENV;
var deployEnvironment = PROD_ENV;

var JDMUSERS_GATEWAY_API_DEV = "https://hlfrbjlph9.execute-api.us-east-2.amazonaws.com/jdmusers";
var JDMUSERS_GATEWAY_API_PROD = "https://rfzx7qspq6.execute-api.us-east-2.amazonaws.com/jdmusers";

//Return the GATEWAY API url for the JDMUsers lambda
function JDMUserRequest()
{
   if (deployEnvironment === DEV_ENV)
   {
      return JDMUSERS_GATEWAY_API_DEV;
   }
   return JDMUSERS_GATEWAY_API_PROD;
}
