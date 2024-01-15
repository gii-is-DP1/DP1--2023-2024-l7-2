import {useState, useEffect} from "react";
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
    const [pageSize, setPageSize] = useState(3);
    const [page, setPage] = useState(0);
    const [count, setCount] = useState(0);
    const [publicGames, setData] = useState({content:[],size:pageSize, first:true,last:false});
    const [invitedGames, setInvitedGames] = useFetchState(
        [],
        `/api/v1/invite`,
        jwt,
        setMessage,
        setVisible
      );

    const modal = getErrorModal(setVisible, visible, message);

    

    function handleSubmit(event) {

        event.preventDefault();
        

        fetch(
            "/api/v1/game/join" + (game ? "/" + game : ""),
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
            } else if(response.status == 404) {
                setMessage("Game not found")
                setVisible(true)
            } else if(response.status == 400) {
                setMessage("Game has already started")
                setVisible(true)
            } else if(response.status == 403) {
                setMessage("Too many players in game")
                setVisible(true)    
            } else {
                setMessage("Failed to join the game")
                setVisible(true)
                console.log("error", response)
            }
        }).catch((message) => alert(message));
    }

    const handleJoinClick = (game) => {
  
        fetch(
          "/api/v1/game/join" + (game ? "/" + game : ""),
          {
            method: "POST",
            headers: {
              Authorization: `Bearer ${jwt}`,
              Accept: "application/json",
              "Content-Type": "application/json",
            }
          }
        ).then((response) => {
          if (response.ok) {
            return response.json()
          } else if (response.status == 404) {
            setMessage("Game not found")
            setVisible(true)
        } else if(response.status == 400) {
            setMessage("Game has already started")
            setVisible(true)
        } else if(response.status == 403) {
            setMessage("Too many players in game")
            setVisible(true)
          } else {
            setMessage("Failed to join the game")
            setVisible(true)
            throw new Error("Failed to join the game");
          }
        })
        .then((response) => {
          console.log(response)
          if (response.code) {
            window.location.href = `/game/${response.code}`;
          } else {
            setMessage("Failed to join the game")
            setVisible(true)
            throw new Error("Failed to join the game");
          }
        })
        .catch((error) => {
            setMessage("Failed to join the game")
            setVisible(true)
          setMessage(error.message);
        });
      };


      useEffect(() => {
        fetch("/api/v1/game/publics?page="+page+"&size="+pageSize, {
            headers: {
                "Authorization": `Bearer ${jwt}` ,
            },
        }).then((response) => response.json()).then(json => {
            if(json.message){
                setMessage (json.message);
                setVisible(json.setVisible);
            }else{
                setData(json);
                setCount (json.totalElements);
            }
            }).catch((error) => {window.alert(error);});
        }, [page ,pageSize]);
              
    function handleNextPage(){
        if(publicGames.content.length >= pageSize)
            setPage (page+1);
    }
    function handlePreviousPage(){
        if(page > 0)
            setPage (page-1);
    }

    const gameList = publicGames.content.map((publicGame) => {
        if (!publicGame.start) {
            return (
                <div key={publicGame.id} style={{ border: '2px solid white', borderRadius: '8px', marginBottom: '15px', padding: '5px', margin: '5px', color: 'white'}}>
                    {publicGame.name} - Players: {publicGame.players.length}
                    <Button className="btn btn-dark btn-sm" style={{marginLeft: '5px'}} onClick={() =>  handleJoinClick(publicGame.code)}>Join</Button>
                </div>
            );
        }
    });

    const invitationList = invitedGames.map((game) => {
        if (game.finish === null) {
            return (
                <div key={game.id} style={{ border: '2px solid white', borderRadius: '8px', marginBottom: '15px', padding: '5px', margin: '5px', color: 'white'}}>
                    {game.name} - Players: {game.players.length}
                    <Button className="btn btn-dark btn-sm" style={{marginLeft: '5px'}} onClick={() =>  handleJoinClick(game.code)}>Join</Button>
                </div>
            );
        } else {
            return null;
        }
    });

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setGame(value);
    }

    return (
        <div>
            {getErrorModal(setVisible,visible,message)}
            <div className="row" style={{height: "100vh", width: "100vw"}}>
                <div className="col game-join-container" style={{height: "100vh", width: "100vw"}}>
                            <div className="custom-button-column">
                                <h1 className="text-center" style={{ color: 'white'}}>
                                    Join a game
                                </h1>
                                <Form onSubmit={handleSubmit}>
                                    <div  className="custom-form-input" style={{backgroundColor:'white'}}>
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
                                <br></br>
                            </div>
                            <div className="custom-button-column">
                                <h4 className="text-center" style={{ color: 'white', textDecoration: 'underline'}}>
                                    Join public games
                                </h4>
                                {gameList}
                                {invitationList.length < 3 ? 
                                <div className="pagination-container" style={{ margin: "20px" }}>
                                    <Button style={{ marginRight: "10px" }} onClick={handlePreviousPage}>
                                        Prev Page
                                    </Button>

                                    <Button onClick={handleNextPage}>
                                        Next Page
                                    </Button>
                                </div>
                                :<br></br>}
                            </div>
                </div>
                <div className="col game-create-container">
                        <h1 className="text-center" style={{ color: 'white'}}>
                            Or create <br></br> your owm game...
                        </h1>
                        <div className="custom-button-row">
                            <Button className="btn btn-dark btn-lg" outline color="warning" size="lg">
                                <Link to={`/game/edit/new`} style={{ color: 'rgb(238, 191, 47)'}}> Create </Link>
                            </Button>
                        </div>
                        <br></br>
                        <div className="custom-button-column">
                                    <h4 className="text-center" style={{ color: 'white', textDecoration: 'underline'}}>
                                        Invitations
                                    </h4>
                                    {invitationList.length > 0 ? (
                                        invitationList
                                    ) : (
                                        <p className="text-center" style={{ color: 'white' }}>
                                            "No invitations yet"
                                        </p>
                                    )}
                        </div>
                        
                </div>
            </div>
        </div>
    );

    
    




}