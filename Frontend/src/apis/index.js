// import axios from "axios";

// import { useRecoilState } from "recoil";
// import { userState } from "../atom";

// const BASE_URL = "http://j7c105.p.ssafy.io:8083";

// const user = useRecoilState(userState);

// const AT = user.sToken;

// const createAPI = () => {
//   const API = axios.create({
//     baseURL: BASE_URL,
//     headers:{
//       'Authorization' : AT,
//     }
//   });

//   return API;
// };

// export default createAPI;
import axios from "axios";
// import cookies from "react-cookies";

export const BASE_URL = "http://j7c105.p.ssafy.io:8083";
// export const coo = cookies.load("Spring");

export const RECOM_URL = "http://j7c105.p.ssafy.io:8080";

export const API = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Access-Control-Allow-Origin": "http://j7c105.p.ssafy.io:8083",
    // Authorization: `Bearer-${localStorage.getItem("recoil-persist")}`
  }
});

export const RECOM_API = axios.create({
  baseURL: RECOM_URL,
});

// API.interceptors.response.use(
//   response => response,
//   async error => {
//     if (
//       error.response.status === 401 &&
//       error.response.data.error === "Unauthorized"
//     ) {
//       const refreshToken = await sessionStorage.getItem("refreshToken");
//       const res = await axios({
//         url: "http://j7c105.p.ssafy.io:8083/token/silentRefresh",
//         method: "post",
//         headers: {
//           "Content-Type": "application/json"
//         },
//         data: {
//           refreshToken: `Bearer-${refreshToken}`
//         }
//       });
//       const originRequest = error.config;
//       originRequest.headers.Authorization = `Bearer-${res.data.accessToken}`;
//
//       sessionStorage.setItem("accessToken", res.data.accessToken);
//       sessionStorage.setItem("refreshToken", res.data.refreshToken);
//       return axios(originRequest);
//     }
//     return Promise.reject(error);
//   }
// );

export const ex = () => {};

