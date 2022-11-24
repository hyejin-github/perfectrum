/* eslint-disable import/prefer-default-export */
import { API } from "./index";

export const getMain = async () => {
  const res = await API.get("/main");
  // console.log(res.data);
  return res.data;
};
export const getDetail = async (perfumeId, body) => {
  const res = await API.post(`/detail/${perfumeId}`, body);
  // console.log(res);
  return res.data;
};
export const getAccordClass = async accordClassIdx => {
  const res = await API.get(`/main/docs/${accordClassIdx}`);
  // console.log(res);
  return res.data;
};
