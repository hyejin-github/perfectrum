package com.perfectrum.backend.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest
@Transactional
public class KakaoUserServiceTest {

    @Test
    public void 카카오_엑세스토큰_발급_test(){
        String code = "eFdeRTE1cEz5q0Uu1ibhSTW94WRIkgBfk_7zJc6KCTENk3Fy4nJi58TNbkd2_WfWWt02Ywo9c04AAAGDU8ADIQ";
        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        String apiKey = "cd27f568a3b025f91f69fd043b1d57f1";
        String redirectUrl = "http://localhost:8083/kakao";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // request parameter 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+apiKey);
            sb.append("&redirect_uri="+redirectUrl);
            sb.append("&code="+code);
            bw.write(sb.toString());
            bw.flush();

            // 결과코드 200 - 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // JSON 타입의 Response 메시지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while((line = br.readLine()) != null){
                result += line;
            }

            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);

            br.close();
            bw.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
