import { useEffect, useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import getErrorModal from "../util/getErrorModal";
import { Button, ButtonGroup, Table } from "reactstrap";
import { Link } from "react-router-dom";
import getIdFromUrl from "./../util/getIdFromUrl";
import Card from "./../cards/card"

const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser()

export default function GamePlay() {
  const code = getIdFromUrl(2);
  console.log(jwt)


  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [choosedCards, setChoosedCards] = useState([]);
  const [cards, setCards] = useState([ 
    {cardType: {id: 4, name: "Other"},description: "Take 3 iron from the supply",id: 1,name: "Iron Seam",position: 1,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: {id: 4, name: "Other"},description: "Take 3 iron from the supply",id: 2,name: "Iron Seam",position: 2,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: {id: 4, name: "Other"},description: "Take 3 iron from the supply",id: 3,name: "Iron Seam",position: 3,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: {id: 4, name: "Other"},description: "Take 3 iron from the supply",id: 4,name: "Iron Seam",position: 4,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: {id: 4, name: "Other"},description: "Take 3 iron from the supply",id: 5,name: "Iron Seam",position: 5,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: {id: 4, name: "Other"},description: "Take 3 iron from the supply",id: 6,name: "Iron Seam",position: 6,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: {id: 4, name: "Other"},description: "Take 3 iron from the supply",id: 7,name: "Iron Seam",position: 7,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: {id: 4, name: "Other"},description: "Take 3 iron from the supply",id: 8,name: "Iron Seam",position: 8,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: {id: 4, name: "Other"},description: "Take 3 iron from the supply",id: 9,name: "Iron Seam",position: 9,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
  ])
  const [selectedCards,setSelectedCards] = useState(
    {1: null,2: null,3: null,
      4: null,5: null,6: null,
      7: null,8: null,9: null}
  )
  
  /*useFetchState(
    [],
    `/api/v1/game/play/${code}/cards`,
    jwt,
    setMessage,
    setVisible,
    code
);*/
  const [game, setGame] = useFetchState(
      {},
      `/api/v1/game/play/${code}`,
      jwt,
      setMessage,
      setVisible,
      code
  );

  const [players, setPlayers] = useFetchState(
    [],
    `/api/v1/game/play/${code}/players`,
    jwt,
    setMessage,
    setVisible,
    code
  );
  const [isMyTurn, setIsMyTurn] = useFetchState(
    false,
    `/api/v1/game/play/${code}/isMyTurn`,
    jwt,
    setMessage,
    setVisible,
    code
  );

  const [dwarves, setDwarves] = useFetchState([]);
  let player = players.filter(p => p.name === user.username)[0]
  useEffect(() => {
    gameLogic()
  },[])


  function setAlreadySelectedCardByPlayers() {
    dwarves.map(d => {
      let dwacards = d.cards;
      let pacolor = d.player.color;

      let updated = selectedCards
      for ( const c of dwacards) {
        console.log(c.id + " to color => " + pacolor);
        updated[c.id] = pacolor;
      }
      setSelectedCards(updated)
    })
  }

  function fetchDwarves(){
    if (game.round !== undefined) {

      fetch(`/api/v1/game/play/${code}/dwarves/${game.round}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
          Accept: 'application/json',
        }
      }).then(response => response.json()).then(response => setDwarves(response))
    } else {
      console.log("not entering to fetch dwarves")
      console.log(game.round)
    }
    console.log(dwarves)
  }


  function fetchPlayers() {
    fetch(`/api/v1/game/play/${code}/players`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => setPlayers(response))
    player = players.filter(p => p.name === user.username)[0]
  }

  function startGame() {
    faseExtraccionMinerales()
  }

  function fetchIsMyTurn() {
    fetch(`/api/v1/game/play/${code}/isMyTurn`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => setIsMyTurn(response))
    fetchDwarves()
    setAlreadySelectedCardByPlayers()
    console.log(isMyTurn)
    console.log(dwarves)
  }
  function faseExtraccionMinerales() {

    fetch(`/api/v1/game/play/${code}/getCards`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json())
    .then((newCards) => {
      let oldCardDisplay = cards

      if ( newCards.length > 0) {
        // Comprobamos que el array tenga valores por si acaso

        oldCardDisplay = oldCardDisplay.map(card => card.position === newCards[0].position ? newCards[0] : card)
      }

      if (newCards.length > 1) {
        // El array no tiene por que tener dos cartas porque si se repite la posicion al robar las cartas,
        // la segunda vez que se repite la posicion no se roba de nuevo. En resumen revisar reglas
        oldCardDisplay = oldCardDisplay.map(card => card.position === newCards[1].position ? newCards[1] : card)
      }
          
      setCards(oldCardDisplay)

    })

  }

  function faseSeleccionAcciones() {

  }

  function faseResolucionAcciones() {

    /* 
      El orden es el siguiente:
      “Recibir ayuda” -> “Defender” -> “Extraer mineral” -> “Forjar”. 
      Se haria un for en las cartas y se iria viendo que tipo de cartas hay
      y aplicando las acciones necesarias
    */
  }

  function waitTillIsMyTurn() {
    if (isMyTurn === false) {
      fetchDwarves();
      fetchIsMyTurn();
      //window.setTimeout(waitTillIsMyTurn, 1000);
    } 
  }

  function gameLogic() {

    if (game != {} && game.playerCreator && game.playerCreator.name !== user.username) {
      fetchDwarves()
    }

    if(isMyTurn === false) {
      waitTillIsMyTurn()
    }



    /*
      Aqui se pondra la logica del juego.
    */

    /* 
      Antes de comprobar la ronda se deberan de hacer comprobaciones
      sobre el numero de jugadores y si el jugador principal empieza la partida o no.
      Lo de que si el jugador principal empieza la partida se puede hacer con un boton que cambie 
      el estado de una variable aqui en el frontend a true y lo del numero de jugadores se puede
      hacer con un while que haga un fetch a game cada 1 segundo y comprueba el numero de jugadores
      y no sale del bucle hasta que el numero de jugadores sea 3 y este aceptado el que empiece la partida
      SOLO DOY IDEAS :)
    */

    //faseExtraccionMinerales()
    faseSeleccionAcciones()
    faseResolucionAcciones()
  }

  

  const modal = getErrorModal(setVisible, visible, message);

  function selectCard(card) {
    if (isMyTurn === false) {
      console.log("is not your turn")
      return false; // Just a random return to ensure that function exits
    }

    let id = card.id
    if (selectedCards[id] !== null) {
      // Card is already selected, you can't select it
      console.log(id);
      console.log(selectedCards[id])
      return false;
    }
    
    if (choosedCards.includes(card)) {
      console.log("we are filtering")
      setChoosedCards(choosedCards.filter((c) => c.position !== card.position))
    } else {
      if (choosedCards.length <2) {
        setChoosedCards([...choosedCards,card])
      } else {
        console.log("You cant choose that many cads :(")
      }
    }
    console.log(choosedCards)
  }

  function getCardColor(card) {
    // If card was already selected by another player, card's color is other player's color
    console.log(selectedCards)
    let id = card.id
    let color = selectedCards[id] !== null ? selectedCards[id] : "white"

    // Else it is checked if the card has been selected
    if ( color === "white") {
      color = choosedCards.includes(card) ? player.color : "white"
    }

    return color
  }

  function sendCards() {
    fetch(
      "/api/v1/game/play/" + code + "/dwarves",
      {
          method:  "POST",
          headers: {
              Authorization: `Bearer ${jwt}`,
              Accept: "application/json",
              "Content-Type": "application/json",
          },
          body: JSON.stringify(choosedCards),
      }
    ).then((response) => response.text())
    .then((data) => {
        console.log(data);
    }).catch((message) => alert(message));
  }

  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">Dwarf - </h1>
        <section className="buttonsLayout" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
          { players && players.length > 1 &&

            <Button
              onClick={() => {startGame()}}
              title="Get Cards"
              color="#008000"
              style={{border: '3px solid black',padding: "3px"}}>
                Get Cards
              </Button>
          }
        
        {game != {} && game.playerCreator && game.playerCreator.name === user.username && (
            <Button
              onClick={() => {
                gameLogic();
              }}
              title="Start Game"
              color="#008000"
              style={{ border: "3px solid black", padding: "3px" }}
            >
              Start Game
            </Button>
          )}
            <Button
              onClick={() => {
                fetchIsMyTurn();
              }}
              title="Start Game"
              color="#008000"
              style={{ border: "3px solid black", padding: "3px" }}
            >
              checkTurn
            </Button>
          { isMyTurn && <h2>Is your turn!</h2>}
          {choosedCards.length === 2 && (
            <Button
              onClick={() => {
                sendCards();
              }}
              title="Send Cards"
              color="#008000"
              style={{ border: "3px solid black", padding: "3px" }}
            >
              Send Cards
            </Button>
          )}

            <Button
              onClick={() => {
                fetchPlayers();
              }}
              title="Fetch Players"
              color="#008000"
              style={{ border: "3px solid black", padding: "3px" }}
            >
              Fetch players
            </Button>

        </section>
        <section className="generalLayout" style={{display:"flex", flexDirection:"column"}}>
          {cards.length != 0 && player && player.color &&
          <section className="cardDeckLayout">
            <section className="cardDeckLayoutRow1" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[0].id} 
                    onClick={() => selectCard(cards[0])} color={getCardColor(cards[0])}/>
              <Card id={cards[1].id}
                    onClick={() => selectCard(cards[1])} color={getCardColor(cards[1])}/>
              <Card id={cards[2].id} 
                    onClick={() => selectCard(cards[2])} color={getCardColor(cards[2])}/>
            </section>
            <section className="cardDeckLayoutRow2" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[3].id}
                    onClick={() => selectCard(cards[3])} color={getCardColor(cards[3])}/>
              <Card id={cards[4].id} 
                    onClick={() => selectCard(cards[4])} color={getCardColor(cards[4])}/>
              <Card id={cards[5].id} 
                    onClick={() => selectCard(cards[5])} color={getCardColor(cards[5])}/>
            </section>
            <section className="cardDeckLayoutRow3" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[6].id} 
                    onClick={() => selectCard(cards[6])} color={getCardColor(cards[6])}/>
              <Card id={cards[7].id} 
                    onClick={() => selectCard(cards[7])} color={getCardColor(cards[7])}/>
              <Card id={cards[8].id} 
                    onClick={() => selectCard(cards[8])} color={getCardColor(cards[8])}/>
            </section>
          </section>
          }
          <section className="specialCardDeckLayout">
            <div><h2 className="text-center">Special Card</h2></div>
            <div><h2 className="text-center">Special Card</h2></div>
            <div><h2 className="text-center">Special Card</h2></div>
          </section>
        </section>
      </div>
    </div>
  );


}
