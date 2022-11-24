import React, {useState} from 'react';
import styled from 'styled-components';
import Question from './Question';

const Wrapper = styled.div`
    display: block;
    //padding: 100px 0px 0px 0px;
    height: auto;
`;

const DivCenter = styled.div`
    //margin: 30px 0px 30px 0px;
    text-align: center;
`;

const PersonalPerfume = () => {
    const [id, setId] = useState(0);
    
    const parentFnc = (idx) => {
        setId(idx);
    }
    return (
        <Wrapper>
            <DivCenter>
                <Question id={id} getId={parentFnc} />
            </DivCenter>
        </Wrapper>
    );
};

export default PersonalPerfume;