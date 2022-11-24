import { useEffect } from "react";
import axios from "axios";
import qs from "qs";
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useSetRecoilState } from "recoil";
import { userProfileState } from "./atom";

import { userState } from "./atom";

const REST_API_KEY = process.env.REACT_APP_REST_API_KEY;
// const REDIRECT_URI = 'http://localhost:3000/oauth/kakao';
const REDIRECT_URI = 'http://j7c105.p.ssafy.io/oauth/kakao';

function Auth() {
    const userProfile = useRecoilState(userProfileState);
    const setUserProfile = useSetRecoilState(userProfileState);
    const coder = new URL(window.location.href).searchParams.get("code");

    let accessToken = '';

    let kToken;

    let sToken;

    let isLogin;

    const [user, setUser] = useRecoilState(userState);

    let User = user;

    const getToken2 = async () => {
        const AT = kToken;
        
        
        console.log("카카오 엑세스토큰 불러오기");
        console.log(AT);

        try{
            const response2 = await axios.get("http://j7c105.p.ssafy.io:8083/kakao",{
                headers: {
                    'Context-Type': 'application/json',
                    'authToken': AT,
                }
            });
            // console.log("isregister", typeof(response2.data.isRegist))
            console.log("스프링 엑세스토큰 저장");
            sToken = response2.data["access-token"];
            console.log(sToken);
            console.log("스프링 엑세스토큰 저장");
            isLogin = true;
            console.log(isLogin);
            User = {
                "isLogin" : isLogin,
                "sToken" : sToken,
                "kToken" : kToken,
                "isRegist" : response2.data.isRegist
            }
            setUser(User);
            const response3 = await axios.get("http://j7c105.p.ssafy.io:8083/user",{
                headers: {
                    'Authorization': sToken,
                }
            });
            console.log("디버깅", response3.data.data);
            // 유저 정보 리코일에 넣기

            const copy = JSON.parse(JSON.stringify(userProfile));
            // console.log("복제", copy[0]);

            console.log("리코일",userProfile)
            copy[0] = {...response3.data.data}
            setUserProfile(copy)
            // console.log("카피",copy)
            // console.log("리코일 테스트", copy);
            // window.location.replace("/preCheck");
            if (response2.data.isRegist === "false") {
                window.location.replace("/");         
                } else {
                window.location.replace("/preCheck");
            }
        }catch(err){
            console.log(err);
        }
    };
    const getToken = async () => {
        const payload = qs.stringify({
            grant_type: "authorization_code",
            client_id: REST_API_KEY,
            redirect_uri: REDIRECT_URI,
            code: coder,
        });
        try{
            const response = await axios.post(
                "https://kauth.kakao.com/oauth/token", payload
            );
            accessToken = response.data.access_token;

            //카카오 엑세스토큰 저장
            kToken = accessToken;
            console.log("카카오 엑세스토큰 저장");
            console.log(kToken);


            getToken2();
        }catch(err){
            console.log(err);
        }
    };

    useEffect(() => {
        getToken();
    },[]);

};
export default Auth;
