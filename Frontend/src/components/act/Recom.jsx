import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';
import cookies from "react-cookies";

const Recom = async () => {
// 오늘의 향수
// 베스트 향수

const [PerfumeList, setPerfumeList] = useState(null);

const AT = cookies.load("Spring");

try{
    const response = await axios.get("http://j7c105.p.ssafy.io:8083/main",{
        headers:{
            "Context-Type": "application/json",
            "Authorization": AT,
        }
    });
    setPerfumeList(response.data);
}catch(err){
    console.log(err);
}
}

export default Recom;