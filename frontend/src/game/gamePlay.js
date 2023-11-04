import { useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import getErrorModal from "../util/getErrorModal";
import { Button, ButtonGroup, Table } from "reactstrap";
import { Link } from "react-router-dom";
import getIdFromUrl from "./../util/getIdFromUrl";

const jwt = tokenService.getLocalAccessToken();

export default function GamePlay() {
    const id = getIdFromUrl(2);
    const emptyGame = {
      id: id==="new"?null:id,
      name: "",
      code: id,
      start: null,
      finish: null,
      winner_id: null,
      round: 0
  };
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [game, setGame] = useFetchState(
      emptyGame,
      `/api/v1/game/check/${id}`,
      jwt,
      setMessage,
      setVisible,
      id
  );

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">Here we are playing</h1>        
        {modal}
        <div>
          <h3 className="text-center">{game.name}</h3>
          <h3 className="text-center">{game.code?"private":"public"}</h3>
          <h3 className="text-center">{game.start?'waiting':game.start}</h3>
          <h3 className="text-center">{game.finish?game.finish:''}</h3>
          <h3 className="text-center">{game.finish?game.winer_id:''}</h3>
          <h3 className="text-center">{game.finish?'':game.round}</h3>
        </div>
      </div>
    </div>
  );


}