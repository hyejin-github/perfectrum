import PieChart from "./Pie Chart";
import React, { useEffect }from "react";
import { useRecoilState } from "recoil";
import styled from 'styled-components';
import { userState } from "../../atom";
import { fetchAccordClassListUser } from "../../apis/perfumeAPI";

const DivCenter = styled.div`
margin: 30px 0px 30px 0px;
text-align: center;
`;


function Pie(props){
    
    const user = useRecoilState(userState);

    let pieBody;

    const accordClassData = props.accordClassList;

    //console.log(accordClassData);
    if(accordClassData.length===0){
      pieBody = <DivCenter><h1 className="tasteAnalysis_emptyList_title fs-36">
      데이터가 없어요...
    </h1></DivCenter>
    }else{
      let sum = 0;
  
      for(let i = 0; i < accordClassData?.length; i++){
        accordClassData[i].y = accordClassData[i].accordClassCount;
        delete accordClassData[i].accordClassIdx;
        delete accordClassData[i].accordClassCount;
        sum += accordClassData[i].y;
      }
      for(let i = 0; i < accordClassData?.length; i++){
        accordClassData[i].y = Math.round(((accordClassData[i].y/sum)*100 + Number.EPSILON) * 100) / 100;
      }
      //console.log("돌아감?")
      pieBody = <PieChart data={accordClassData}/>
    }


    

  
    return(<>
    {pieBody}</>);
}

export default Pie;