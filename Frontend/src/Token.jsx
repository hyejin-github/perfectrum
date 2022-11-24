import axios from "axios";
// import qs from "qs";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import cookies from "react-cookies";

function Token(){

    const AT = cookies.load("Kakao");

    const navigate = useNavigate();

    const getToken = async () => {
        const headers = {
            'authToken': AT,
        };

        try{
            const response = await axios.get(headers, "http://j7c105.p.ssafy.io:8083/kakao");
            console.log(response.data.isRegist);
            navigate("/")
        }catch(err){
            console.log(err);
        }
    };

    useEffect(() => {
        getToken();
    },[]);

    return null;
}

export default Token;