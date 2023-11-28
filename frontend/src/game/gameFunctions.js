
function getAlreadySelectedCardByPlayers(dwarves, setSelectedCards) {

    /*
    const updated= dwarves.map(d => {
      let dwacards = d.cards;
      let pacolor = d.player.color;

      let updated = {1: null,2: null,3: null,
        4: null,5: null,6: null,
        7: null,8: null,9: null};

      for ( const c of dwacards) {
        console.log(c.id + " to color => " + pacolor);
        updated[c.position] = pacolor;
      }
      //setSelectedCards(updated)
      return updated;
    })*/
    let updated = {1: null,2: null,3: null,
        4: null,5: null,6: null,
        7: null,8: null,9: null};
    for (const d of dwarves) {
        let dwacards = d.cards;
        let pacolor = d.player.color;
        for ( const c of dwacards) {
            console.log(c.id + " to color => " + pacolor);
            updated[c.position] = pacolor;
          }
    }
    console.log(updated)
    setSelectedCards(updated)
}

// Obtiene los dwarves de esta ronda
// y selecciona las cartas que han sido 
// seleccionadas por otros jugadores
function fetchDwarves(game, code, jwt, setDwarves, setSelectedCards){
    if (game.round !== undefined) {

      fetch(`/api/v1/game/play/${code}/dwarves`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
          Accept: 'application/json',
        }
      }).then(response => response.json()).then(response => {
        setDwarves(response.dwarves)

        getAlreadySelectedCardByPlayers(response, setSelectedCards);
      })
    } else {
      console.log("not entering to fetch dwarves")
      console.log(game.round)
    }
}

function changeRound(setSelectedCards, setChoosedCards, setGame,newGame) {
    setSelectedCards({1: null,2: null,3: null,
        4: null,5: null,6: null,
        7: null,8: null,9: null})
    setChoosedCards([])
    setGame(newGame)
    console.log("changing round")
}

// Actualiza los datos del juego
// Si al actualizar el juego se detecta que 
// se ha cambiado la ronda, se llama a una funcion que cambia de ronda
function fetchGame(game, code, jwt, setSelectedCards, setChoosedCards, setGame){

    fetch(`/api/v1/game/play/${code}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
       Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => {
      if(response.round !== game.round) {
        changeRound(setSelectedCards, setChoosedCards, setGame,response)
      }
    })
} 

function fetchCards(code, jwt, cards, setCards){// setSelectedCards, setChoosedCards) {
    fetch(`/api/v1/game/play/${code}/getCards`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => {
        if (cards !== response) {
            setCards(response)
        }    
    })
    /*
    setSelectedCards({1: null,2: null,3: null,
        4: null,5: null,6: null,
        7: null,8: null,9: null})
    setChoosedCards([])*/
  }

function fetchPlayers(code, jwt, players, setPlayers) {
    fetch(`/api/v1/game/play/${code}/players`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => {
        if (players !== response){
            setPlayers(response)
        }
    })
    //player = players.filter(p => p.name === user.username)[0]
}

function fetchIsMyTurn(game, code, jwt, isMyTurn, setIsMyTurn, setSelectedCards, 
    setChoosedCards, setGame, setDwarves) {
    fetch(`/api/v1/game/play/${code}/isMyTurn`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => {
        if (isMyTurn !== response){
            setIsMyTurn(response)
        }
    })
    fetchGame(game, code, jwt, setSelectedCards, setChoosedCards, setGame)
    fetchDwarves(game, code, jwt, setDwarves, setSelectedCards)
    //setAlreadySelectedCardByPlayers()
    //console.log(game)
    //console.log(isMyTurn)
    //console.log(dwarves)
  }

  function finDelJuego(code,jwt) {
    fetch(`/api/v1/game/play/${code}/finish`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    })
    .then(() => {
      window.location.href = `/game/${code}/finish`;
    })
  }

  function isFinished(code, jwt) {
    fetch(`/api/v1/game/play/${code}/isFinished`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => {
        if (response === true) {
            finDelJuego(code,jwt)
        }
    })
  }
  
function sendCard(code, jwt, choosedCard) {
  fetch(
    "/api/v1/game/play/" + code + "/dwarves",
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        card: choosedCard
      })
    }
  ).then((response) => response.text())
    .then((data) => console.log(data))
    .catch((message) => alert(message));
}


export { 
    fetchDwarves, fetchCards, fetchPlayers, 
    fetchIsMyTurn, isFinished, sendCard
};