package model.other;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class SmsUtils {

    public static void send(String[] numTels,String message){
        initHttpClient();
        if (numTels!=null && numTels.length!=0){
            for (String tel: numTels) {
                sendTokenRequest();
                HttpRequest smsSenderRequest = createSmsRequest(tel,message);
                sendSmsRequest(numTels, message, smsSenderRequest);
            }
        }
    }

    private static void sendSmsRequest(String[] numTels, String message, HttpRequest smsSenderRequest) {
        try {
            HttpResponse<String> response = httpClient.send(smsSenderRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201){
                isUnauthorizedOrInvalidTokenError = true;
                send(numTels,message);
            }else isUnauthorizedOrInvalidTokenError = false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendTokenRequest(){
        if (accessToken == null || isUnauthorizedOrInvalidTokenError){
            String responseBody = execute(createTokenRequest());
            accessToken = extractTokenForm(responseBody);
        }
    }

    private static String extractTokenForm(String responseBody){
        responseBody = responseBody.replaceAll("[{}\"]", "").trim();
        String[] tab = responseBody.split(",");
        if (tab.length!=0){
            for (String jsonElement: tab) {
                String[] jsonSplit = jsonElement.trim().split(":");
                if (jsonSplit.length!=0){
                    String property = jsonSplit[0];
                    if (property.equals("access_token")){
                        return jsonSplit[1];
                    }
                }
            }
        }
        return "";
    }

    // TOKEN REQUEST

    private static HttpRequest createTokenRequest(){
        return HttpRequest.newBuilder(URI.create(authentificationUri))
                .header("Authorization", AUTHORIZATION_HEADER)
                .header("Content-type","application/x-www-form-urlencoded")
                .header("Accept","application/json")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();
    }

    public static HttpRequest createTokenRequest(String authentificationUri,String authorizationHeader){
        return HttpRequest.newBuilder(URI.create(authentificationUri))
                .header("Authorization",authorizationHeader)
                .header("Content-type","application/x-www-form-urlencoded")
                .header("Accept","application/json")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();
    }
    // PURCHASE
    private  HttpRequest createPurchaseOrderRequest(){
        return HttpRequest.newBuilder(URI.create(purchaseOrdersUri))
                .header("authorization", "Barear "+accessToken)
                .GET()
                .build();
    }
    public static HttpRequest createPurchaseOrderRequest(String purchaseOrdersUri,String accessToken){
        return HttpRequest.newBuilder(URI.create(purchaseOrdersUri))
                .header("authorization", "Barear "+accessToken)
                .GET()
                .build();
    }
    // STATISTIC
    public HttpRequest createStatisticRequest(){
        return HttpRequest.newBuilder(URI.create(statisticsUri))
                .header("authorization", "Barear "+accessToken+"")
                .GET()
                .build();
    }
    public  HttpRequest createStatisticRequest(String statisticsUri,String accessToken){
        return HttpRequest.newBuilder(URI.create(statisticsUri))
                .header("authorization", "Barear "+accessToken)
                .GET()
                .build();
    }
    // BALANCE
    private HttpRequest createBalanceRequest(){
        return HttpRequest.newBuilder(URI.create(balanceUri))
                .header("authorization", "Barear "+accessToken)
                .GET()
                .build();
    }

    public static HttpRequest createBalanceRequest(String balanceUri,String accessToken){
        return HttpRequest.newBuilder(URI.create(balanceUri))
                .header("authorization", "Barear "+accessToken)
                .GET()
                .build();
    }

    public static HttpRequest createSmsRequest(String recipientNumTel, String message){
        return  HttpRequest.newBuilder(URI.create(smsUri))
                .header("authorization", "Bearer "+accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"outboundSMSMessageRequest\": {" +
                        "\"address\": \"tel:+261"+recipientNumTel.substring(1)+"\"," +
                        "\"senderAddress\":\"tel:+"+ SENDER_NUMTEL +"\"," +
                        "\"outboundSMSTextMessage\": {" +
                        "\"message\": \""+message+"\"" +
                        "}" +
                        "}" +
                        "}"))
                .build();
    }

    public static HttpRequest createSmsRequest(String smsUri,String accessToken,String senderNumTel,String recipientNumTel,String message){
        return  HttpRequest.newBuilder(URI.create(smsUri))
                .header("authorization", "Bearer "+accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"outboundSMSMessageRequest\": {" +
                        "\"address\": \"tel:+261"+ recipientNumTel.substring(1)+"\"," +
                        "\"senderAddress\":\"tel:+"+senderNumTel +"\"," +
                        "\"outboundSMSTextMessage\": {" +
                        "\"message\": \""+message+"\"" +
                        "}" +
                        "}" +
                        "}"))
                .build();
    }

    public  static void send(String recipientNumTel,String message){
        String[] tab = {recipientNumTel};
        send(tab,message);
    }

    public static String execute(HttpRequest request){
        initHttpClient();
        try {
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get().body();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void initHttpClient() {
        if (httpClient==null)
            httpClient = HttpClient.newHttpClient();
    }

    public static Boolean getIsUnauthorizedOrInvalidTokenError() {
        return isUnauthorizedOrInvalidTokenError;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public  void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getAuthentificationUri() {
        return authentificationUri;
    }
    public  void setAuthentificationUri(String authentificationUri) {
        this.authentificationUri = authentificationUri;
    }
    public String getAUTHORIZATION_HEADER() {
        return AUTHORIZATION_HEADER;
    }
    // java 11 httpClient
    private static HttpClient httpClient;
    // request error
    private static Boolean isUnauthorizedOrInvalidTokenError = false;
    private static final String AUTHORIZATION_HEADER = "Basic S01LcEEyZEg0NFFJblpuV1R3MlduV2g2aDI1WEFHMjc6Vmo5d3JVNWMyejNWOXJKbA==";
    private static final String SENDER_NUMTEL ="261326874561";
    // URI
    private  String balanceUri = "https://api.orange.com/sms/admin/v1/contracts";
    private static String authentificationUri = "https://api.orange.com/oauth/v3/token";
    private static String smsUri = "https://api.orange.com/smsmessaging/v1/outbound/tel%3A%2B"+SENDER_NUMTEL+"/requests";
    private  String statisticsUri = "https://api.orange.com/sms/admin/v1/statistics";
    private  String purchaseOrdersUri = "https://api.orange.com/sms/admin/v1/purchaseorders";
    private static String accessToken;
}
