

import {Input, FormGroup, Form, Label } from 'reactstrap'

export default function SellAnItemForm(props) {


    let steal = props.steal;
    const setSteal = props.setSteal;
    const iron = props.iron;
    const setIron = props.setIron;


    function handleChange(event, setParam) {
        const target = event.target;
        const value = target.value;
        console.log(value)
        setParam(value);
    }

    return (
    <Form>
        <FormGroup>
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
        <FormGroup>
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
        <FormGroup>
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
    </Form>
    )

}