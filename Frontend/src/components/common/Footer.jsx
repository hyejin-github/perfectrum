import React from "react";
import styled from "styled-components";
import notion from "../../assets/images/logo/notion.png";
import { Col, Container, Row } from "react-bootstrap";

const Footer = styled.footer`
  background-color: black;
  text-align: center;
  padding: 5px;
`;

const ContentHeader = styled.h1`
  margin-top: 40px;
  padding: 5px;
  font-size: 32px;
  color: white;
`;

const DivCenter = styled.div`
margin-top:40px;
border: 5px;
padding: 5px;
color:white;
`


const FooterPage = () => {
  return (
    <Footer>
      <Container>
        <Row>
          <Col xs={4}></Col>
          <Col xs={12} md={4}>
            <ContentHeader className="text-uppercase">Contact Us</ContentHeader>
            <br></br>
            <p>
              <a href="https://www.notion.so/PJT2_C105_-8ba2738dcb49470ba9f4e9c1fd1fd88f">
                <img src={notion} width="50px" />
              </a>
            </p>
          </Col>
          <Col xs={4}></Col>
        </Row>
      </Container>

      <DivCenter className="text-center p-3">
        &copy; {new Date().getFullYear()} Copyright:{" "}
        <a className="text-white" href="https://edu.ssafy.com/">
          SSAFY.com
        </a>
      </DivCenter>
    </Footer>
  );
};

export default FooterPage;
