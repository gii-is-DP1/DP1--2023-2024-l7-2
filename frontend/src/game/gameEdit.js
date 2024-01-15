import {useState} from "react";
import tokenService from "../services/token.service";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import getErrorModal from "./../util/getErrorModal";
import getIdFromUrl from "./../util/getIdFromUrl";
import useFetchState from "./../util/useFetchState";
import jwt_decode from "jwt-decode";

const jwt = tokenService.getLocalAccessToken();

export default function GameEdit() {
    const id = getIdFromUrl(3);
    const emptyGame = {
        id: id==="new"?null:id,
        name: "",
        code: null,
        start: null,
        finish: null,
        winner_id: null,
        round: 0,
        playerCreator: null
    };
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    
    const [game, setGame] = useFetchState(
        emptyGame,
        `/api/v1/game/${id}`,
        jwt,
        setMessage,
        setVisible,
        id
    );

    // if (id !== "new") {
    //     fetch(`/api/v1/game/${id}`, {
    //         method: "GET",
    //         headers: {
    //           "Content-Type": "application/json",
    //           Authorization: `Bearer ${jwt}`,
    //           Accept: 'application/json',
    //         }
    //       }).then(response => response.json()).then(response => setGame(response))
    // } 

    const modal = getErrorModal(setVisible, visible, message);
    const role = jwt_decode(jwt).authorities[0];

    function getRandomCode(name) {
        return Math.random()*550050+name+Math.random()*110010
    }

    function handleSubmit(event) {

        event.preventDefault();

        if(game.isPublic) {
            game.code = getRandomCode(game.name)
        }

        fetch(
            "/api/v1/game" + (game.id ? "/" + game.id : ""),
            {
                method: game.id ? "PUT" : "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(game),
            }
        )
        .then((response) => {
            if (response.status == 400) {
                setMessage("Game code already taken");
                setVisible(true);
                return "";
                
            } else {
                return response.text()
            }
        })
        .then((data) => {
            if(data==="")
            //window.location.href = role=="USER" ? `/game` : `/games`;
                return;
            else{
                let json = JSON.parse(data);
                if(json.message){
                    setMessage(JSON.parse(data).message);
                    setVisible(true);
                }else
                    window.location.href = role=="USER" ? `/game/${json.code}` : "/games";
            }
        })

        .catch((message) => alert(message));
    }


    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setGame({ ...game, [name]: value });
    }


    return (
        <div className="auth-page-container" style={{height: "100vh"}}>
            <h1 className="text-center">
                {game.id ? "Edit Game" : "Add Game"}
            </h1>
            <div  className="custom-form-input">
                {modal}
                <Form onSubmit={handleSubmit}>
                    <div  className="custom-form-input">
                        <Label for="name" className="custom-form-input-label"> Name </Label>
                        <Input
                            type="text"
                            required
                            name="name"
                            id="name"
                            value={game.name || ""}
                            onChange={(e) => setGame({ ...game, name: e.target.value })}
                            className="custom-input"
                        />
                    </div>
                    {
                        !game.isPublic &&
                        <div  className="custom-form-input">
                            <Label for="code" className="custom-form-input-label"> Code </Label>
                            <Input
                                type="text"
                                name="code"
                                id="code"
                                value={game.code || ""}
                                onChange={(e) => setGame({ ...game, code: e.target.value })}
                                className="custom-input"
                            />
                        </div>
                    }
                    <div className="custom-form-input">
                        <Label for="isPublic" className="custom-form-input-label">Public</Label>
                        <Input
                            type="checkbox"
                            name="isPublic"
                            checked={game.isPublic === true}
                            unselectable="on"
                            onChange={(e) => setGame({ ...game, isPublic: e.target.checked })}
                        />
                    </div>

                    <br></br>
                    <div className="custom-button-row">
                        <button className="auth-button">Save</button>
                        <Link to={`/game`} className="auth-button" style={{ textDecoration: "none" }}> Cancel </Link>
                    </div>
                </Form>
            </div>
        </div>
    );
}