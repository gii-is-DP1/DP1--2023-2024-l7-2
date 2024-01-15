import React, { useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";

import { Button, ButtonGroup, Table  } from "reactstrap";
import getErrorModal from "../util/getErrorModal";
import RequestFormModel from "./RequestFormModel";

const jwt = tokenService.getLocalAccessToken();

export default function FriendList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [showRequestForm, setShowRequestForm] = useState(false)
  const [friends, setFriends] =  useFetchState(
    [],
    `/api/v1/friends/accepted`,
    jwt,
    setMessage,
    setVisible
  );

  const [requests, setRequests] =  useFetchState(
    [],
    `/api/v1/friends/pending`,
    jwt,
    setMessage,
    setVisible
  );

  const [games, setGames] = useFetchState(
    [],
    `/api/v1/friends/friendGames`,
    jwt,
    setMessage,
    setVisible
  );

  const handleJoinClick = (game) => {
  
    fetch(
      "/api/v1/game/spectate" + (game ? "/" + game : ""),
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        }
      }
    ).then((response) => {
      if (response.ok) {
        return response.json()
      } 
    })
    .then((response) => {
      console.log(response)
      if (response.code) {
        window.location.href = `/espectate/${response.code}`;
      } else {
        setMessage("Failed to join the game")
        setVisible(true)
        throw new Error("Failed to join the game");
      }
    })
    .catch((error) => {
        setMessage("Failed to join the game")
        setVisible(true)
      setMessage(error.message);
    });
  };

  const espectateList = games.map((game) => {
    if (game.finish === null) {
        return (
            <div key={game.id} style={{ border: '2px solid black', borderRadius: '8px', marginBottom: '15px', padding: '5px', margin: '5px', color: 'black'}}>
                {game.name} - Players: {game.players.length}
                <Button className="btn btn-dark btn-sm" style={{marginLeft: '5px'}} onClick={() =>  handleJoinClick(game.code)}>Join</Button>
            </div>
        );
    } else {
        return null;
    }
});

  function showFriend(req) {
    return (
      <div key={req.id} style={{ border: '2px solid black', borderRadius: '8px', marginBottom: '15px', padding: '5px', margin: '5px', color: 'black'}}>
        <td className="text-center">{req.receiver.username!==tokenService.getUsername? req.sender.username : req.receiver.username}</td>
        <td className="text-center">
          {req.sender.username!==tokenService.getUsername ?
              <ButtonGroup>
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
                <Button
                  size="sm"
                  color="dark"
                  aria-label="Block"
                  onClick={() => {
                    let confirmMessage = window.confirm("Are you sure you want block this user?");
            
                    if(!confirmMessage) return;
                    fetch(`/api/v1/friends/${req.id}/block`, {
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
                  Block
                </Button>
              </ButtonGroup>
          : <tb></tb>}
        </td>
      </div>
    );
  }
  
  function showRequest(req) {
    return (
      <div key={req.id} style={{ border: '2px solid black', borderRadius: '8px', marginBottom: '15px', padding: '5px', margin: '5px', color: 'black'}}>
        <td className="text-center">{req.sender.username}</td>
        <td className="text-center">
          {req.sender.username!=tokenService.getUserName ?
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
                  color="primary"
                  aria-label="Accept"
                  onClick={() => {
                    let confirmMessage = window.confirm("Are you sure you want accept this friendship petition?");

                    if(!confirmMessage) return;

                    fetch(`/api/v1/friends/${req.id}/accept`, {
                      method: "PUT",
                      headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${jwt}`,
                      },
                    })
                      .then((res) => {
                        if (res.status === 200) {
                          setMessage("Petition accept");
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
                <Button
                  size="sm"
                  color="dark"
                  aria-label="Block"
                  onClick={() => {
                    let confirmMessage = window.confirm("Are you sure you want block this user?");
            
                    if(!confirmMessage) return;
                    fetch(`/api/v1/friends/${req.id}/block`, {
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
                  Block
                </Button>
              </ButtonGroup>
          : <tb></tb>}
        </td>
      </div>
    );
  }

  const currentFriends =
  friends.map((request) => {
    if(friends.receiver!==tokenService.getUser())
      return showFriend(request)
  });

  const sentRequests =
  requests.map((request) => {
    if(requests.sender!==tokenService.getUser())
      return showRequest(request)
  });


  return (
    <>
    <RequestFormModel
    isOpen={showRequestForm}
    toggle={() => {
      setShowRequestForm(!showRequestForm)
      console.log(jwt)
    }}
  ></RequestFormModel>
  
  {getErrorModal(setVisible,visible,message)}

    <div>
      <div className="auth-page-container" style={{ height: "100vh", textAlign: "center" }}>
        <br></br>   
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
          <div className="row">
            <div className="col d-flex flex-column align-items-center justify-content-center" style={{marginRight:"50px", marginLeft:"100px"}}>
              <h2 className="text-center">Friends</h2>
              <Table aria-label="friends" className="mt-4">
                <tbody>{currentFriends}</tbody>
              </Table>
            </div>

            <div className="col d-flex flex-column align-items-center justify-content-center" style={{marginRight:"50px", marginLeft:"100px"}}>
              <h2 className="text-center">Requests</h2>
              <Table aria-label="requests" className="mt-4">
                <tbody>{sentRequests}</tbody>
              </Table>
            </div>

            <div className="col d-flex flex-column align-items-center justify-content-center" style={{marginRight:"50px", marginLeft:"100px"}}>
              <h2 className="text-center">Games to spectate</h2>
              <Table aria-label="spectators" className="mt-4">
                <tbody>{espectateList}</tbody>
              </Table>
            </div>
          </div>
      </div>
    </div>
    </>
  );
};
