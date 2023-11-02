import {useState} from "react";
import tokenService from "../services/token.service";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import getErrorModal from "../util/getErrorModal";
import getIdFromUrl from "../util/getIdFromUrl";
import useFetchState from "../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function GameJoin() {

    let game = {
        id: null,
        name: "",
        code: "",
        start: null,
        finish: null,
        winner_id: null,
        round: 0
    };

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);


    const modal = getErrorModal(setVisible, visible, message);

    function handleSubmit(event) {

        event.preventDefault();

        

    }


    function handleChange(event) {
        const target = event.target;
        const code = target.code;
        
        fetch(
            "/api/v1/game" + (code ? "/" + code : ""),
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(game),
            }
        )
        .then((response) => response.text())
        .then((data) => {
            if(data==="")
                //window.location.href = "/game/";
                console.log("Not found")
            else{
                let json = JSON.parse(data);
                if(json.id){
                    window.location.href = `/game/${json.id}`
                }else
                    //window.location.href = "/game";
                    console.log("error in json format")
            }
        })

        .catch((message) => alert(message));

    }


    return (
        <div className="auth-page-container">
            <h2 className="text-center">
                Join game
            </h2>
            <div className="auth-form-container">
                {modal}
                <Form onSubmit={handleSubmit}>
                    <div  className="custom-form-input">
                        <Label for="name" className="custom-form-input-label"> code </Label>
                        <Input
                            type="text"
                            required
                            name="name"
                            id="name"
                            value={""}
                            onChange={handleChange}
                            className="custom-input"
                        />
                    </div>
                    <div className="custom-button-row">
                        <button className="auth-button">Join</button>
                        <Link to={`/game`} className="auth-button" style={{ textDecoration: "none" }}> Cancel </Link>
                    </div>
                </Form>
            </div>
        </div>
    );
}