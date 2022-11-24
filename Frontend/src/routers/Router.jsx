import React from "react";
import { Route, Routes } from "react-router-dom";

import MainNavBar from "@components/common/MainNavBar";
import Home from "@screens/Home";
import InfoEdit from "@screens/user/InfoEdit";
import UserReview from "@screens/user/UserReview";
import PerfumeSearch from "@screens/perfume/PerfumeSearch";
import PerfumeDetail from "@screens/perfume/PerfumeDetail";
import TasteAnalysis from "@screens/perfume/TasteAnalysis";
import TasteInfo from "@screens/perfume/TasteInfo";
import Login from "@screens/Login";
import Footer from "@components/common/Footer";
import Rec from "@components/common/Rec";
import StickyButton from "@components/common/StickyButton";
import Test from "@components/common/Test";
import { RecoilRoot } from "recoil";

import Auth from "../Auth";
import Token from "../Token";
import PersonalPerfume from "../screens/PersonalPerfume";
import PollStart from "../screens/poll/PollStart";
import PollResult from "../screens/poll/PollResult";
import Survey from "../screens/user/Survey";
import PreCheck from "../screens/user/PreCheck";

function Router() {
  return (
    <RecoilRoot>
      <MainNavBar />
      <Routes>
        <Route path="/" element={<Home />} />
        {/* user */}
        <Route path="/infoedit" element={<InfoEdit />} />
        <Route path="/userreview/:nickname" element={<UserReview />} />
        {/* perfume */}
        <Route path="/perfumesearch" element={<PerfumeSearch />} />
        <Route path="/tasteInfo" element={<TasteInfo />} />
        <Route path="/detail/:id" element={<PerfumeDetail />} />
        <Route path="/tasteanalysis" element={<TasteAnalysis />} />
        <Route path="/login" element={<Login />} />
        <Route path="/oauth/kakao" element={<Auth />} />
        <Route path="/oauth/token" element={<Token />} />
        <Route path="/personal" element={<PersonalPerfume />} />
        <Route path="/poll" element={<PollStart />} />
        <Route path="/pollresult" element={<PollResult />} />
        <Route path="/getRec" element={<Rec></Rec>}></Route>
        <Route path="/survey" element={<Survey />} />
        <Route path="/preCheck" element={<PreCheck />} />
      </Routes>
      <Footer />
      <StickyButton />
      {/* <Test /> */}
    </RecoilRoot>
  );
}
export default Router;
