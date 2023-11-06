import React, { useState, useEffect } from "react";
import tokenService from "../services/token.service";

const CardList = () => {
 const [cards, setCards] = useState([]);
 const jwt = tokenService.getLocalAccessToken();

 useEffect(() => {
 fetch("/api/v1/card", {
   headers: {
     Authorization: `Bearer ${jwt}`,
   },
 })
   .then((response) => {
     if (response.ok) {
       return response.json();
     } else {
       throw new Error("Error fetching cards");
     }
   })
   .then((data) => {
     setCards(data);
   })
   .catch((error) => {
     console.error("Error:", error);
   });
 }, []);

 const rectanguloStyle = {
  width: '25%',
  height: '400px',
  backgroundColor: 'white',
  border: '3px solid black',
 };

 return (
 <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between' }}>
   {cards.map((card) => (
     <div key={card.id} style={rectanguloStyle}>
       <h1 style={{ textAlign: 'center' }}> {card.name} </h1>
       <p style={{ textAlign: 'center', marginTop: '150px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>{card.description}</p>
       <div style={{ display: 'flex', marginTop: 'auto', justifyContent: 'space-between', padding: '0 25px' }}>
         <p>Hierro: {card.totalIron}</p>
         <p>Oro: {card.totalGold}</p>
         <p>Acero: {card.totalSteal}</p>
       </div>
     </div>
   ))}
 </div>
 );
};

export default CardList;
