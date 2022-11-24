import { useRecoilState } from "recoil";

import { useEffect } from "react";

import { userState } from "../../atom";

import { RECOM_API, API } from "../../apis";

function Rec(){



    const user = useRecoilState(userState);
    const AT = user[0].sToken;

    const config = {
        headers: {
            Authorization: user[0].sToken,
        }
    }

    console.log(AT);
    let idx;

    const getRecommend2 = async () => {
        try{

            const response = await RECOM_API.get('/api/recommend-nmf/', 
            { params: { 'user_idx': 12 } }
            );
            
            console.log(response.data);
        }
        catch(err){
            console.log(err);
        }
    }

    const getRecommend = async () => {
        try{
            const response = await API.get('/user', config);
            idx = response.data.data.idx;
            console.log(idx);
            getRecommend2();
        }
        catch(err){
            console.log(err);
        }
    }

    useEffect(() => {
        getRecommend();
    },[]);
}

export default Rec;