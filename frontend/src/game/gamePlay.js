import { useState } from "react";
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

    }
      
    )
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

  function gameLogic() {




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

  gameLogic()

  const modal = getErrorModal(setVisible, visible, message);
 

  /*
  let cardList =
  cards.map((card) => {
    return (
      <Card id={card.id}/>
    )
  })*/

  function selectCard(card) {

    
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

  function sendCards() {
    fetch(
      "/api/v1/game/play/" + code + "/dwarves/" + user.id,
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

  console.log(game)
  if (game != {}) {
    game.playerCreator = user;
  }
  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">Dwarf - </h1>
        <section className="buttonsLayout" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
        <Button
          onClick={() => {faseExtraccionMinerales()}}
          title="Get Cards"
          color="#008000"
          style={{border: '3px solid black',padding: "3px"}}
        >Get Cards</Button>
        
        {game != {} && game.playerCreator && game.playerCreator.username === user.username && (
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

        </section>
        <section className="generalLayout" style={{display:"flex", flexDirection:"column"}}>
          {cards.length != 0 && 
          <section className="cardDeckLayout">
            <section className="cardDeckLayoutRow1" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[0].id} 
                    onClick={() => selectCard(cards[0])} color={choosedCards.includes(cards[0]) ? "green" : "white"}/>
              <Card id={cards[1].id}
                    onClick={() => selectCard(cards[1])} color={choosedCards.includes(cards[1]) ? "green" : "white"}/>
              <Card id={cards[2].id} 
                    onClick={() => selectCard(cards[2])} color={choosedCards.includes(cards[2]) ? "green" : "white"}/>
            </section>
            <section className="cardDeckLayoutRow2" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[3].id}
                    onClick={() => selectCard(cards[3])} color={choosedCards.includes(cards[3]) ? "green" : "white"}/>
              <Card id={cards[4].id} 
                    onClick={() => selectCard(cards[4])} color={choosedCards.includes(cards[4]) ? "green" : "white"}/>
              <Card id={cards[5].id} 
                    onClick={() => selectCard(cards[5])} color={choosedCards.includes(cards[5]) ? "green" : "white"}/>
            </section>
            <section className="cardDeckLayoutRow3" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[6].id} 
                    onClick={() => selectCard(cards[6])} color={choosedCards.includes(cards[6]) ? "green" : "white"}/>
              <Card id={cards[7].id} 
                    onClick={() => selectCard(cards[7])} color={choosedCards.includes(cards[7]) ? "green" : "white"}/>
              <Card id={cards[8].id} 
                    onClick={() => selectCard(cards[8])} color={choosedCards.includes(cards[8]) ? "green" : "white"}/>
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
