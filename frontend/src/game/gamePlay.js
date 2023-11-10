import { useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import getErrorModal from "../util/getErrorModal";
import { Button, ButtonGroup, Table } from "reactstrap";
import { Link } from "react-router-dom";
import getIdFromUrl from "./../util/getIdFromUrl";
import Card from "./../cards/card"

const jwt = tokenService.getLocalAccessToken();

export default function GamePlay() {
  const code = getIdFromUrl(2);


  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [cards, setCards] = useState([ 
    {cardType: "StarterCard",description: "Take 3 iron from the supply",id: null,name: "Iron Seam",position: 1,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: "StarterCard",description: "Take 3 iron from the supply",id: null,name: "Iron Seam",position: 2,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: "StarterCard",description: "Take 3 iron from the supply",id: null,name: "Iron Seam",position: 3,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: "StarterCard",description: "Take 3 iron from the supply",id: null,name: "Iron Seam",position: 4,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: "StarterCard",description: "Take 3 iron from the supply",id: null,name: "Iron Seam",position: 5,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: "StarterCard",description: "Take 3 iron from the supply",id: null,name: "Iron Seam",position: 6,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: "StarterCard",description: "Take 3 iron from the supply",id: null,name: "Iron Seam",position: 7,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: "StarterCard",description: "Take 3 iron from the supply",id: null,name: "Iron Seam",position: 8,
    totalGold: 0,totalIron: 3,totalMedals: 0,totalSteal: 0},
    {cardType: "StarterCard",description: "Take 3 iron from the supply",id: null,name: "Iron Seam",position: 9,
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


  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">Dward - </h1>
        <Button
          onClick={() => {faseExtraccionMinerales()}}
          title="Get Cards"
          color="#008000"
          style={{border: '3px solid black',padding: "3px"}}
        >Get Cards</Button>        
        <section className="generalLayout" style={{display:"flex", flexDirection:"column"}}>
          {cards.length != 0 && 
          <section className="cardDeckLayout">
            <section className="cardDeckLayoutRow1" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[0].id} name={cards[0].name} 
                    description={cards[0].description} totalIron={cards[0].totalIron}  
                    totalGold={cards[0].totalGold} totalSteal={cards[0].totalIron} />
              <Card id={cards[1].id} name={cards[1].name} 
                    description={cards[1].description} totalIron={cards[1].totalIron}  
                    totalGold={cards[1].totalGold} totalSteal={cards[1].totalIron} />
              <Card id={cards[2].id} name={cards[2].name} 
                    description={cards[2].description} totalIron={cards[2].totalIron}  
                    totalGold={cards[2].totalGold} totalSteal={cards[2].totalIron} />
            </section>
            <section className="cardDeckLayoutRow2" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[3].id} name={cards[3].name} 
                    description={cards[3].description} totalIron={cards[3].totalIron}  
                    totalGold={cards[3].totalGold} totalSteal={cards[3].totalIron} />
              <Card id={cards[4].id} name={cards[4].name} 
                    description={cards[4].description} totalIron={cards[4].totalIron}  
                    totalGold={cards[4].totalGold} totalSteal={cards[4].totalIron} />
              <Card id={cards[5].id} name={cards[5].name} 
                    description={cards[5].description} totalIron={cards[5].totalIron}  
                    totalGold={cards[5].totalGold} totalSteal={cards[5].totalIron} />
            </section>
            <section className="cardDeckLayoutRow3" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[6].id} name={cards[6].name} 
                    description={cards[6].description} totalIron={cards[6].totalIron}  
                    totalGold={cards[6].totalGold} totalSteal={cards[6].totalIron} />
              <Card id={cards[7].id} name={cards[7].name} 
                    description={cards[7].description} totalIron={cards[7].totalIron}  
                    totalGold={cards[7].totalGold} totalSteal={cards[7].totalIron} />
              <Card id={cards[8].id} name={cards[8].name} 
                    description={cards[8].description} totalIron={cards[8].totalIron}  
                    totalGold={cards[8].totalGold} totalSteal={cards[8].totalIron} />
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