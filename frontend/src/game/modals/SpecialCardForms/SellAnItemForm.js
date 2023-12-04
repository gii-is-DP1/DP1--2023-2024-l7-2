import {Input, FormGroup, Form, Label } from 'reactstrap'


function resolveSellAnItem(code, jwt, payload) {
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

function FormSellAnItem(props) {


    let steal = props.steal;
    const setSteal = props.setSteal;
    const iron = props.iron;
    const setIron = props.setIron;


    function handleChange(event, setParam) {
        const target = event.target;
        const value = target.value;
        setParam(value);
    }

    return (
    <Form>
        <FormGroup style={{display:'flex',flexDirection:'row'}}>
            <Label for="gold"> Gold </Label>
            <Input
                type="number"
                required
                name="gold"
                id="gold"
                value={props.gold}
                onChange={(event) => handleChange(event,props.setGold)}
                className="custom-input"
            />
        </FormGroup>
        <FormGroup style={{display:'flex',flexDirection:'row'}}>
            <Label for="steal"> Steal </Label>
            <Input
                type="number"
                required
                name="steal"
                id="steal"
                value={props.steal}
                onChange={(event) => handleChange(event,props.setSteal)}
                className="custom-input"
            />
        </FormGroup>
        <FormGroup style={{display:'flex',flexDirection:'row'}}>
            <Label for="iron"> Iron </Label>
            <Input
                type="number"
                required
                name="iron"
                id="iron"
                value={props.iron}
                onChange={(event) => handleChange(event,props.setIron)}
                className="custom-input"
            />
        </FormGroup>
        <FormGroup style={{display:'flex',flexDirection:'row'}}>
          <Input
            id="exampleSelect"
            name="select"
            type="select"
            onChange={(event) => handleChange(event,props.setObjectSelected)}
          >
            <option key={0}>-</option>
            {props.gameObject.map((object) => {
              return (
                <option key={object.id}>
                  {object.name}
                </option>
              )
            })}
          </Input>
        </FormGroup>
    </Form>
    )

}

export {FormSellAnItem, resolveSellAnItem};