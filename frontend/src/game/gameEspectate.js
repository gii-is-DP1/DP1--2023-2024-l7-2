import { useEffect, useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import getErrorModal from "../util/getErrorModal";
import { Button, ButtonGroup, Table } from "reactstrap";
import getIdFromUrl from "../util/getIdFromUrl";
import Card from "../cards/card"
import SpecialCard from "../cards/specialCard";
import ChatModel from "./modals/ChatModel";
import useIntervalFetchState from "../util/useIntervalFetchState";

import '../static/css/game/objects.css'; 



const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser()

export default function GameEspectate() {
  const code = getIdFromUrl(2);

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);

  const [gameRound, setGameRound] = useState(null);
  const [showChat,setShowChat] = useState(false);
  const [cards, setCards] = useIntervalFetchState(
    [],
    `/api/v1/game/play/${code}/getCards`,
    jwt,
    setMessage,
    setVisible,
    code
  );
  const [specialCards, setSpecialCards] = useIntervalFetchState(
    [],
    `/api/v1/game/play/${code}/getSpecialCards`,
    jwt,
    setMessage,
    setVisible,
    code
  );


  const emptySelectedCards = {1: null,2: null,3: null,
    4: null,5: null,6: null,
    7: null,8: null,9: null}
  const [selectedCards,setSelectedCards] = useState(emptySelectedCards)

  const [game, setGame] = useIntervalFetchState(
      {},
      `/api/v1/game/play/${code}`,
      jwt,
      setMessage,
      setVisible,
      code
  );
  
  /*
  let spectator;
  if (game != {} && game.spectators) {
    spectator = game.spectators.filter(s => s.name === user.username)[0];
    if (spectator == null) spectator = {};
  }*/

  useEffect(()=>{
    if (game != {} && game.finish) {
      window.location.href = `/game/${code}/finish`
    }
  },[game])

  const [players, setPlayers] = useIntervalFetchState(
    [],
    `/api/v1/game/play/${code}/players`,
    jwt,
    setMessage,
    setVisible,
    code
  );
  

  const [playerTurn, setPlayerTurn] = useIntervalFetchState(
    null,
    `/api/v1/game/play/${code}/isMyTurn`,
    jwt,
    setMessage,
    setVisible,
    code
  );

  
  const [dwarves, setDwarves] = useIntervalFetchState(
    [],
    `/api/v1/game/play/${code}/dwarves`,
    jwt,
    setMessage,
    setVisible,
    code
  );


  const modal = getErrorModal(setVisible, visible, message);

  useEffect(() => {
    let updated = {1: null,2: null,3: null,
      4: null,5: null,6: null,
      7: null,8: null,9: null};
    
    let updatedDwarves = [];
    for (const d of dwarves) {
        if (!d.player) continue;
        const c = d.card;
        if (c==null) {
          continue;
        }


        const pacolor = d.player.color;
        //console.log(c.id + " to color => " + pacolor);
        updated[c.position] = pacolor;  
    }

    setSelectedCards(updated)

    if (game.round !== gameRound) {
      setGameRound(game.round)
    } 
  }, [game,dwarves])




  function getCardColor(id,card) {
    let color = selectedCards[id] !== null ? selectedCards[id] : "white" 

    return color
  }

  const playerList = players.map((play) => {
    return (
      <tr key={play.id} style={{ color: play.color, backgroundColor: "grey" }}>
        <td style={{ color: play.color }} className="text-center">{play.name}</td>
        <td style={{ color: play.color }} className="text-center">Iron: {play.iron}</td>
        <td style={{ color: play.color }} className="text-center">Gold: {play.gold}</td>
        <td style={{ color: play.color }} className="text-center">Steal: {play.steal}</td>
        <td style={{ color: play.color }} className="text-center">Medals: {play.medal}</td>
        <td style={{ color: play.color }} className="text-center">Objects: {play.objects.map(
          (object) => {
            return (
              <div className={object.name}>
                <br></br>
              </div>
            )
          }
        )}</td>
        {game.playerStart && play.name === game.playerStart.name && 
            <td style={{ color: "red" }} >Round Starter</td>
        }        
        {playerTurn && playerTurn.name && play.name === playerTurn.name && 
            <td style={{ color: "red" }} >Current turn</td>
        }
      </tr>
    )
  })

  return (
    <>
    <ChatModel
      isOpen={showChat}
      toggle={() => {
        setShowChat(!showChat)
      }}
      code={code}
    ></ChatModel>

    {getErrorModal(setVisible,visible,message)}
    <div style={{marginTop: "70px"}}>

      <div className="admin-page-container">
        <h1 className="text-center">Game: {game.name} - Round: {game.round}</h1>

        <Button 
          onClick={() => {
            setShowChat(!showChat)}}
          title="showChat"
          color="#008000"
          style={{border: '3px solid black',padding: "3px"}}>
          Show Chat
        </Button>
        

        <section className="generalLayout" style={{display:"flex", flexDirection:"column"}}>
          <section>
            <Table>
              <tbody>
              {playerList}
              </tbody>
            </Table>
          </section>
          {cards.length != 0 &&
          <section className="cardDeckLayout">
            <section className="cardDeckLayoutRow1" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[0].id} 
                    color={getCardColor(1,cards[0])}/>
              <Card id={cards[1].id}
                    color={getCardColor(2,cards[1])}/>
              <Card id={cards[2].id} 
                    color={getCardColor(3,cards[2])}/>
            </section>
            <section className="cardDeckLayoutRow2" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[3].id}
                    color={getCardColor(4,cards[3])}/>
              <Card id={cards[4].id} 
                    color={getCardColor(5,cards[4])}/>
              <Card id={cards[5].id} 
                    color={getCardColor(6,cards[5])}/>
            </section>
            <section className="cardDeckLayoutRow3" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[6].id} 
                    color={getCardColor(7,cards[6])}/>
              <Card id={cards[7].id} 
                    color={getCardColor(8,cards[7])}/>
              <Card id={cards[8].id} 
                    color={getCardColor(9,cards[8])}/>
            </section>
          </section>
          }

          <section className="specialCardDeckLayout"  style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
            {specialCards && specialCards[0] &&
              <SpecialCard id={specialCards[0].id} />
            }
            {specialCards && specialCards[1] &&
              <SpecialCard id={specialCards[1].id}/>
            }
            {specialCards && specialCards[2] &&
              <SpecialCard id={specialCards[2].id} />
            }
          </section>
          
        </section>
      </div>
    </div>
    </>
  );


}
