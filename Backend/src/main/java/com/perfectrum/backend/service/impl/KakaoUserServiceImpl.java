package com.perfectrum.backend.service.impl;

import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.repository.UserRepository;
import com.perfectrum.backend.service.KakaoUserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoUserServiceImpl implements KakaoUserService {
    private UserRepository userRepository;

    @Autowired
    KakaoUserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public String getKakaoAccessToken(String code) throws IOException { // 카카오 자체 accesstoken 발급
        String accessToken = "";

        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=cd27f568a3b025f91f69fd043b1d57f1");
            sb.append("&redirect_uri=http://localhost:8083/oauth/kakao");
//            sb.append("&redirect_uri=http://j7c105.p.ssafy.io:8083/oauth/kakao");
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

//            int responseCode = urlConnection.getResponseCode();
//            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("result = " + result);

            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("access_token = " + access_token);

            accessToken = access_token;

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    @Override
    public String[] createKakaoUser(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String[] jsonId = new String[2];
        // accessToken을 이용해서 사용자 정보 조회
        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while((line = br.readLine()) != null){
                result += line;
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            jsonId[0] = json.get("id").toString();
            String nameId = "k" + jsonId[0].substring(1,10);
            jsonId[0] = "kakao" + jsonId[0];
            // DB에서 회원인지 찾기
            Optional<UserEntity> userEntity = userRepository.findByUserId(jsonId[0]);
            // 존재하지 않는다면 -> 회원가입
            if(!userEntity.isPresent()){
                UserEntity user = UserEntity.builder()
                        .userId(jsonId[0])
                        .nickname(nameId)
                        .build();
                userRepository.save(user);
                jsonId[1] = "true";
            }else{
                jsonId[1] = "false";
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (ParseException e){
            throw new RuntimeException(e);
        }

        return jsonId;
    }

}
