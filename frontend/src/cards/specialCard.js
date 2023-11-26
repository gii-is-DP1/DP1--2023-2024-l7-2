import React, { useState, useEffect } from "react";
import tokenService from "../services/token.service";

import getIdFromUrl from "./../util/getIdFromUrl";
import useFetchState from "./../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();


const SpecialCard = (props) => {
  const id = props.id
  const emptyCard = {};
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [specialCard, setCard] = useFetchState(
      emptyCard,
      `/api/v1/specialcard/${id}`,
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
      <h3 style={{ textAlign: 'center', marginTop: '30px' }}> {specialCard.name} </h3>
      <p style={{ textAlign: 'center', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>{specialCard.description}</p>
    </div>
   );
  }

export default SpecialCard;




