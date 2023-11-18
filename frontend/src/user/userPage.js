import React, { useState, useEffect } from 'react';
import tokenService from "../services/token.service";

import { Button } from "reactstrap";

import { Link } from "react-router-dom";

import useFetchState from "./../util/useFetchState";

const user = tokenService.getUser();
const jwt = tokenService.getLocalAccessToken();


const UserProfile = () => {

  console.log(user);
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
  console.log(user)
  return (
    <div className="auth-page-container" style={{height: "100vh"}}>
      
      <h1>My profile</h1>
      
      <h4 style={{ listStyleType: 'none', padding: 0, margin: 0 }}>
        <li>Username: {user.username}</li>
        <li>Nombre: {user.name}</li>
        
      </h4>

      <div className="col text-center" style={{marginTop: "20px"}}>
        <Button outline color="success" margin="15px">
          <Link to={`/user/userEdit`} className="btn sm" style={{ textDecoration: "none" }}>Edit</Link>
        </Button>
        <Button outline color="danger" margin="15px">
            <Link to="/home" onClickCapture={() => deleteUser(`/api/v1/owners/${user.id}`,user.id, user)} 
                      className="btn sm" style={{ textDecoration: "none" }}>Delete</Link>
        </Button>
      </div>

    </div>
  );
};

export default UserProfile;
