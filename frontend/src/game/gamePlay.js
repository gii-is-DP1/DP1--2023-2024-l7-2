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

  const emptyGame = {};

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