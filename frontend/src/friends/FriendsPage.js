import React, { useState, useEffect } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";

import { Form, Button, Input, Label  } from "reactstrap";
import { Link } from "react-router-dom";

const jwt = tokenService.getLocalAccessToken();

export default function FriendList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [friends, setFriends] = useFetchState(
    [],
    `/api/v1/friends`,
    jwt,
    setMessage,
    setVisible
  );

  const [req, setReq] = useState({
    id: null,
    sender: null,
    receiver: null,
    status: null,
    sendTime:null
  })
  
  const user = tokenService.getUser();
  console.log(friends)
  function acceptRequest(url) {
    let confirmMessage = window.confirm("Are you sure you want to accept the request?");
    if (confirmMessage) {
      fetch(url, {
        method: "PUT",
        headers: {
          "Authorization": `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(req)
      })
      
      .then((response) => {
        console.log('Response:', response);
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json().catch(() => ({}));
      })
      .then((data) => {
        console.log('Data:', data);
        if (data && data.message) {
          setMessage(data.message);
          setVisible(true);
        }})
        .catch((error) => {
          console.error('Error:', error);
          if (error instanceof SyntaxError) {
            console.error('SyntaxError. Response body:', error.body);
          }
          alert(error.message);
        });
    }
  }

  function deniedRequest(url) {
    let confirmMessage = window.confirm("Are you sure you want to denie the request?");
    if (confirmMessage) {
      fetch(url, {
        method: "PUT",
        headers: {
          "Authorization": `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(req)
      })
      
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json().catch(() => ({}));
      })
      .then((data) => {
        if (data && data.message) {
          setMessage(data.message);
          setVisible(true);
        }})
        .catch((error) => {
          console.error('Error:', error);
          if (error instanceof SyntaxError) {
            console.error('SyntaxError. Response body:', error.body);
          }
          alert(error.message);
        });
    }
  }

function deleteFriends(url, id) {
  let confirmMessage = window.confirm("Are you sure you want to delete this friend?");
  if (confirmMessage) {
    fetch(url, {
      method: "DELETE",
      headers: {
        "Authorization": `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      }
    })
    .then((response) => {
      setFriends(friends.filter((i) => i.id !== id));
      return response.text();
    })
      .catch((err) => {
        console.log(err);
        alert("Error accepting request");
      });
  }
}

const friendRequest = friends.map((request) => (
  <tr key={request.id}>
    <td>{request.receiver.username === user.username
        ? request.sender.username
        : null}</td>
    <td>
    {request.sender.username === user.username
        ? request.receiver.username
        : null}
    </td>
    <td>{request.status.name}</td>
    <td>
      {request.status.id === 1 && request.receiver.username === user.username ? (
        <h1>
          <Button outline color="success" margin="10px" >
            <Link
              to="/friends"
              onClickCapture={() => acceptRequest(`/api/v1/friends/${request.id}`)}
              className="btn sm"
              style={{ textDecoration: "none" }}
            >
              Accept
            </Link>
          </Button>
          <Button outline color="danger" margin="15px">
            <Link
              to="/friends"
              onClickCapture={() => deniedRequest(`/api/v1/friends/${request.id}`)}
              className="btn sm"
              style={{ textDecoration: "none" }}
            >
              Rechazar
            </Link>
          </Button>
        </h1>
      ) : null}
    </td>
  </tr>
));

const friendList = friends.map((request) => (
  <tr key={request.id}>
    <td>
      {request.status.name === 'Accepted' ? (
        <h2>{request.receiver.username == user.username? request.sender.username: request.receiver.name}</h2>
      ) : null}
    </td>
    <td>
      {request.status.id === 2 ? (
        <h1>
          {/* Botón solo para solicitudes aceptadas */}
          <Button outline color="danger" margin="15px">
            <Link
              to="/friends"
              onClickCapture={() => deleteFriends(`/api/v1/friends/${request.id}`, request.id)}
              className="btn sm"
              style={{ textDecoration: "none" }}
            >
              Delete friend
            </Link>
          </Button>
        </h1>
      ) : null}
    </td>
  </tr>
));

  return (
    <div className="auth-page-container" style={{ height: "100vh", marginTop: "65px", textAlign: "center" }}>
      <div className="friends" style={{ marginTop: "10px" }}>
        <Button outline color="success" margin="10px" style = {{fontSize: "12px"}}>
              <Link
                to={`/friendRequest/SentRequest`}
                className="btn sm"
                style={{ textDecoration: "none" }}
              >
                To sent request to someone
              </Link>
            </Button>
        <h3>Friend Requests</h3>
        <table>
          <thead>
            <tr>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                Sender User
              </th>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                Receiver User
              </th>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                Status
              </th>
            </tr>
          </thead>
          <tbody>{friendRequest}</tbody>
        </table>
        <h3>Friend List</h3>
        <table>
          <tbody>{friendList}</tbody>
        </table>
      </div>
    </div>
  );
};
