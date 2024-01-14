import React, { useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";

import { Button, ButtonGroup, Table  } from "reactstrap";
import { Link } from "react-router-dom";
import RequestFormModel from "./RequestFormModel";

const jwt = tokenService.getLocalAccessToken();

export default function FriendList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [showRequestForm, setShowRequestForm] = useState(false)
  const [requests, setRequests] =  useFetchState(
    [],
    `/api/v1/friends`,
    jwt,
    setMessage,
    setVisible
  );

  function showRequest(req) {
    return (
      <tr key={req.id}>
        <td className="text-center">{req.receiver.username}</td>
        <td className="text-center">{req.sender.username}</td>
        <td className="text-center">{req.status}</td>
        <td className="text-center">
          {req.sender.username!==tokenService.getUsername ?
              <Button
                size="sm"
                color="danger"
                aria-label="Delete"
                onClick={() => {
                  let confirmMessage = window.confirm("Are you sure you want to delete it?");

                  if(!confirmMessage) return;

                  fetch(`/api/v1/friends/${req.id}`, {
                    method: "DELETE",
                    headers: {
                      "Content-Type": "application/json",
                      Authorization: `Bearer ${jwt}`,
                    },
                  })
                    .then((res) => {
                      if (res.status === 204) {
                        setMessage("Deleted successfully");
                        setVisible(true);
                        setRequests(requests.filter((r) => r.id!=req.id));
                      }
                    })
                    .catch((err) => {
                      setMessage(err.message);
                      setVisible(true);
                    });
                }}
              >
                Delete
              </Button>
          : <tb></tb>}
          {req.receiver.username==tokenService.getUsername ?
            <div className="custom-button-row">
              <ButtonGroup>
                <Button
                  size="sm"
                  color="danger"
                  aria-label="Deny"
                  onClick={() => {
                    let confirmMessage = window.confirm("Are you sure you want dismiss this friend petition?");

                    if(!confirmMessage) return;

                    fetch(`/api/v1/friends/${req.id}/deny`, {
                      method: "PUT",
                      headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${jwt}`,
                      },
                    })
                      .then((res) => {
                        if (res.status === 200) {
                          setMessage("Petition enied");
                          setVisible(true);
                          setRequests(requests.filter((r) => r.id!=req.id));
                        }
                      })
                      .catch((err) => {
                        setMessage(err.message);
                        setVisible(true);
                      });
                  }}
                >
                  Deny
                </Button>
                <Button
                  size="sm"
                  color="green"
                  aria-label="Accept"
                  onClick={() => {

                    fetch(`/api/v1/friends/${req.id}/accept`, {
                      method: "PUT",
                      headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${jwt}`,
                      },
                    })
                      .then((res) => {
                        if (res.status === 200) {
                          setMessage("Petition accepted");
                          setVisible(true);
                          setRequests(requests.filter((r) => r.id!=req.id));
                        }
                      })
                      .catch((err) => {
                        setMessage(err.message);
                        setVisible(true);
                      });
                  }}
                >
                  Accept
                </Button>
              </ButtonGroup>
            </div>
          : <tb></tb>}
        </td>
      </tr>
    );
  }

  const receivedRequests =
  requests.map((request) => {
    if(requests.receiver!==tokenService.getUser())
      return showRequest(request)
  });

  const sentRequests =
  requests.map((request) => {
    if(requests.sender==tokenService.getUser())
      return showRequest(request)
  });

  <RequestFormModel
    isOpen={showRequestForm}
    toggle={() => {
      setShowRequestForm(!showRequestForm)
      console.log(jwt)
    }}
  ></RequestFormModel>



  return (
    <div>
      <div className="admin-page-container" style={{marginTop: "70px"}}>
        <h1 className="text-center">Social hub</h1>        
        <Button 
            onClick={() => {setShowRequestForm(!showRequestForm)}}
            title="Send request"
            className="btn btn-dark btn-lg" 
            outline color="warning" 
            size="lg">
            Send request
        </Button>
        {/* <Link
          to={`/friendRequest/SentRequest`}
          className="btn btn-dark btn-lg" 
          outline color="warning">
          Send request
        </Link> */}
        <br></br>
        <div>
          <h2 className="text-center">Friends</h2>  
          <Table aria-label="friends" className="mt-4">
            <thead>
              <tr>
                <th width="15%" className="text-center">Receiber</th>
                <th width="15%" className="text-center">Sender</th>
                <th width="15%" className="text-center">Status</th>
              </tr>
            </thead>
            <tbody>{receivedRequests}</tbody>
          </Table>
        </div>
        <br></br>
        <div>
          <h2 className="text-center">Requests</h2>  
          <Table aria-label="requests" className="mt-4">
            <thead>
              <tr>
                <th width="15%" className="text-center">Receiver</th>
                <th width="15%" className="text-center">Sender</th>
                <th width="15%" className="text-center">State</th>
              </tr>
            </thead>
            <tbody>{sentRequests}</tbody>
          </Table>
        </div>
      </div>
    </div>
  );
};
