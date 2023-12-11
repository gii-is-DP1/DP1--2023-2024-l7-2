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
    status: {id:1, name:"Accepted"},
    sendTime:null
  })
  
  const user = tokenService.getUser();

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
        window.location.reload();
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
        window.location.reload();
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
      window.location.reload();
      return response.text();
    })
      .catch((err) => {
        console.log(err);
        alert("Error accepting request");
      });
  }
}

function blockFriend(url) {
  let confirmMessage = window.confirm("Are you sure you want to block this friend?");
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
      window.location.reload();
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
              onClickCapture={() => {setReq(req.id = request.id)
                setReq(req.sender = request.sender)
                setReq(req.receiver = request.receiver)
                setReq(req.status = {id: 2, name: 'Accepted'})
                setReq(req.sendTime = request.sendTime)
              acceptRequest(`/api/v1/friends/${request.id}`)}}
              className="btn sm"
              style={{ textDecoration: "none" }}
            >
              Accept
            </Link>
          </Button>
          <Button outline color="danger" margin="15px">
            <Link
              to="/friends"
              onClickCapture={() => {
                setReq(req.id = request.id)
                setReq(req.sender = request.sender)
                setReq(req.receiver = request.receiver)
                setReq(req.status = {id: 3, name: 'Denied'})
                setReq(req.sendTime = request.sendTime)
                deniedRequest(`/api/v1/friends/${request.id}`)}}
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
          <Button outline color="danger" margin="15px">
            <Link
              to="/friends"
              onClickCapture={() => {
                setReq(req.id = request.id)
                setReq(req.sender = request.sender)
                setReq(req.receiver = request.receiver)
                setReq(req.status = {id: 4, name: 'Blocked'})
                setReq(req.sendTime = request.sendTime)
                blockFriend(`/api/v1/friends/${request.id}`, request.id)}}
              className="btn sm"
              style={{ textDecoration: "none" }}
            >
              Block friend
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
              <Link
                to={`/friendRequest/SentRequest`}
                className="auth-button"
                style={{ textDecoration: "none" }}
              >
                To send request to someone
              </Link>
              <Link
                to={`/friendRequest/BlockRequest`}
                className="auth-button"
                style={{ textDecoration: "none" }}
              >
                To block someone
              </Link>
        <h3 style={{ marginTop: "30px" }}>Friend Requests</h3>
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
