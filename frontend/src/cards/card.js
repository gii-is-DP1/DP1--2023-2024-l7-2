import React, { useState, useEffect } from "react";
import tokenService from "../services/token.service";

import getIdFromUrl from "./../util/getIdFromUrl";
import useFetchState from "./../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();


const Card = (props) => {
  const id = props.id;
  /*
  const name = card.name;
  const description = props.description;
  const totalIron = props.totalIron;
  const totalGold = props.totalGold;
  const totalSteal = props.totalSteal;*/

  const emptyCard = {};
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [card, setCard] = useFetchState(
      emptyCard,
      `/api/v1/card/${id}`,
      jwt,
      setMessage,
      setVisible,
      id);

    const rectanguloStyle = {
      width: '200px',
      height: '250px',
      backgroundColor: 'white',
      border: '7px solid black',
      display: 'flex', 
      justifyContent: 'center', 
      alignItems: 'center', 
      flexDirection: 'column',
      backgroundColor: props.color
    };


  return (
    <div style={rectanguloStyle} onClick={props.onClick}>
      <h3 style={{ textAlign: 'center', marginTop: '30px' }}> {card.name} </h3>
      <p style={{ textAlign: 'center', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>{card.description}</p>
      <div style={{ display: 'flex', marginTop: 'auto', justifyContent: 'space-between', padding: '0 25px', flexDirection:"column" }}>
        <p>Hierro: {card.totalIron}</p>
        <p>Oro: {card.totalGold}</p>
        <p>Acero: {card.totalSteal}</p> 
      </div>
    </div>
   );
  }

export default Card;