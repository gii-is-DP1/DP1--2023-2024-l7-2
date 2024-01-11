import React, { useState, useEffect } from "react";
import {Input, FormGroup, Form, Label } from 'reactstrap'
import tokenService from '../../../services/token.service';
import getIdFromUrl from '../../../util/getIdFromUrl';
import Card from '../../../cards/card';
import useFetchState from "../../../util/useFetchState";


function resolvePastGlories(code, jwt, payload) {
  fetch(`/api/v1/game/play/${code}/specialAction`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${jwt}`,
      Accept: "application/json",
    },
    body: JSON.stringify(payload),
  })
  .then((response) => response.json())
  .then((response) => {
    if (!response.success) {

      // Handle the case where the special sell item order is not successful
      alert("Special sell item order failed. Please try again.");
    }
  })
  .catch((error) => {
    console.error("Error during special sell item order:", error);
  });
}

const jwt = tokenService.getLocalAccessToken();

function FormPastGlories(props) {
  const code = getIdFromUrl(2);

  const [cardSelection, setCardSelection] = useState(null);
  const [tmpCard, setTmpCard] = useState(null);
  const [cardVisible, setCardVisible] = useState(null);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [allCards, setAllCards] = useFetchState(
    [],
    `/api/v1/game/play/${code}/getAllCards`,
    jwt,
    setMessage,
    setVisible,
    code
  );

  useEffect(() => {
    if (allCards && allCards.length === 9) {
      setCardSelection(allCards[props.position-1].map((cardObj) => {
        return (
          <option key={cardObj.id}>
            {cardObj.name}
          </option>
        )
      }))
    }
  },[props.position])

  useEffect(() => {
    if (allCards && allCards.length === 9) {
      let possibleCards = allCards[props.position-1];
      for (const card of possibleCards) {
        if (card.name === tmpCard) {
          props.setCardSelected(card)
          break
        }
      }
      if (props.cardSelected) {
        setCardVisible(() => { return (<Card id={props.cardSelected.id}/>)})
      }
    }
  },[tmpCard])


    function handleChange(event, setParam) {
        const target = event.target;
        const value = target.value;
        setParam(value);
    }

    function handleChangeForCard(event, setParam) {
      const target = event.target;
      const value = target.value;
      setParam(value);
  }

    //console.log("Selected card =>",props.cardSelected )
    return (
    <Form>
        <FormGroup style={{display:'flex',flexDirection:'row'}}>
          <Input
            id="exampleSelect"
            name="select"
            type="select"
            required
            onChange={(event) => handleChange(event,props.setPosition)}
          >
            <option key={-1}>-</option>
            <option key={1}>1</option>
            <option key={2}>2</option>
            <option key={3}>3</option>
            <option key={4}>4</option>
            <option key={5}>5</option>
            <option key={6}>6</option>
            <option key={7}>7</option>
            <option key={8}>8</option>
            <option key={9}>9</option>
          </Input>
        </FormGroup>
        <FormGroup style={{display:'flex',flexDirection:'Column'}}>
            <Label for="iron"> Cards </Label>
            <Input
                type="select"
                required
                name="CardSelection"
                value={tmpCard}
                onChange={(event) => handleChangeForCard(event,setTmpCard)}
                className="custom-input"
            >
              <option key={-1}>-</option>
              {cardSelection}
            </Input>
            {props.cardSelected && props.cardSelected.id && 
              <Card id={props.cardSelected.id}/>}
        </FormGroup>
    </Form>
    )

}

export {FormPastGlories, resolvePastGlories};