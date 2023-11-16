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
    <div style={{ textAlign: 'center', padding: 0 }}>
      <h2 style={{ margin: 0 }}>My profile</h2>
      <ul style={{ listStyleType: 'none', padding: 0, margin: 0 }}>
        <li>Username: {user.username}</li>
        <li>Nombre: {user.name}</li>
        
      </ul>
      <Button outline color="success" >
                    <Link to={`/user/userEdit`} className="btn sm" style={{ textDecoration: "none" }}>Edit</Link>
      </Button>
      <Button outline color="danger" >
                    <Link to="/home" onClickCapture={() => deleteUser(`/api/v1/owners/${user.id}`,user.id, user)} 
                    className="btn sm" style={{ textDecoration: "none" }}>Delete</Link>
      </Button>
    </div>
  );
};

export default UserProfile;
