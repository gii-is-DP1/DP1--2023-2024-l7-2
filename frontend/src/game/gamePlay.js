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
  //console.log(jwt)


  const [gameStarted, setGameStarted] = useState(null);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [choosedCards, setChoosedCards] = useState([]);
  const [cards, setCards] = useFetchState(
    [],
    `/api/v1/game/play/${code}/getCards`,
    jwt,
    setMessage,
    setVisible,
    code
);
    //console.log(cards)
  /*useState([ 
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
  ])*/
  const emptySelectedCards = {1: null,2: null,3: null,
    4: null,5: null,6: null,
    7: null,8: null,9: null}
  const [selectedCards,setSelectedCards] = useState(emptySelectedCards)
  
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
    setSelectedCards(emptySelectedCards);
    setChoosedCards([]);

    fetchIsMyTurn()
    fetchDwarves()
    setAlreadySelectedCardByPlayers()
  },[])


  function setAlreadySelectedCardByPlayers() {
    dwarves.map(d => {
      let dwacards = d.cards;
      let pacolor = d.player.color;

      let updated = emptySelectedCards
      for ( const c of dwacards) {
        console.log(c.id + " to color => " + pacolor);
        updated[c.position] = pacolor;
      }
      setSelectedCards(updated)
    })
  }

  function fetchDwarves(){
    if (game.round !== undefined) {

      fetch(`/api/v1/game/play/${code}/dwarves`, {
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
    //console.log(dwarves)
  }

  function fetchGame(){

    fetch(`/api/v1/game/play/${code}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
       Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => {
      if(response.round !== game.round) {
        setSelectedCards(emptySelectedCards)
        setChoosedCards([])
        setGame(response)
        console.log("changing round")
      }
    })
  } 
  

  function fetchCards() {
    fetch(`/api/v1/game/play/${code}/getCards`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => setCards(response))
    setSelectedCards(emptySelectedCards)
    setChoosedCards([])
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

  /*
  function startGame() {
    faseExtraccionMinerales()
  }*/

  function fetchIsMyTurn() {
    fetch(`/api/v1/game/play/${code}/isMyTurn`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => setIsMyTurn(response))
    fetchGame()
    fetchDwarves()
    setAlreadySelectedCardByPlayers()
    console.log(game)
    console.log(isMyTurn)
    console.log(dwarves)
  }
/*
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
      } else {
        // Quizas el juego ha terminado
        isFinished()
        return;
      }

      if (newCards.length > 1) {
        // El array no tiene por que tener dos cartas porque si se repite la posicion al robar las cartas,
        // la segunda vez que se repite la posicion no se roba de nuevo. En resumen revisar reglas
        oldCardDisplay = oldCardDisplay.map(card => card.position === newCards[1].position ? newCards[1] : card)
      }
          
      setCards(oldCardDisplay)

    })

  }

  /*
  function faseSeleccionAcciones() {

  }

  function faseResolucionAcciones() {

    /* 
      El orden es el siguiente:
      “Recibir ayuda” -> “Defender” -> “Extraer mineral” -> “Forjar”. 
      Se haria un for en las cartas y se iria viendo que tipo de cartas hay
      y aplicando las acciones necesarias
    */
  

  function isFinished() {
    fetch(`/api/v1/game/play/${code}/isFinished`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => {if (response === true) {finDelJuego()}})
  }

  function finDelJuego() {
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

/*
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


    //faseExtraccionMinerales()
    //faseSeleccionAcciones()
    //faseResolucionAcciones()
    // if (isFinished)
    //   finDelJuego();
  }*/

  

  const modal = getErrorModal(setVisible, visible, message);

  function selectCard(id,card) {
    if (isMyTurn === false) {
      console.log("is not your turn")
      return false; // Just a random return to ensure that function exits
    }

    if (selectedCards[id] !== null && selectedCards[id] !== undefined) {
      // Card is already selected, you can't select it
      console.log(selectedCards)
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
  }

  function getCardColor(id,card) {
    // If card was already selected by another player, card's color is other player's color
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
        fetchIsMyTurn()
        console.log(data);
    }).catch((message) => alert(message));
  }

  //console.log(players)
  const playerList = players.map((play) => {
    return (
      <tr key={play.id} style={{color:play.color}}>
        <td style={{color:play.color}} className="text-center">{play.name}</td>
        <td style={{color:play.color}} className="text-center">Iron: {play.iron}</td>
        <td style={{color:play.color}} className="text-center">Gold: {play.gold}</td>
        <td style={{color:play.color}} className="text-center">Steal: {play.steal}</td>
        <td style={{color:play.color}} className="text-center">Medals: {play.medal}</td>
      </tr>
    )
  })

  console.log(choosedCards)
  return (
    <div style={{marginTop: "70px"}}>

      <div className="admin-page-container">
      <h1 className="text-center">{game.name} - Round {game.round}</h1>
          { game != {} && game.playerCreator && game.playerCreator.name === user.username 
          && players && players.length > 1 && !gameStarted &&
          <Button
            onClick={() => {setGameStarted(true)}}
            title="Start game"
            color="#008000"
            style={{border: '3px solid black',padding: "3px"}}>
              Start game
            </Button>
          }

          { (gameStarted || game != {} && game.playerCreator && game.playerCreator.name !== user.username) &&
        <section className="buttonsLayout" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>

            <Button
                onClick={() => {isFinished()}}
                title="Finish?"
                color="#008000"
                style={{border: '3px solid black',padding: "3px"}}>
                  Finish?
            </Button>
          
            <Button
                onClick={() => {fetchCards()}}
                title="Finish?"
                color="#008000"
                style={{border: '3px solid black',padding: "3px"}}>
                  Get Cards
            </Button>

            <Button
              onClick={() => {fetchIsMyTurn();}}
              title="Start Game"
              color="#008000"
              style={{ border: "3px solid black", padding: "3px" }}>
              checkTurn
            </Button>
            {choosedCards.length === 2 && (
              <Button
              onClick={() => {
                sendCards();
              }}
              title="Send Cards"
              color="#008000"
              style={{ border: "3px solid black", padding: "3px" }}>
                Send Cards
              </Button>
            )}

          { isMyTurn && <h2>Is your turn!</h2>}
        </section>
          }
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
        <section className="generalLayout" style={{display:"flex", flexDirection:"column"}}>
          <section>
            <Table>
              <tbody>
              {playerList}
              </tbody>
            </Table>
          </section>
          {cards.length != 0 && player && player.color &&
          <section className="cardDeckLayout">
            <section className="cardDeckLayoutRow1" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[0].id} 
                    onClick={() => selectCard(1,cards[0])} color={getCardColor(1,cards[0])}/>
              <Card id={cards[1].id}
                    onClick={() => selectCard(2,cards[1])} color={getCardColor(2,cards[1])}/>
              <Card id={cards[2].id} 
                    onClick={() => selectCard(3,cards[2])} color={getCardColor(3,cards[2])}/>
            </section>
            <section className="cardDeckLayoutRow2" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[3].id}
                    onClick={() => selectCard(4,cards[3])} color={getCardColor(4,cards[3])}/>
              <Card id={cards[4].id} 
                    onClick={() => selectCard(5,cards[4])} color={getCardColor(5,cards[4])}/>
              <Card id={cards[5].id} 
                    onClick={() => selectCard(6,cards[5])} color={getCardColor(6,cards[5])}/>
            </section>
            <section className="cardDeckLayoutRow3" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[6].id} 
                    onClick={() => selectCard(7,cards[6])} color={getCardColor(7,cards[6])}/>
              <Card id={cards[7].id} 
                    onClick={() => selectCard(8,cards[7])} color={getCardColor(8,cards[7])}/>
              <Card id={cards[8].id} 
                    onClick={() => selectCard(9,cards[8])} color={getCardColor(9,cards[8])}/>
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
