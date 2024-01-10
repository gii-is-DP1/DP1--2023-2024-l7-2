import React from "react";
import { Link } from "react-router-dom";
import "../../static/css/auth/authButton.css";
import "../../static/css/auth/authPage.css";
import tokenService from "../../services/token.service";

const Logout = () => {

  const jwt = tokenService.getLocalAccessToken();

  async function logout() {
    await fetch(`/api/v1/users/${tokenService.getUserName()}/logout`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      }
    })
    .then(function (data) {
      tokenService.setUser(data);
      tokenService.updateLocalAccessToken(data.token);
    })
    .catch((message) => alert(message));;      

  }
  function sendLogoutRequest() {
    const jwt = window.localStorage.getItem("jwt");
    if (jwt || typeof jwt === "undefined") {
      logout();
      tokenService.removeUser();
      window.location.href = "/";
    } else {
      alert("There is no user logged in");
    }
  }

  return (
    <div className="auth-page-container"  style={{height: "100vh"}}>
      <div className="text-center">
        <h2 className="text-center text-md">
          Are you sure you want to log out?
        </h2>
        <div className="options-row">
          <Link className="auth-button" to="/" style={{textDecoration: "none"}}>
            No
          </Link>
          <button className="auth-button" onClick={() => sendLogoutRequest()}>
            Yes
          </button>
        </div>
      </div>
    </div>
  );
};

export default Logout;
