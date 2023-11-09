import React, { useState, useEffect, Button } from "react";
import tokenService from "../services/token.service";

import getIdFromUrl from "./../util/getIdFromUrl";
import useFetchState from "./../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();


const Card = (props) => {
  const id = props.id;
  const emptyCard = {};
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [select, setSelect] = useState(false);
  const [card, setCard] = useFetchState(
      emptyCard,
      `/api/v1/card/${id}`,
      jwt,
      setMessage,
      setVisible,
      id);

    const rectanguloStyle = {
      width: '300px',
      height: '400px',
      backgroundColor: 'white',
      border: '3px solid black',
    };

    const rectanguloStyle2 = {
      width: '300px',
      height: '400px',
      backgroundColor: 'grey',
      border: '3px solid black',
    };

    const renderCardNormal = () => {
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', flexDirection: 'column' }}>
        <div style={rectanguloStyle}>
          <h1 style={{ textAlign: 'center' }}> {card.name} </h1>
          <p style={{ textAlign: 'center', marginTop: '150px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>{card.description}</p>
          <div style={{ display: 'flex', marginTop: 'auto', justifyContent: 'space-between', padding: '0 25px' }}>
            <p>Hierro: {card.totalIron}</p>
            <p>Oro: {card.totalGold}</p>
            <p>Acero: {card.totalSteal}</p> 
          </div>
          <Button
            onPress={setSelect}
            title="SelectCard"
            color="#008000"
          />
        </div>
      </div>
    }

    const renderCardSelected = () => {
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', flexDirection: 'column' }}>
      <div style={rectanguloStyle2}>
        <h1 style={{ textAlign: 'center' }}> {card.name} </h1>
        <p style={{ textAlign: 'center', marginTop: '150px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>{card.description}</p>
        <div style={{ display: 'flex', marginTop: 'auto', justifyContent: 'space-between', padding: '0 25px' }}>
          <p>Hierro: {card.totalIron}</p>
          <p>Oro: {card.totalGold}</p>
          <p>Acero: {card.totalSteal}</p> 
        </div>
        <Button
          onPress={setSelect}
          title="SelectCard"
          color="#008000"
        />
      </div>
    </div>
  }
  if(select === true){
    return renderCardSelected
  }else{
    return renderCardNormal
   } 
  ;
  }

export default Card;




