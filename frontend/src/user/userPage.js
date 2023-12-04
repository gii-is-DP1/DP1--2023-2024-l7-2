import React, { useState, useEffect } from 'react';
import tokenService from "../services/token.service";

import { Button } from "reactstrap";

import { Link } from "react-router-dom";

import useFetchState from "./../util/useFetchState";

const user = tokenService.getUser();
const jwt = tokenService.getLocalAccessToken();


const UserProfile = () => {

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);

  function sendLogoutRequest() {
    const jwt = window.localStorage.getItem("jwt");
    if (jwt || typeof jwt === "undefined") {
      tokenService.removeUser();
      window.location.href = "/";
    } else {
      alert("There is no user logged in");
    }
  }

  function deleteUser(url,id,ownerData) {
    let confirmMessage = window.confirm("Are you sure you want to delete it?");
    if (confirmMessage) {
        fetch(url, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                return response.text();
            })
            .catch((err) => {
                console.log(err);
                alert("Error deleting entity")
            });
    }
    }

  return (
    <div className="auth-page-container" style={{height: "100vh", marginTop: "60px", textAlign: "center"}}>

      <h1>My profile</h1>
      <div className="user-containe" style={{display:"flex",flexDirection:"row", width:"100vw", justifyContent: "space-around"}}>
        <div>
          <h4 style={{ listStyleType: 'none', padding: 0, margin: 0 }}>
            <li>Username: {user.username}</li>
          </h4>

          <div className="col text-center" style={{marginTop: "20px"}}>
            <Button outline color="success" margin="15px">
              <Link to={`/user/userEdit`} className="btn sm" style={{ textDecoration: "none" }}>Edit</Link>
            </Button>
            <Button outline color="danger" margin="15px">
                <Link to="/" onClickCapture={() => {deleteUser(`/api/v1/users/${user.id}`,user.id, user); sendLogoutRequest() }} 
                          className="btn sm" style={{ textDecoration: "none" }}>Delete</Link>
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default UserProfile;