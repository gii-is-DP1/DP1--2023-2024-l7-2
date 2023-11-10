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
  const [cards, setCards] = useFetchState(
    [],
    `/api/v1/game/play/${code}/cards`,
    jwt,
    setMessage,
    setVisible,
    code
);
  const [game, setGame] = useFetchState(
      {},
      `/api/v1/game/play/${code}`,
      jwt,
      setMessage,
      setVisible,
      code
  );


  function faseExtraccionMinerales() {

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

    faseExtraccionMinerales()
    faseSeleccionAcciones()
    faseResolucionAcciones()
  }

  
  console.log(cards);
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
        <section className="generalLayout" style={{display:"flex", flexDirection:"column"}}>
          <section className="cardDeckLayout">
            <section className="cardDeckLayoutRow1" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[0].id} />
              <Card id={cards[1].id} />
              <Card id={cards[2].id} />
            </section>
            <section className="cardDeckLayoutRow2" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[3].id} />
              <Card id={cards[4].id} />
              <Card id={cards[5].id} />
            </section>
            <section className="cardDeckLayoutRow3" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[6].id} />
              <Card id={cards[7].id} />
              <Card id={cards[8].id} />
            </section>
          </section>
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