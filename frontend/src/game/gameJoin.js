import {useState} from "react";
import tokenService from "../services/token.service";
import { Link } from "react-router-dom";
import { Form, Input, Label, Button} from "reactstrap";
import getErrorModal from "../util/getErrorModal";
import getIdFromUrl from "../util/getIdFromUrl";
import useFetchState from "../util/useFetchState";
import '../static/css/game/gameJoin.css'; 


const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser()

export default function GameJoin() {

    const [game, setGame] = useState("");

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);


    const modal = getErrorModal(setVisible, visible, message);

    function handleSubmit(event) {

        event.preventDefault();

        

    }


    function handleSubmit(event) {

        event.preventDefault();
        
        fetch(
            "/api/v1/game/check" + (game ? "/" + game : ""),
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                }
            }
        )
        .then((response) => {
            if (response.ok) {
                fetch(
                    "/api/v1/game/join" + (game ? "/" + game : "") + (user.id ? "/" + user.id : ""),
                    {
                        method: "POST",
                        headers: {
                            Authorization: `Bearer ${jwt}`,
                            Accept: "application/json",
                            "Content-Type": "application/json",
                        }
                    }
                ).then((response) => {
                    console.log(response.text)
                    if (response.ok) {
                        window.location.href = `/game/${game}`
                    } else {
                        console.log("error", response)
                    }
                })
            }
        })/*
        .then((data) => {
            console.log(data);
            if(data==="")
                //window.location.href = "/game/";
                console.log("Not found")
            else{
                let json = JSON.parse(data);
                if(json.id){
                    window.location.href = `/game/${game}`
                }else
                    //window.location.href = "/game";
                    console.log("error in json format")
            }
        })*/

        .catch((message) => alert(message));

    }

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setGame(value);
    }


    return (
        <div className="center-container" style={{height: "100vh"}}>
                <div className="row" style={{height: "100vh", width: "100vw"}}>
                    <div className="col game-join-container" style={{height: "100vh", width: "100vw"}}>
                                <h1 className="text-center" style={{ color: 'white'}}>
                                    Join game
                                </h1>
                                <Form onSubmit={handleSubmit}>
                                    <div  className="custom-form-input">
                                        <Label for="name" className="custom-form-input-label"> code </Label>
                                        <Input
                                            type="text"
                                            required
                                            name="name"
                                            id="name"
                                            value={game || ""}
                                            onChange={handleChange}
                                            className="custom-input"
                                        />
                                    </div>
                                    <div className="custom-button-row">
                                        <Button className="btn btn-dark btn-lg" outline color="warning" size="lg">
                                            Join
                                        </Button>
                                    </div>
                                </Form>
                    </div>
                    <div className="col game-create-container">
                            <h1 className="text-center" style={{ color: 'white'}}>
                                Or create <br></br> your owm game...
                            </h1>
                            <div className="custom-button-row">
                                <Button className="btn btn-dark btn-lg" outline color="warning" size="lg">
                                    <Link to={`/game/edit`} style={{ color: 'rgb(238, 191, 47)'}}> Create </Link>
                                </Button>
                            </div>
                    </div>
            </div>
        </div>
    );
}