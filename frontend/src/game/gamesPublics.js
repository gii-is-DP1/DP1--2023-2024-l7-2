import React, { useState, useEffect } from 'react';
import tokenService from "../services/token.service";
import jwtDecode from 'jwt-decode';

const jwt = tokenService.getLocalAccessToken();

const GamesPublics = () => {
  const [publicGames, setPublicGames] = useState([]);
  const [message, setMessage] = useState(null);
  const [game, setGame] = useState("");

  useEffect(() => {
    fetch(
      "/api/v1/game/publics", 
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      }
    )
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error(`Error fetching public games: ${response.status}`);
        }
      })
      .then((data) => {
        setPublicGames(data);
      })
      .catch((error) => setMessage(error.message));
  }, []);

  const handleJoinClick = (gameId) => {
    fetch(
      "/api/v1/game/joinPublic/" + gameId,
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
      } else {
        throw new Error("Failed to join the game");
      }
    })
    .then((response) => {
      console.log(response)
      if (response.code) {
        window.location.href = `/game/${response.code}`;
      } else {
        throw new Error("Failed to join the game");
      }
    })
    .catch((error) => {
      setMessage(error.message);
    });
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
      <h1 style={{ marginBottom: '20px' }}>Lista de Juegos Públicos</h1>
      {message && <p>{message}</p>}
      <ul style={{ listStyle: 'none', padding: '0' }}>
        {publicGames
          .map((publicGame) => (
            <li key={publicGame.id} style={{ border: '2px solid #000', borderRadius: '8px', marginBottom: '10px', padding: '10px' }}>
              {publicGame.name} - Jugadores: {publicGame.players.length}
              <button onClick={() => handleJoinClick(publicGame.id)}>Join</button>
            </li>
          ))}
      </ul>
    </div>
  );
};

export default GamesPublics;
