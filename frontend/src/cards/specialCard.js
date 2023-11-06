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
      width: '300px',
      height: '400px',
      backgroundColor: 'white',
      border: '3px solid black',
    };


  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', flexDirection: 'column' }}>
      <div style={rectanguloStyle}>
        <h1 style={{ textAlign: 'center' }}> {specialCard.name} </h1>
        <p style={{ textAlign: 'center', marginTop: '150px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>{specialCard.description}</p>
      </div>
    </div>
   );
  }

export default SpecialCard;




