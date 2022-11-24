import { API } from "./index";

export const getUserProfile = async () => {
  const res = await API.get("/user");
  return res.data;
};
