package com.example.demo;

import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    // Good documentation for /login, /authorization, /token and /logout endpoints
    //http://docs.aws.amazon.com/cognito/latest/developerguide/cognito-userpools-server-contract-reference.html

    // My keys
    //https://cognito-idp.us-east-1.amazonaws.com/us-east-1_tHhhasTwb/.well-known/jwks.json

    /*
     {
      "keys": [
        {
          "alg": "RS256",
          "e": "AQAB",
          "kid": "q2k81bhof7OcLF6bJ8+BrfLb1OBpaoXVmi3G0ZXaUb8=",
          "kty": "RSA",
          "n": "kyPfge4xBKEBlbn_qIus-S_BjzKeJRdNqV7R744n0pULinWQ1QYu9SpCaJFVU9nIw7Hcfoa8NqNf2ob0kUa8YGwDBkc30TnAhW-gISfxshnhYzUJ2k9FbfIaUsFo6FaeAWtHRx-f5ViNqHsLvWRpAxQA_Mf11V-8_QlSTwa3sw6Z5vGgmgeOjmnSxYXavP3oi5Du8I6jbbjR38_IKdl_LBNPCPZcG9H7q3V7WuhI6xqSjPV_OMiw4BDSagFSqLGcIUFpyaVaadh3cTXGcUwHyVzKs68WmF2dlARDtnp964LHf5cL6iiJZQyT6_T5TFxep8ZNsUhbnDCIYY4KoNrf8w",
          "use": "sig"
        },
        {
          "alg": "RS256",
          "e": "AQAB",
          "kid": "k/BM7jsnlkj9mJo1JGSoJ7U+i0MlMzMHef4dpsQ0MdM=",
          "kty": "RSA",
          "n": "ssisNp-inESwU37vYl6k7kgSmmG-iUQ4FecH4wZuHRkr-REVOismCOvha9-_vOx9eHJuJKJ06mAUTRzevhnPh7NwgR_r0TdOg5qC2L6AZz84NZScpTcyLEY4X6ydqJgnFo3rMSX_GwWv69Q93Mu3ZJ_2fYRT89Q5SnETxi5OOiJG-rkUuR7QAhIG4DMIzZ3TJ9NF4B4cpPLxgM6iZ328ut8QfvFtjvrqLinjiI8xjHbasSqrpETzKQo1DMf5WpSSEWPG1ka4DKcSpTipDJuxv-8ejbkoI23oPy1wiGeA8RLKnFVmMm_M14oykorojy9wWygO6J1hQgCXRgOIaBpT3Q",
          "use": "sig"
        }
      ]
    }

     */

    // login page url
    // https://waits-test.auth.us-east-1.amazoncognito.com/login?response_type=code&client_id=<your_app_client_id>&redirect_uri=<your_callback_url>
    // https://waits-test.auth.us-east-1.amazoncognito.com/login?response_type=token&client_id=3j8suhjar4asibcvbacn5pa99h&redirect_uri=https://portal.callbackurl.com

    @Autowired
    AmazonCognitoIdentity cognito;


    @GetMapping("/token")
    public String token(@RequestParam("t") String token) {
        //eyJraWQiOiJxMms4MWJob2Y3T2NMRjZiSjgrQnJmTGIxT0JwYW9YVm1pM0cwWlhhVWI4PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiIzNThhZDI3MS00ZmE4LTRmMmQtOGI3OS02NmUyODVhZGUxMDIiLCJhdWQiOiIzajhzdWhqYXI0YXNpYmN2YmFjbjVwYTk5aCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTUxMTEyODY2NCwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLWVhc3QtMS5hbWF6b25hd3MuY29tXC91cy1lYXN0LTFfdEhoaGFzVHdiIiwiY29nbml0bzp1c2VybmFtZSI6IjM1OGFkMjcxLTRmYTgtNGYyZC04Yjc5LTY2ZTI4NWFkZTEwMiIsInByZWZlcnJlZF91c2VybmFtZSI6InBkYXZpZHNvd2FpdHMyIiwiZXhwIjoxNTExMTMyMjY0LCJpYXQiOjE1MTExMjg2NjQsImVtYWlsIjoicGRhdmlkc28rd2FpdHMyQGdtYWlsLmNvbSJ9.N0NXWSvusPrlvpHG25jL8JJjOD2c0C-NyQa5QX_IBI_Z5tQek5X7aHWUYWPwwbKAbfS_-DgZFtOf5Jh7ZDsHECUuOsfQ2VuqheapNWKuMG4x7iBSXzAZCs70LkMk3PCfuJCza_exdslgkQQ3A5SukGcwxPoqd4tTjvLki1bIwjzhvmeR4ihP0zGSw_6cRHGTaaD8zSf3Eld3MgHWgUTncQpO2EnZjaSsQlqJoAsOvsrO29DgoMZ0cT7ZzrCxGM9CTPHFgkmEfMlqQPZeYa2AP3iKGGN1rWkCJv79r7Ja4rpBW0sIj2FlZBJ9Hbfk5ndLY37SW-WK3AHfFLcwM294mQ&access_token=eyJraWQiOiJrXC9CTTdqc25sa2o5bUpvMUpHU29KN1UraTBNbE16TUhlZjRkcHNRME1kTT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzNThhZDI3MS00ZmE4LTRmMmQtOGI3OS02NmUyODVhZGUxMDIiLCJ0b2tlbl91c2UiOiJhY2Nlc3MiLCJzY29wZSI6ImF3cy5jb2duaXRvLnNpZ25pbi51c2VyLmFkbWluIG9wZW5pZCBwcm9maWxlIGVtYWlsIiwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLWVhc3QtMS5hbWF6b25hd3MuY29tXC91cy1lYXN0LTFfdEhoaGFzVHdiIiwiZXhwIjoxNTExMTMyMjY0LCJpYXQiOjE1MTExMjg2NjQsInZlcnNpb24iOjIsImp0aSI6IjdlNDE1YWQzLTY0YTItNDIzYS1iNjQ0LTQwYThiNjQ1NDY3MCIsImNsaWVudF9pZCI6IjNqOHN1aGphcjRhc2liY3ZiYWNuNXBhOTloIiwidXNlcm5hbWUiOiIzNThhZDI3MS00ZmE4LTRmMmQtOGI3OS02NmUyODVhZGUxMDIifQ.nbGh7l9SZ_TPWmfoTUVK8J5-LR8gOqQMtTbghs3jB2j8Kbjq5_9qbQofbMTmFiPftYWa4G4uWXzzVWJ9GMGDFRjx2BMh5YWyfC7P-gS2kL8QpsXi9jK0i9XTVe1MGKuVsccjJjEADSx6aMG4Kkqj8GTnloa6YpJdK5ieURWNCyf0ruf0j2rM2jXE-XngyRY4hbGJKhSmzzmOjIVbKGCWjqITJpICCrz99RezFW3kdWYqXoloq398XQNS9Iu7pN1chmMOoZ0J9yxF459VaFjTpWlFHLCrAy6bwGJQl6wzsaJRrK8lRds4I7QL6vbHXOa0A93jesCIZ1lFZns1Bj75lA&expires_in=3600&token_type=Bearer
//        cognito.get

        return "";
    }


}
