
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
        const c = d.card;
        const pacolor = d.player.color;
        console.log(c.id + " to color => " + pacolor);
        updated[c.position] = pacolor;
          
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
    setChoosedCards(null)
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

  function isStart(code, jwt) {
    fetch(`/api/v1/game/play/${code}/isStart`, {
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
      body: JSON.stringify(choosedCard)
    }
  ).then((response) => response.text())
    .then((data) => console.log(data))
    .catch((message) => alert(message));
}
/*
function specialOrder(code, jwt, setSelectedCards) {
  const gold = prompt("Enter the number of gold:");
  const steal = prompt("Enter the number of steal:");
  const iron = prompt("Enter the number of iron:");

  const order = {
    gold: parseInt(gold),
    steal: parseInt(steal),
    iron: parseInt(iron),
  };

  const wantsObject = window.confirm("Do you want to select an object in exchange for the materials?");
  
  if (wantsObject) {
    // Fetch the list of available objects from the server
    fetch(`/api/v1/game/play/${code}/availableObjects`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
      },
    })
      .then((response) => response.json())
      .then((objects) => {
        // Let the player choose an object
        const selectedObject = prompt("Select an object:\n" + objects.join("\n"));

        // Attach the selected object to the order
        order.selectedObject = selectedObject;

        // Continue with the API call
        sendOrderToServer(code, jwt, order, setSelectedCards);
      })
      .catch((error) => {
        console.error("Error fetching available objects:", error);
        alert("An error occurred while fetching available objects. Please try again.");
      });
  } else {
    // Continue with the API call without selecting an object
    sendOrderToServer(code, jwt, order, setSelectedCards);
  }
}
/*
function sendOrderToServer(code, jwt, order, setSelectedCards) {
  // You can replace the following fetch with your actual API call
  fetch(`/api/v1/game/play/${code}/specialOrder`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${jwt}`,
      Accept: "application/json",
    },
    body: JSON.stringify(order),
  })
    .then((response) => response.json())
    .then((response) => {
      if (response.success) {
        // Assuming the server responds with success
        // Update the selected cards or perform any other necessary actions
        getAlreadySelectedCardByPlayers(response.dwarves, setSelectedCards);
      } else {
        // Handle the case where the special order is not successful
        alert("Special order failed. Please try again.");
      }
    })
    .catch((error) => {
      console.error("Error during special order:", error);
      alert("An error occurred during the special order. Please try again.");
    });
}


function sellAnItem(code, jwt, setSelectedCards) {
  const gold = prompt("Enter the number of gold:");
  const steal = prompt("Enter the number of steal:");
  const iron = prompt("Enter the number of iron:");

  const order = {
    gold: parseInt(gold),
    steal: parseInt(steal),
    iron: parseInt(iron),
  };

  const wantsObject = window.confirm("Do you want to select an object in exchange for the materials?");
  
  if (wantsObject) {
    
    fetch(`/api/v1/game/play/${code}/availableObjects`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
      },
    })
      .then((response) => response.json())
      .then((objects) => {
        // Let the player choose an object
        const selectedObject = prompt("Select an object:\n" + objects.join("\n"));

        // Attach the selected object to the order
        order.selectedObject = selectedObject;

        // Continue with the API call
        sendSellAnItem(code, jwt, order, setSelectedCards);
      })
      .catch((error) => {
        console.error("Error fetching available objects:", error);
        alert("An error occurred while fetching available objects. Please try again.");
      });
  } else {
    // Continue with the API call without selecting an object
    sendSellAnItem(code, jwt, order, setSelectedCards);
  }
}

function sendSellAnItem(code, jwt, order, setSelectedCards) {
  // You can replace the following fetch with your actual API call
  fetch(`/api/v1/game/play/${code}/specialSellItem`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${jwt}`,
      Accept: "application/json",
    },
    body: JSON.stringify(order),
  })
    .then((response) => response.json())
    .then((response) => {
      if (response.success) {
        // Assuming the server responds with success
        // Update the selected cards or perform any other necessary actions
        getAlreadySelectedCardByPlayers(response.dwarves, setSelectedCards);
      } else {
        // Handle the case where the special sell item order is not successful
        alert("Special sell item order failed. Please try again.");
      }
    })
    .catch((error) => {
      console.error("Error during special sell item order:", error);
      alert("An error occurred during the special sell item order. Please try again.");
    });
}
*/


export { 
    fetchDwarves, fetchCards, fetchPlayers, 
    fetchIsMyTurn, isFinished, sendCard,isStart
};