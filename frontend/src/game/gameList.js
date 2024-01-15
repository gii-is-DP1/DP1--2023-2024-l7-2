import React, { useState, useEffect } from 'react';
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import getErrorModal from "../util/getErrorModal";
import { Button, ButtonGroup, Table, Form} from "reactstrap";
import { Link } from "react-router-dom";
import jwt_decode from "jwt-decode";

const jwt = tokenService.getLocalAccessToken();

export default function GameList() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [games, setGames] = useFetchState(
      [],
      `/api/v1/game`,
      jwt,
      setMessage,
      setVisible
    );
    
    const [role, setRole] = useState("");
    const [username, setUsername] = useState("");
    useEffect(() => {
      if (jwt) {
          setRole(jwt_decode(jwt).authorities[0]);
          setUsername(jwt_decode(jwt).sub);
      }
    }, [jwt])

    function handleSubmit(game) {
      
      fetch(
          "/api/v1/game/check" + (game ? "/" + game : ""),
          {
              method: "GET",
              headers: {
                  Authorization: `Bearer ${jwt}`,
                  Accept: "application/json",
                  "Content-Type": "application/json",
              }
          }
      )
      .then((response) => {
          if (response.ok) {
              fetch(
                  "/api/v1/game/join" + (game ? "/" + game : ""),
                  {
                      method: "POST",
                      headers: {
                          Authorization: `Bearer ${jwt}`,
                          Accept: "application/json",
                          "Content-Type": "application/json",
                      }
                  }
              ).then((response) => {
                  console.log(response.text)
                  if (response.ok) {
                      window.location.href = `/game/${game}`
                  } else {
                      console.log("error", response)
                  }
              })
          }
      })
      .catch((message) => alert(message));

  }

  function gamePlayers(players) {
    
    if (players.length == 0) {
      return "No players"
    }
    let res = players[0].name;

    if (players.length>1) {
      res = res + "," + players[1].name;
      if (players.length === 3){
        res = res + "," + players[2].name;
      }
    }

    return res;
  }

    
    function showGame(game) {
      const players = gamePlayers(game.players)
      return (
        <tr key={game.id}>
          <td className="text-center">{game.name}</td>
          <td className="text-center">{game.isPublic?"Public":"Private"}</td>
          <td className="text-center">{game.playerCreator?game.playerCreator.name:"No player creator"}</td>
          <td className="text-center">{game.start==null?'Waiting':(game.finish==null?'Playing':'Finished')}</td>
          <td className="text-center">{game.finish?game.finish:'not finished'}</td>
          <td className="text-center">{game.finish!=null?(game.winner_id==null?'"Tie"':game.winner_id):'-'}</td>
          <td className="text-center">{game.finish?'-':game.round}</td>
          <td className="text-center">{players}</td>
          <td className="text-center">
            {username==game.playerCreator || role=="ADMIN"?
              <ButtonGroup>
                <Button
                  size="sm"
                  color="primary"
                  aria-label={"edit-" + game.name}
                  tag={Link}
                  to={"/game/edit/" + game.id}
                >
                    <Link to={`/game/edit/${game.id}`} className="btn sm" style={{ textDecoration: "none" }}>Edit</Link>
                </Button>
                <Button
                  size="sm"
                  color="danger"
                  aria-label={"delete-" + game.name}
                  onClick={() => {
                    let confirmMessage = window.confirm("Are you sure you want to delete it?");

                    if(!confirmMessage) return;

                    fetch(`/api/v1/game/${game.id}`, {
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
                          setGames(games.filter((g) => g.id!=game.id));
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
              </ButtonGroup>
            : <tb></tb>}
            {role!="ADMIN" && game.finish==null?
              <div className="custom-button-row">
                <Button className="btn btn-dark btn-lg" outline color="warning" size="lg"
                  onClick={() => {
                    fetch(`/api/v1/game/check/${game.id}`, {
                      method: "GET",
                      headers: {
                          Authorization: `Bearer ${jwt}`,
                          Accept: "application/json",
                          "Content-Type": "application/json",
                      }
                    }).then((response) => {
                    if (response.ok) {
                      fetch(`/api/v1/game/join/${game.id}`, {
                        method: "POST",
                        headers: {
                          Authorization: `Bearer ${jwt}`,
                          Accept: "application/json",
                          "Content-Type": "application/json",
                        }
                      }).then((response) => {
                          console.log(response.text)
                          if (response.ok) {
                              window.location.href = `/game/${game.id}`
                          } else {
                              console.log("error", response)
                          }
                      })
                    }
                    }).catch((message) => alert(message));
      
                  }}>
                  Join
                </Button>
              </div>
            : <tb></tb>}
          </td>
        </tr>
      );
    }
    const gamesList =
    games.map((game) => {
      if(role === "ADMIN")
        return showGame(game)
      else if (role === "USER")
        console.log(game);
        if(game.playerCreator && game.playerCreator.user.username === username || game.isPublic)
          return showGame(game) 
    });

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="auth-page-container" style={{ height: "100vh", textAlign: "center" }}>
        <h1 className="text-center">Games</h1>        
        {modal}
        <div className="float-right">
        <Button className="btn btn-dark btn-lg" outline color="warning" size="lg">
            <Link to={`/game/edit/new`} style={{ color: 'rgb(238, 191, 47)'}}> Add Create </Link>
        </Button>
        </div>
        <br></br>
        <div>
        <Table aria-label="games" className="transparent-table">
            <thead>
              <tr>
                <th width="15%" className="text-center">Name</th>
                <th width="15%" className="text-center">Type</th>
                <th width="15%" className="text-center">State</th>
                <th width="15%" className="text-center">Finished</th>
                <th width="15%" className="text-center">Winner</th>
                <th width="15%" className="text-center">Round</th>
                <th width="15%" className="text-center">Nº players</th>
                <th width="15%" className="text-center">Players</th>
                <th width="15%" className="text-center"> </th>
              </tr>
            </thead>
            <tbody>{gamesList}</tbody>
          </Table>
        </div>
    </div>
  );


}