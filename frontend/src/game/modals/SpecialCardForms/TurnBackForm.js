import React, { useState, useEffect } from "react";
import {Input, FormGroup, Form, Label } from 'reactstrap'
import tokenService from '../../../services/token.service';
import getIdFromUrl from '../../../util/getIdFromUrl';
import Card from '../../../cards/card';
import useFetchState from "../../../util/useFetchState";


function resolveTurnBack(code, jwt, payload) {
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
      alert("Turn back failed. Please try again.");
    }
  })
  .catch((error) => {
    console.error("Error during Turn Back:", error);
  });
}

const jwt = tokenService.getLocalAccessToken();

function FormTurnBack(props) {

    function handleChange(event, setParam) {
        const target = event.target;
        const value = target.value;
        setParam(value);
    }


    //console.log("Selected card =>",props.cardSelected )
    return (
    <Form>
        <FormGroup style={{display:'flex',flexDirection:'row'}}>
          <Input
            id="positionSelect"
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
    </Form>
    )

}

export {FormTurnBack, resolveTurnBack};