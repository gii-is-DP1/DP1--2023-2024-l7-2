import React, { useState, useEffect } from "react";
import {Input, FormGroup, Form, Label } from 'reactstrap'
import tokenService from '../../../services/token.service';
import getIdFromUrl from '../../../util/getIdFromUrl';
import Card from '../../../cards/card';
import useFetchState from "../../../util/useFetchState";


function resolveApprentice(code, jwt, payload) {
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

function FormApprentice(props) {

  function handleChange(event, setParam) {
    const target = event.target;
    const value = target.value;
    setParam(value);
  }

  let posPositions = [1,2,3,4,5,6,7,8,9].map((val) => {
    val = val
    if (props.selectedCards[val] != null) {
      return (<option key={val}>{val}</option>)
    }
  })

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
            {posPositions}
          </Input>
        </FormGroup>
    </Form>
    )

}

export {FormApprentice, resolveApprentice};