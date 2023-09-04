// import { API_URL } from "$lib/utils/constants";
//
// export const POST = async (url, json) => {
//   let output = await fetch(url, {
//     method: "POST",
//     body: json,
//     headers: {
//       "Content-type": "application/json",
//     },
//   }).then((response) => response.json());
//   return output;
// };
// export const AUTH_POST = async (url, json, token) => {
//   let output = await fetch(url, {
//     method: "POST",
//     body: json,
//     headers: {
//       "Content-type": "application/json",
//       Cookie: `sessionid=${token}`,
//     },
//   }).then((response) => response.json());
//   return output;
// };
//
// //@_deprecated
// export const SELF_AUTH_POST = async (url, body, token) => {
//   let output = await self
//     .fetch(url, {
//       method: "POST",
//       body: body,
//       headers: {
//         "Content-type": "application/json",
//       },
//
//       headers: {
//         Cookie: `sessionid=${token}`,
//       },
//     })
//     .then((response) => response.json());
//   return output;
// };
//
// export const AUTH_GET = async (url, token) => {
//   let output = await fetch(url, {
//     method: "GET",
//     headers: {
//       "Content-type": "application/json",
//     },
//     credentials: "include",
//     headers: {
//       Cookie: `sessionid=${token}`,
//     },
//   }).then((response) => response.json());
//   return output;
// };
//
// export let db = {
//   getUserFromSession: async function (sessionId) {
//     let output = await POST(
//       API_URL + "getUser/token",
//       JSON.stringify({
//         token: sessionId,
//       }),
//     );
//
//     if (output != null && output.status != "ERR_INVALID_TOKEN") {
//       return {
//         name: output.user.name,
//         email: output.user.email,
//         dashboardData: output.user.dashboardData,
//         verified: output.user.verified,
//         status: output.status,
//       };
//     } else {
//       return output;
//     }
//   },
//   saveDashBoard: async function (dashboardData, token) {
//     let output = await POST(
//       API_URL + "user/saveDashboard",
//       JSON.stringify({
//         data: dashboardData,
//         token: token,
//       }),
//     );
//
//     return output.status;
//   },
//   createSession: async function (user) {
//     return "session";
//   },
//   isLoggedIn: async function () {
//     return await getUserFromSession();
//   },
//
//   register: async function (name, email, password) {
//     let formData = new FormData();
//     formData.append("email", email);
//     formData.append("password", password);
//     formData.append("name", name);
//
//     let output = await POST(
//       API_URL + "register",
//       JSON.stringify({
//         name: name,
//         email: email,
//         password: password,
//       }),
//     );
//
//     return output;
//   },
//
//   login: async function (email, password) {
//     let output = await POST(
//       API_URL + "login",
//       JSON.stringify({
//         password: password,
//         email: email,
//       }),
//     );
//     return output;
//   },
//   userWithMailExists: async function (email) {
//     let output = await fetch(API_URL + "user/emailExists/" + email).then(
//       (response) => response.json(),
//     );
//
//     return output.exists == true;
//   },
//   logout: async function (token) {
//     let output = await POST(
//       API_URL + "logout",
//       JSON.stringify({
//         token: token,
//       }),
//     );
//
//     return output;
//   },
//   verify: async function (token) {
//     let output = await self
//       .fetch(API_URL + `verify?key=${token}`)
//       .then((res) => res.json());
//     return output;
//   },
//   getData: async function (token) {
//     let output = await AUTH_GET(API_URL + `dashboard/getData`, token);
//
//     return await output;
//   },
//   async getNotifications(token) {
//     let output = await AUTH_GET(API_URL + `dashboard/getNotifications`, token);
//     return output;
//   },
//
//   renameBeehive: async (token, name, beehive_id) => {
//     let output = await AUTH_POST(
//       API_URL + `dashboard/renameBeehive`,
//       JSON.stringify({
//         name: name,
//         token: beehive_id,
//       }),
//       token
//     );
//     return output;
//   },
//
//   saveReminder: async (date) => {
//     let output = await AUTH_POST(
//       API_URL + `dashboard/newReminder`,
//       JSON.stringify({
//         name: name,
//         token: beehive_id,
//       }),
//       token
//     );
//     return output;
//   },
//
//   async changeInterval(token, interval, beehive_id) {
//     let output = await AUTH_POST(
//       API_URL + `dashboard/changeInterval`,
//       JSON.stringify({
//         interval: interval,
//         token: beehive_id,
//       }),
//       token,
//     );
//     return output;
//   },
// };
