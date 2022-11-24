import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage";
import firebase from "firebase/compat/app";
import "firebase/compat/auth";
import "firebase/compat/storage";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyDEDC8PjI6ErFNCLqLErGBSqMAKDXdtwbw",
  authDomain: "frangrance-df69e.firebaseapp.com",
  projectId: "frangrance-df69e",
  storageBucket: "frangrance-df69e.appspot.com",
  messagingSenderId: "97208816836",
  appId: "1:97208816836:web:12ff0f25d6569f097da591",
  measurementId: "G-0W2KJZ1NNL"
};

// Initialize Firebase
// export const app = initializeApp(firebaseConfig);
// export const storage = getStorage(app);
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
// const analytics = getAnalytics(app);

const auth_obj = firebase.auth();
const storage_obj = firebase.storage();

export default firebase;
export const auth = auth_obj;
export const storage = storage_obj;