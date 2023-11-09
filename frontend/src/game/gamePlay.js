import { useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import getErrorModal from "../util/getErrorModal";
import { Button, ButtonGroup, Table } from "reactstrap";
import { Link } from "react-router-dom";
import getIdFromUrl from "./../util/getIdFromUrl";

const jwt = tokenService.getLocalAccessToken();

export default function GamePlay() {
  const code = getIdFromUrl(2);

  const emptyGame = {
  };

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [game, setGame] = useFetchState(
      emptyGame,
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

  gameLogic()

  const modal = getErrorModal(setVisible, visible, message);
 

  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">Dward - </h1>        
        <section className="generalLayout">
          <section className="cardDeckLayout">
            <div><h2 className="text-center">Card</h2></div>
            <div><h2 className="text-center">Card</h2></div>
            <div><h2 className="text-center">Card</h2></div>
            <div><h2 className="text-center">Card</h2></div>
            <div><h2 className="text-center">Card</h2></div>
            <div><h2 className="text-center">Card</h2></div>
            <div><h2 className="text-center">Card</h2></div>
            <div><h2 className="text-center">Card</h2></div>
            <div><h2 className="text-center">Card</h2></div>
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